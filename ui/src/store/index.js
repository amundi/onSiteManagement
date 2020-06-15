import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";
import router from "@/router";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    hue: "light",
    user: null,
    settings: [],
    userRoles: null,
    overridenUser: null,
    info: null,
    sites: [],
    restaurants: [],
    parkings: [],
    selectedSite: null,
    selectedRestaurant: null,
    selectedParking: null,
    siteUsages: [],
    restaurantUsages: [],
    parkingUsages: [],
    currentUserUsages: []
  },
  mutations: {
    SET_HUE: (state, hue) => {
      state.hue = hue;
    },
    SET_USER: (state, user) => {
      state.user = user;
    },
    SET_SETTINGS: (state, settings) => {
      state.settings = settings;
    },
    SET_OVERRIDEN_USER: (state, overridenUser) => {
      state.overridenUser = overridenUser;
    },
    SET_INFO: (state, info) => {
      state.info = info;
    },
    SET_SITES: (state, sites) => {
      state.sites = sites;
    },
    SET_RESTAURANTS: (state, restaurants) => {
      state.restaurants = restaurants;
    },
    SET_PARKINGS: (state, parkings) => {
      state.parkings = parkings;
    },
    SET_SELECTED_SITE: (state, selectedSite) => {
      state.selectedSite = selectedSite;
    },
    SET_SELECTED_RESTAURANT: (state, selectedRestaurant) => {
      state.selectedRestaurant = selectedRestaurant;
    },
    SET_SELECTED_PARKING: (state, selectedParking) => {
      state.selectedParking = selectedParking;
    },
    SET_SITE_USAGES: (state, siteUsages) => {
      state.siteUsages = siteUsages;
    },
    SET_RESTAURANT_USAGES: (state, restaurantUsages) => {
      state.restaurantUsages = restaurantUsages;
    },
    SET_PARKING_USAGES: (state, parkingUsages) => {
      state.parkingUsages = parkingUsages;
    },
    SET_USER_ROLES: (state, userRoles) => {
      state.userRoles = userRoles;
    },
    SET_CURRENT_USER_USAGES: (state, currentUserUsages) => {
      state.currentUserUsages = currentUserUsages;
    },
    LOAD_LOCALSTORAGE: state => {
      if (localStorage.getItem("state")) {
        Object.assign(state, JSON.parse(localStorage.getItem("state")));
      }
    },
    CHANGE_HUE: state => {
      state.hue = state.hue === "light" ? "dark" : "light";
      localStorage.setItem("state", JSON.stringify({ hue: state.hue }));
    }
  },
  actions: {
    async setInfo({ commit }, message) {
      commit("SET_INFO", message);
      setTimeout(() => {
        commit("SET_INFO", null);
      }, 3000);
    },
    async getUserInfo({ commit, dispatch }) {
      axios
        .get("api/auth/userInfo")
        .then(response => {
          if (
            !response.data ||
            !response.data.login ||
            !response.data.role ||
            response.data.role == "none"
          ) {
            router.push("/denied");
          } else {
            commit("SET_USER", response.data);
            dispatch("loadCurrentUserUsages");
          }
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
          router.push("/denied");
        });
    },
    async loadSettings({ commit }) {
      axios
        .get("api/settings")
        .then(response => {
          commit("SET_SETTINGS", response.data.settings);
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async saveSettings({ commit, dispatch }, settings) {
      axios
        .post("api/settings", { settings })
        .then(response => {
          commit("SET_SETTINGS", response.data.settings);
          dispatch("setInfo", "Settings saved");
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async loadSites({ commit }) {
      axios
        .get("api/site/all")
        .then(response => {
          commit("SET_SITES", response.data);
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async loadRestaurants({ commit }) {
      axios
        .get("api/restaurant/all")
        .then(response => {
          commit("SET_RESTAURANTS", response.data);
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async loadParkings({ commit }) {
      axios
        .get("api/parking/all")
        .then(response => {
          commit("SET_PARKINGS", response.data);
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async changeOverridenUser({ commit, dispatch }, overridenUser) {
      commit("SET_OVERRIDEN_USER", overridenUser);
      if (overridenUser) {
        dispatch("reloadAll");
      } else {
        window.location.href = "/";
      }
    },
    async changeSelectedSite({ commit, dispatch }, selectedSite) {
      if (selectedSite) {
        commit("SET_SELECTED_SITE", selectedSite);
        dispatch("loadSiteUsages", selectedSite);
      }
    },
    async changeSelectedRestaurant({ commit, dispatch }, selectedRestaurant) {
      if (selectedRestaurant) {
        commit("SET_SELECTED_RESTAURANT", selectedRestaurant);
        dispatch("loadRestaurantUsages", selectedRestaurant);
      }
    },
    async changeSelectedParking({ commit, dispatch }, selectedParking) {
      if (selectedParking) {
        commit("SET_SELECTED_PARKING", selectedParking);
        dispatch("loadParkingUsages", selectedParking);
      }
    },
    async loadSiteUsages({ commit }, selectedSite) {
      if (selectedSite) {
        axios
          .get("api/usage/site/" + selectedSite)
          .then(response => {
            commit("SET_SITE_USAGES", response.data);
          })
          .catch(err => {
            commit("SET_INFO", "Error: " + err.message);
          });
      }
    },
    async loadRestaurantUsages({ commit }, selectedRestaurant) {
      if (selectedRestaurant) {
        axios
          .get("api/usage/restaurant/" + selectedRestaurant)
          .then(response => {
            commit("SET_RESTAURANT_USAGES", response.data);
          })
          .catch(err => {
            commit("SET_INFO", "Error: " + err.message);
          });
      }
    },
    async loadParkingUsages({ commit }, selectedParking) {
      if (selectedParking) {
        axios
          .get("api/usage/parking/" + selectedParking)
          .then(response => {
            commit("SET_PARKING_USAGES", response.data);
          })
          .catch(err => {
            commit("SET_INFO", "Error: " + err.message);
          });
      }
    },
    async loadCurrentUserUsages({ commit, state, dispatch }, user) {
      if (!user) {
        if (state.overridenUser) {
          user = state.overridenUser;
        } else if (state.user) {
          user = state.user.login;
        }
      }
      if (user) {
        axios
          .get("api/day/user/" + user)
          .then(response => {
            commit("SET_CURRENT_USER_USAGES", response.data);
            if (!state.selectedSite && response.data && response.data.length > 0) {
              dispatch("changeSelectedSite", response.data[0].siteId);
            }
          })
          .catch(err => {
            commit("SET_INFO", "Error: " + err.message + " (while retrieving current user usages)");
          });
      }
    },
    async saveCurrentUserUsages({ commit, dispatch, state }, usages) {
      axios
        .post("api/day", usages)
        .then(response => {
          commit("SET_CURRENT_USER_USAGES", response.data.userDays);
          dispatch("setInfo", "working days updated");
          dispatch("loadSiteUsages", state.selectedSite);
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async deleteCurrentUserUsages({ commit, dispatch, state }, usages) {
      axios
        .post("api/day/delete", usages)
        .then(response => {
          commit("SET_CURRENT_USER_USAGES", response.data.userDays);
          dispatch("setInfo", "working days updated");
          dispatch("loadSiteUsages", state.selectedSite);
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async loadUserRoles({ commit }) {
      axios
        .get("api/role/all")
        .then(response => {
          commit("SET_USER_ROLES", response.data.userRoles);
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async saveUserRoles({ commit }, userRoles) {
      axios
        .post("api/role/all", { userRoles })
        .then(response => {
          commit("SET_USER_ROLES", response.data.userRoles);
          this.dispatch("setInfo", "User roles saved");
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async reloadAll({ dispatch }) {
      dispatch("loadSettings");

      dispatch("loadSites");
      dispatch("loadRestaurants");
      dispatch("loadParkings");
      dispatch("loadSiteUsages");
      dispatch("loadRestaurantUsages");
      dispatch("loadParkingUsages");

      dispatch("getUserInfo");
    }
  },
  getters: {
    hue: state => state.hue,
    settings: state => state.settings,
    info: state => state.info,

    sites: state => state.sites,
    siteDefinitions: state => {
      let defs = [];
      if (state.sites) {
        for (let site of state.sites) {
          defs.push(site.definition);
        }
      }
      return defs;
    },
    siteUsages: state => state.siteUsages,
    selectedSite: state => state.selectedSite,

    restaurants: state => state.restaurants,
    restaurantDefinitions: state => {
      let defs = [];
      if (state.restaurants) {
        for (let restaurant of state.restaurants) {
          defs.push(restaurant.definition);
        }
      }
      return defs;
    },
    restaurantUsages: state => state.restaurantUsages,
    selectedRestaurant: state => state.selectedRestaurant,

    parkings: state => state.parkings,
    parkingDefinitions: state => {
      let defs = [];
      if (state.parkings) {
        for (let parking of state.parkings) {
          defs.push(parking.definition);
        }
      }
      return defs;
    },
    parkingUsages: state => state.parkingUsages,
    selectedParking: state => state.selectedParking,

    user: state => state.user,
    userName: state => state.userName,
    overridenUser: state => state.overridenUser,
    currentUser: state => (state.overridenUser != null ? state.overridenUser : state.user.login),
    userRoles: state => state.userRoles,
    currentUserUsages: state => state.currentUserUsages
  },
  modules: {}
});
