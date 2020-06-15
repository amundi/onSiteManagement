<template>
  <div id="sites" class="w-full center-panel" :class="[hue]">
    <div class="wrapper w-full p-4">
      <div class="create w-full select-none">
        <div class="buttons flex">
          <span
            class="button create-button ml-3"
            @click="
              createdSite =
                createdSite == null ? { name: '', description: '', tags: '#France #Paris' } : null
            "
          >
            <font-awesome-icon class="menu" icon="plus"></font-awesome-icon>&nbsp;create site
          </span>
          <span class="button ml-1" @click="reloadSites()">
            <font-awesome-icon class="menu" icon="sync-alt"></font-awesome-icon>&nbsp;reload
          </span>
        </div>
        <div class="content w-full" v-if="createdSite != null">
          <div class="flex items-center mb-2">
            <div class="w-1/3">
              <label class="block text-right mb-1 mb-0 pr-4" for="inline-name">Name</label>
            </div>
            <div class="w-2/3">
              <input
                class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                id="inline-name"
                type="text"
                v-model="createdSite.name"
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
                v-model="createdSite.description"
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
                v-model="createdSite.tags"
              />
            </div>
          </div>

          <div class="buttons select-none flex justify-end">
            <span class="button cancel" @click="createdSite = null">cancel</span>
            <span class="button" @click="createSite(createdSite)">save</span>
          </div>
        </div>
      </div>
      <div class="w-full">
        <div class="site" v-for="site of orderedSites" :key="site.id">
          <template v-if="site.definition">
            <div class="main select-none">
              <div class="flex justify-end pr-2">
                <div class="buttons flex">
                  <template v-if="site != editedSite">
                    <span class="button edit obfuscable" @click="editedSite = site">
                      <font-awesome-icon icon="pen"></font-awesome-icon>&nbsp;edit
                    </span>
                    <span class="button delete obfuscable" @click="deleteSite(site)">
                      <font-awesome-icon icon="times"></font-awesome-icon>&nbsp;delete
                    </span>
                  </template>
                  <span class="button save obfuscable" @click="saveSite(site)">
                    <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;save
                  </span>
                  <span class="menu pl-2 cursor-pointer">
                    <font-awesome-icon icon="bars"></font-awesome-icon>
                  </span>
                </div>
              </div>
              <div class="flex">
                <template v-if="site != editedSite">
                  <div class="name value whitespace-no-wrap">{{ site.definition.name }}</div>
                  <div class="description value w-full">{{ site.definition.description }}</div>
                </template>
                <template v-if="site == editedSite">
                  <div class="name">
                    <input
                      class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                      id="inline-name"
                      type="text"
                      v-model="site.definition.name"
                    />
                  </div>
                  <div class="description w-full">
                    <input
                      class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                      id="inline-desc"
                      type="text"
                      v-model="site.definition.description"
                    />
                  </div>
                </template>
              </div>

              <template v-if="site != editedSite && site.definition.tags">
                <div class="flex">
                  <div class="tags flex w-1/3">
                    <span
                      class="tag"
                      v-for="tag of site.definition.tags.split(' ')"
                      :key="tag"
                    >{{ tag }}</span>
                  </div>
                  <div class="restaurant flex w-1/3">
                    <font-awesome-icon icon="utensils"></font-awesome-icon>
                    <div
                      class="value"
                    >{{ site.definition.defaultRestaurant ? site.definition.defaultRestaurant.name : "none" }}</div>
                  </div>
                  <div class="parking flex w-1/3">
                    <font-awesome-icon icon="parking"></font-awesome-icon>
                    <div
                      class="value"
                    >{{ site.definition.defaultParking ? site.definition.defaultParking.name : "none" }}</div>
                  </div>
                </div>
              </template>
              <template v-if="site == editedSite">
                <div class="flex">
                  <div class="tags w-1/3">
                    <input
                      class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                      id="inline-path"
                      type="text"
                      v-model="site.definition.tags"
                    />
                  </div>
                  <div class="restaurant flex w-1/3">
                    <v-select class="white" :options="listRestaurants" v-model="selectedRestaurant"></v-select>
                  </div>
                  <div class="parking flex w-1/3">
                    <v-select class="white" :options="listParkings" v-model="selectedParking"></v-select>
                  </div>
                </div>
              </template>
            </div>
          </template>
          <div class="capacities w-full">
            <SiteCapacities :site="site" :saveSite="saveSite"></SiteCapacities>
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

import SiteCapacities from "@/components/SiteCapacities";

