<template>
  <div id="parkings" class="w-full center-panel" :class="[hue]">
    <div class="wrapper w-full p-4">
      <div class="create w-full select-none">
        <div class="buttons flex">
          <span
            class="button create-button ml-3"
            @click="
              createdParking =
                createdParking == null
                  ? { name: '', description: '', tags: '#France #Paris' }
                  : null
            "
          >
            <font-awesome-icon class="menu" icon="plus"></font-awesome-icon>&nbsp;create parking
          </span>
          <span class="button ml-1" @click="reloadParkings()">
            <font-awesome-icon class="menu" icon="sync-alt"></font-awesome-icon>&nbsp;reload
          </span>
        </div>
        <div class="content w-full" v-if="createdParking != null">
          <div class="flex items-center mb-2">
            <div class="w-1/3">
              <label class="block text-right mb-1 mb-0 pr-4" for="inline-name">Name</label>
            </div>
            <div class="w-2/3">
              <input
                class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                id="inline-name"
                type="text"
                v-model="createdParking.name"
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
                v-model="createdParking.description"
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
                v-model="createdParking.tags"
              />
            </div>
          </div>

          <div class="buttons select-none flex justify-end">
            <span class="button cancel" @click="createdParking = null">cancel</span>
            <span class="button" @click="createParking(createdParking)">save</span>
          </div>
        </div>
      </div>
      <div class="w-full">
        <div class="parking" v-for="parking of orderedParkings" :key="parking.id">
          <template v-if="parking.definition">
            <div class="main select-none">
              <div class="flex justify-end pr-2">
                <div class="buttons flex">
                  <template v-if="parking != editedParking">
                    <span class="button edit obfuscable" @click="editedParking = parking">
                      <font-awesome-icon icon="pen"></font-awesome-icon>&nbsp;edit
                    </span>
                    <span class="button delete obfuscable" @click="deleteParking(parking)">
                      <font-awesome-icon icon="times"></font-awesome-icon>&nbsp;delete
                    </span>
                  </template>
                  <span class="button save obfuscable" @click="saveParking(parking)">
                    <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;save
                  </span>
                  <span
                    class="menu pl-2 cursor-pointer"
                    @click="
                      expandedParking = parking == expandedParking ? null : parking
                    "
                  >
                    <font-awesome-icon icon="bars"></font-awesome-icon>
                  </span>
                </div>
              </div>
              <div class="flex">
                <template v-if="parking != editedParking">
                  <div class="name value whitespace-no-wrap">{{ parking.definition.name }}</div>
                  <div class="description value w-full">
                    {{ parking.definition.description }}
                  </div>
                </template>
                <template v-if="parking == editedParking">
                  <div class="name">
                    <input
                      class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                      id="inline-name"
                      type="text"
                      v-model="parking.definition.name"
                    />
                  </div>
                  <div class="description w-full">
                    <input
                      class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                      id="inline-desc"
                      type="text"
                      v-model="parking.definition.description"
                    />
                  </div>
                </template>
              </div>

              <template v-if="parking != editedParking && parking.definition.tags">
                <div class="tags flex">
                  <span
                    class="tag"
                    v-for="tag of parking.definition.tags.split(' ')"
                    :key="tag"
                    >{{ tag }}</span
                  >
                </div>
              </template>
              <template v-if="parking == editedParking">
                <div class="tags w-full">
                  <input
                    class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                    id="inline-path"
                    type="text"
                    v-model="parking.definition.tags"
                  />
                </div>
              </template>
            </div>
          </template>
          <div class="capacities w-full">
            <ParkingCapacities
              :parking="parking"
              :saveParking="saveParking"
            ></ParkingCapacities>
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

import ParkingCapacities from "@/components/ParkingCapacities";

export default {
  store,
  components: { ParkingCapacities },
  computed: {
    ...mapGetters(["hue", "parkings"]),
    orderedParkings() {
      return this.parkings.slice().sort((a, b) => {
        return a.definition.name.localeCompare(b.definition.name);
      });
    }
  },
  data() {
    return {
      createdParking: null,
      expandedParking: null,
      editedParking: null
    };
  },
  methods: {
    createParking(definition, capacities) {
      let vm = this;
      axios
        .post("api/parking/", {
          definition: definition,
          capacities: capacities
        })
        .then(response => {
          if (response.data && response.data.definition && response.data.definition.name) {
            store.commit(
              "SET_INFO",
              "Parking " + response.data.definition.name + " successfully created"
            );
          } else {
            store.commit("SET_INFO", "Error while creating Parking");
          }
          store.dispatch("loadParkings");
        })
        .catch(err => {
          store.commit(
            "SET_INFO",
            "Error while creating Parking: " +
              (err.response && err.response.data ? err.response.data.message : err.message)
          );
        });
      vm.createdParking = null;
    },
    reloadParkings() {
      this.createdParking = null;
      this.editedParking = null;
      store.dispatch("reloadAll");
    },
    saveParking(parking) {
      let vm = this;
      axios
        .post("api/parking/", parking)
        .then(() => {
          store.dispatch("setInfo", "Parking " + parking.definition.name + "  saved.");
          vm.reloadParkings();
        })
        .catch(err => {
          store.commit(
            "SET_INFO",
            "Error while saving Parking: " +
              (err.response && err.response.data ? err.response.data.message : err.message)
          );
        });
    },
    deleteParking(parking) {
      let vm = this;
      axios
        .delete("api/parking/" + parking.definition.id)
        .then(() => {
          store.dispatch("setInfo", "Parking " + parking.definition.name + "  deleted.");
          vm.reloadParkings();
        })
        .catch(err => {
          store.commit(
            "SET_INFO",
            "Error while deleting Parking: " +
              (err.response && err.response.data ? err.response.data.message : err.message)
          );
        });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "@/scss/_variables.scss";

#parkings {
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
    .parking {
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
    .parking {
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

  .parking {
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
