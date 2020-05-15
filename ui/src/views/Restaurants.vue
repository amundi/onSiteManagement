<template>
  <div id="restaurants" class="w-full" :class="[hue]">
    <div class="wrapper w-full p-4">
      <div class="create w-full select-none">
        <div class="buttons flex">
          <span
            class="button create-button ml-3"
            @click="
              createdRestaurant =
                createdRestaurant == null
                  ? { name: '', description: '', path: '#France #Paris' }
                  : null
            "
          >
            <font-awesome-icon class="menu" icon="plus"></font-awesome-icon>&nbsp;create restaurant
          </span>
          <span class="button ml-1" @click="reloadRestaurants()">
            <font-awesome-icon class="menu" icon="sync-alt"></font-awesome-icon>&nbsp;reload
          </span>
        </div>
        <div class="content w-full" v-if="createdRestaurant != null">
          <div class="flex items-center mb-2">
            <div class="w-1/3">
              <label class="block text-right mb-1 mb-0 pr-4" for="inline-name">Name</label>
            </div>
            <div class="w-2/3">
              <input
                class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                id="inline-name"
                type="text"
                v-model="createdRestaurant.name"
              />
            </div>
          </div>
          <div class="flex items-center mb-2">
            <div class="w-1/3">
              <label class="block text-right mb-1 mb-0 pr-4" for="inline-desc">Description</label>
            </div>
            <div class="w-2/3">
              <input
                class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                id="inline-desc"
                type="text"
                v-model="createdRestaurant.description"
              />
            </div>
          </div>
          <div class="flex items-center mb-2">
            <div class="w-1/3">
              <label class="block text-right mb-1 mb-0 pr-4" for="inline-path">Tags</label>
            </div>
            <div class="w-2/3">
              <input
                class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                id="inline-path"
                type="text"
                v-model="createdRestaurant.path"
              />
            </div>
          </div>

          <div class="buttons select-none flex justify-end">
            <span class="button cancel" @click="createdRestaurant = null">cancel</span>
            <span class="button" @click="createRestaurant(createdRestaurant)">save</span>
          </div>
        </div>
      </div>
      <div class="w-full">
        <div class="restaurant" v-for="restaurant of orderedRestaurants" :key="restaurant.id">
          <template v-if="restaurant.definition">
            <div class="main select-none">
              <div class="flex justify-end pr-2">
                <div class="buttons flex">
                  <template v-if="restaurant != editedRestaurant">
                    <span class="button edit obfuscable" @click="editedRestaurant = restaurant">
                      <font-awesome-icon icon="pen"></font-awesome-icon>&nbsp;edit
                    </span>
                    <span class="button delete obfuscable" @click="deleteRestaurant(restaurant)">
                      <font-awesome-icon icon="times"></font-awesome-icon>&nbsp;delete
                    </span>
                  </template>
                  <span class="button save obfuscable" @click="saveRestaurant(restaurant)">
                    <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;save
                  </span>
                  <span
                    class="menu pl-2 cursor-pointer"
                    @click="
                      expandedRestaurant = restaurant == expandedRestaurant ? null : restaurant
                    "
                  >
                    <font-awesome-icon icon="bars"></font-awesome-icon>
                  </span>
                </div>
              </div>
              <div class="flex">
                <template v-if="restaurant != editedRestaurant">
                  <div class="name value whitespace-no-wrap">{{ restaurant.definition.name }}</div>
                  <div class="description value w-full">
                    {{ restaurant.definition.description }}
                  </div>
                </template>
                <template v-if="restaurant == editedRestaurant">
                  <div class="name">
                    <input
                      class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                      id="inline-name"
                      type="text"
                      v-model="restaurant.definition.name"
                    />
                  </div>
                  <div class="description w-full">
                    <input
                      class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                      id="inline-desc"
                      type="text"
                      v-model="restaurant.definition.description"
                    />
                  </div>
                </template>
              </div>

              <template v-if="restaurant != editedRestaurant && restaurant.definition.path">
                <div class="tags flex">
                  <span
                    class="tag"
                    v-for="tag of restaurant.definition.path.split(' ')"
                    :key="tag"
                    >{{ tag }}</span
                  >
                </div>
              </template>
              <template v-if="restaurant == editedRestaurant">
                <div class="tags w-full">
                  <input
                    class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                    id="inline-path"
                    type="text"
                    v-model="restaurant.definition.path"
                  />
                </div>
              </template>
            </div>
          </template>
          <div class="capacities w-full">
            <RestaurantCapacities
              :restaurant="restaurant"
              :saveRestaurant="saveRestaurant"
            ></RestaurantCapacities>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import store from "../store/index";
import { mapGetters } from "vuex";
import axios from "axios";

import RestaurantCapacities from "@/components/RestaurantCapacities";