export default {
  store,
  components: { SiteCapacities },
  computed: {
    ...mapGetters(["hue", "sites", "restaurants", "parkings"]),
    orderedSites() {
      return this.sites.slice().sort((a, b) => {
        return a.definition.name.localeCompare(b.definition.name);
      });
    },
    listRestaurants() {
      let rs = [{ label: "none", code: null }];
      for (let r of this.restaurants) {
        rs.push({ label: r.definition.name, code: r.definition.id });
      }
      return rs;
    },
    listParkings() {
      let rs = [{ label: "none", code: null }];
      for (let r of this.parkings) {
        rs.push({ label: r.definition.name, code: r.definition.id });
      }
      return rs;
    }
  },
  watch: {
    selectedRestaurant(val) {
      if (val && this.editedSite) {
        this.editedSite.definition.defaultRestaurant =
          this.selectedRestaurant.code != null
            ? { id: this.selectedRestaurant.code }
            : null;
      }
    },
    selectedParking(val) {
      if (val && this.editedSite) {
        this.editedSite.definition.defaultParking =
          this.selectedParking.code != null
            ? { id: this.selectedParking.code }
            : null;
      }
    },
    editedSite(val) {
      if (val) {
        if (val.definition.defaultRestaurant) {
          this.selectedRestaurant = {
            label: val.definition.defaultRestaurant.name,
            code: val.definition.defaultRestaurant.id
          };
        }
        if (val.definition.defaultParking) {
          this.selectedParking = {
            label: val.definition.defaultParking.name,
            code: val.definition.defaultParking.id
          };
        }
      }
    }
  },
  data() {
    return {
      createdSite: null,
      editedSite: null,
      selectedRestaurant: null,
      selectedParking: null
    };
  },
  methods: {
    createSite(definition, capacities) {
      let vm = this;
      axios
        .post("api/site/", { definition: definition, capacities: capacities })
        .then(response => {
          if (
            response.data &&
            response.data.definition &&
            response.data.definition.name
          ) {
            store.dispatch(
              "setInfo",
              "Site " + response.data.definition.name + " successfully created"
            );
          } else {
            console.log(response.data);
            store.commit("SET_INFO", "Error while creating site");
          }
          store.dispatch("loadSites");
        })
        .catch(err => {
          store.dispatch(
            "setInfo",
            "Error while creating site: " +
              (err.response && err.response.data
                ? err.response.data.message
                : err.message)
          );
        });
      vm.createdSite = null;
    },
    reloadSites() {
      this.createdSite = null;
      this.editedSite = null;
      this.selectedRestaurant = null;
      store.dispatch("reloadAll");
    },
    saveSite(site) {
      let vm = this;
      axios
        .post("api/site/", site)
        .then(() => {
          store.dispatch(
            "setInfo",
            "Site " + site.definition.name + "  saved."
          );
          vm.reloadSites();
        })
        .catch(err => {
          store.dispatch(
            "setInfo",
            "Error while saving site: " +
              (err.response && err.response.data
                ? err.response.data.message
                : err.message)
          );
        });
    },
    deleteSite(site) {
      let vm = this;
      axios
        .delete("api/site/" + site.definition.id)
        .then(() => {
          store.dispatch(
            "setInfo",
            "Site " + site.definition.name + "  deleted."
          );
          vm.reloadSites();
        })
        .catch(err => {
          store.dispatch(
            "setInfo",
            "Error while deleting site: " +
              (err.response && err.response.data
                ? err.response.data.message
                : err.message)
          );
        });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "@/scss/_variables.scss";

#sites {
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
        background-color: #191919;
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
        border: 1px solid #464646;
        input {
          color: black;
          background-color: #cecece;
        }
      }
    }
    .site {
      .main {
        background-color: #4848481c;
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
        background-color: white;
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
        border: 1px solid #cacaca;
        input {
          background-color: white !important;
          &:focus {
            border-color: #c5c5c5;
          }
        }
      }
    }
    .site {
      .main {
        background-color: #f0f0f1;
        border-color: #cacaca;
        .tag {
          color: #39739d;
          background-color: #d0e0ec;
          border: 1px solid #d0e0ec;
        }

        .restaurant, .parking {
          svg {
            color: #1f689e;
          }
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
    }
  }

  .site {
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
      .restaurant, .parking {
        svg {
          margin-top: 3px;
        }
        .value {
          padding-left: 6px;
        }
        .v-select {
          width: 100%;
          margin-top: 5px;
          margin-right: 5px;
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
