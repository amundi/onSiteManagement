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
    selectedSite: null,
    selectedRestaurant: null,
    siteUsages: [],
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
    SET_SELECTED_SITE: (state, selectedSite) => {
      state.selectedSite = selectedSite;
    },
    SET_SITE_USAGES: (state, siteUsages) => {
      state.siteUsages = siteUsages;
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
    async getUserInfo({ commit, dispatch }) {
      axios
        .get("api/auth/userInfo")
        .then(response => {
          if (!response.data || !response.data.login || !response.data.role || response.data.role == 'none') {
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
        .get("api/setting/all")
        .then(response => {
          commit("SET_SETTINGS", response.data.settings);
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async saveSettings({ commit }, settings) {
      axios
        .post("api/setting/all", { settings })
        .then(response => {
          commit("SET_SETTINGS", response.data.settings);
          commit("SET_INFO", "Settings saved");
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
    async changeOverridenUser({ commit, dispatch }, overridenUser) {
      commit("SET_OVERRIDEN_USER", overridenUser);
      if (overridenUser) {
        dispatch("reloadAll");
      } else {
        window.location.href = "/";
      }
    },
    async changeSelectedSite({ commit, dispatch, state }, selectedSite) {
      commit("SET_SELECTED_SITE", selectedSite);
      if (state.sites) {
        for (let site of state.sites) {
          if (site.definition && site.definition.id == selectedSite.id) {
            dispatch("loadSiteUsages", selectedSite);
            break;
          }
        }
      }
    },
    async loadSiteUsages({ commit }, selectedSite) {
      if (selectedSite) {
        axios
          .get(
            "api/usage/site/basic/" +
              selectedSite.id +
              (selectedSite.restaurant && selectedSite.restaurant.id
                ? "?restaurant=" + selectedSite.restaurant.id
                : "")
          )
          .then(response => {
            commit("SET_SITE_USAGES", response.data);
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
          .get("api/usage/user/" + user)
          .then(response => {
            commit("SET_CURRENT_USER_USAGES", response.data);
            if (!state.selectedSite && response.data && response.data.length > 0) {
              dispatch("changeSelectedSite", response.data[0].site);
            }
          })
          .catch(err => {
            commit("SET_INFO", "Error: " + err.message + " (while retrieving current user usages)");
          });
      }
    },
    async saveCurrentUserUsages({ commit, dispatch, state }, usages) {
      axios
        .post("api/workingDays", usages)
        .then(response => {
          commit("SET_CURRENT_USER_USAGES", response.data.workingDays);
          commit("SET_INFO", "working days updated");
          dispatch("loadSiteUsages", state.selectedSite);
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async deleteCurrentUserUsages({ commit, dispatch, state }, usages) {
      axios
        .post("api/workingDays/delete", usages)
        .then(response => {
          commit("SET_CURRENT_USER_USAGES", response.data.workingDays);
          commit("SET_INFO", "working days updated");
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
          commit("SET_INFO", "User roles saved");
        })
        .catch(err => {
          commit("SET_INFO", "Error: " + err.message);
        });
    },
    async reloadAll({ dispatch }) {
      dispatch("getUserInfo");
      dispatch("loadSettings");
      dispatch("loadRestaurants");
      dispatch("loadSites");
      dispatch("loadSiteUsages");
      dispatch("loadCurrentUserUsages");
    }
  },
  getters: {
    hue: state => state.hue,
    user: state => state.user,
    overridenUser: state => state.overridenUser,
    currentUser: state => (state.overridenUser != null ? state.overridenUser : state.user.login),
    settings: state => state.settings,
    userName: state => state.userName,
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
    restaurantDefinitions: state => {
      let defs = [];
      if (state.restaurants) {
        for (let restaurant of state.restaurants) {
          defs.push(restaurant.definition);
        }
      }
      return defs;
    },
    restaurants: state => state.restaurants,
    selectedSite: state => state.selectedSite,
    selectedRestaurant: state => state.selectedRestaurant,
    siteUsages: state => state.siteUsages,
    userRoles: state => state.userRoles,
    currentUserUsages: state => state.currentUserUsages
  },
  modules: {}
});