export default {
  store,
  components: { RestaurantCapacities },
  computed: {
    ...mapGetters(["hue", "restaurants"]),
    orderedRestaurants() {
      return this.restaurants.slice().sort((a, b) => {
        return a.definition.name.localeCompare(b.definition.name);
      });
    }
  },
  data() {
    return {
      createdRestaurant: null,
      expandedRestaurant: null,
      editedRestaurant: null
    };
  },
  methods: {
    createRestaurant(definition, capacities) {
      let vm = this;
      axios
        .post("api/restaurant/", {
          definition: definition,
          capacities: capacities
        })
        .then(response => {
          if (response.data && response.data.definition && response.data.definition.name) {
            store.commit(
              "SET_INFO",
              "Restaurant " + response.data.definition.name + " successfully created"
            );
          } else {
            console.log(response.data);
            store.commit("SET_INFO", "Error while creating restaurant");
          }
          store.dispatch("loadRestaurants");
        })
        .catch(err => {
          store.commit(
            "SET_INFO",
            "Error while creating restaurant: " +
              (err.response && err.response.data ? err.response.data.message : err.message)
          );
        });
      vm.createdRestaurant = null;
    },
    reloadRestaurants() {
      this.createdRestaurant = null;
      this.editedRestaurant = null;
      store.dispatch("reloadAll");
    },
    saveRestaurant(restaurant) {
      let vm = this;
      axios
        .post("api/restaurant/", restaurant)
        .then(() => {
          store.commit("SET_INFO", "Restaurant " + restaurant.definition.name + "  saved.");
          vm.reloadRestaurants();
        })
        .catch(err => {
          store.commit(
            "SET_INFO",
            "Error while saving restaurant: " +
              (err.response && err.response.data ? err.response.data.message : err.message)
          );
        });
    },
    deleteRestaurant(restaurant) {
      let vm = this;
      axios
        .delete("api/restaurant/" + restaurant.definition.id)
        .then(() => {
          store.commit("SET_INFO", "Restaurant " + restaurant.definition.name + "  deleted.");
          vm.reloadRestaurants();
        })
        .catch(err => {
          store.commit(
            "SET_INFO",
            "Error while deleting restaurant: " +
              (err.response && err.response.data ? err.response.data.message : err.message)
          );
        });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "@/scss/_variables.scss";

#restaurants {
  .wrapper {
    margin-top: 100px;
    padding: 70px 10px 10px 10px;
    height: calc(100vh - 100px);
    overflow-y: auto;
  }

  &.dark {
    border-right: 1px solid #464646;
    .buttons {
      .button {
        border: 1px solid #636262;
        &.delete:hover {
          color: white;
          background-color: #ec4f4f !important;
          border: 1px solid #ec4f4f !important;
        }
      }
    }

    .create {
      .content {
        background-color: #2b2b2b;
        border-color: #464646;
        input {
          color: black;
          background-color: #cecece;
        }
      }
    }
    .restaurant {
      .main {
        background-color: #2b2b2b;
        border-color: #464646;
        .tag {
          color: #d8d8d8;
          background-color: #1e4058;
          border: 1px solid #1e4058;
        }
        .description {
          color: grey;
        }
      }
    }
  }

  &.light {
    border-right: 1px solid #cacaca;
    .buttons {
      .button {
        border: 1px solid #4ca9e0;
        color: #4ca9e0;

        &.cancel {
          border: 1px solid #636262;
          background-color: transparent;
          color: #242424;
        }
        &.delete:hover {
          color: white;
          background-color: #ec4f4f !important;
          border: 1px solid #ec4f4f !important;
        }
      }
    }

    .create {
      .content {
        background-color: #efefef;
        border-color: #cacaca;
        input {
          background-color: white !important;
          &:focus {
            border-color: #c5c5c5;
          }
        }
      }
    }
    .restaurant {
      .main {
        background-color: #efefef;
        border-color: #cacaca;
        .tag {
          color: #39739d;
          background-color: #d0e0ec;
          border: 1px solid #d0e0ec;
        }
        .name {
          color: #1f689e;
        }
        .description {
          color: grey;
        }
      }
    }
  }

  .buttons {
    height: 22px;
    margin-bottom: 5px;
    .button {
      cursor: pointer;
      font-size: 0.8rem;
      font-weight: bold;
      padding: 2px 4px;
      margin-right: 4px;
      border-radius: 4px;

      &:hover {
        color: white !important;
        border-color: $primaryColor !important;
        background-color: $primaryColor !important;
      }
    }
  }

  .create {
    .create-button {
      z-index: 1;
    }
    .content {
      margin-top: -35px;
      font-size: 0.8rem;
      padding: 20px;
      border-radius: 6px;
      border: 1px solid;
    }
  }

  .restaurant {
    margin-top: 20px;
    padding: 5px 0;
    // border: 1px solid;
    border-radius: 6px;
    // box-shadow: 0 5px 5px 0 rgba(12, 13, 14, 0.15);

    .obfuscable {
      opacity: 0;
    }
    &:hover {
      .obfuscable {
        opacity: 1;
      }
    }

    .main {
      border-top-right-radius: 6px;
      border-top-left-radius: 6px;
      border-left: 1px solid;
      border-top: 1px solid;
      border-right: 1px solid;
      padding: 5px 5px 5px 10px;

      .tags {
        text-align: left;
        font-size: 0.7rem;
        font-weight: bold;
        padding: 5px 5px 0 0;

        .tag {
          padding: 1px 4px;
          border-radius: 2px;
          margin-right: 4px;
        }

        input {
          height: 33px;
          font-size: 0.8rem;
        }
      }
      .name {
        text-align: left;
        font-weight: bold;
        font-size: 1.2rem;
        input {
          width: 350px;
        }
      }
      .description {
        &.value {
          padding: 6px 0 0 4px;
        }

        &:not(.value) {
          padding: 0 6px;
        }

        input {
          height: 33px;
        }
        font-size: 0.8rem;
        text-align: left;
      }
    }
  }
}
</style>
