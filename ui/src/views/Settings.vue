<template>
  <div id="settings" class="w-2/3 center-panel" :class="[hue]">
    <div class="wrapper w-full">
      <div class="section features w-full">
        <div class="title text-left pl-3 flex justify-between">
          <div>Management features</div>
          <div class="actions">
            <div class="button save" @click="saveSettings()">
              <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;save
            </div>
          </div>
        </div>
        <div class="content">
          <div class="item flex even">
            <toggle-button
              class="hueButton"
              :height="14"
              :width="28"
              :color="{
                checked: '#1a93b7',
                unchecked: '#cccccc',
                disabled: '#aaaaaa'
              }"
              v-model="manageOnSites"
              :sync="true"
            />
            <div class="comment">On site</div>
          </div>
          <div class="item flex odd">
            <toggle-button
              class="hueButton"
              :height="14"
              :width="28"
              :color="{
                checked: '#1a93b7',
                unchecked: '#cccccc',
                disabled: '#aaaaaa'
              }"
              v-model="manageArrivals"
              :sync="true"
            />
            <div class="comment">Arrivals</div>
          </div>
          <div class="item flex even">
            <toggle-button
              class="hueButton"
              :height="14"
              :width="28"
              :color="{
                checked: '#1a93b7',
                unchecked: '#cccccc',
                disabled: '#aaaaaa'
              }"
              v-model="manageDepartures"
              :sync="true"
            />
            <div class="comment">Departures</div>
          </div>
          <div class="item flex odd">
            <toggle-button
              class="hueButton"
              :height="14"
              :width="28"
              :color="{
                checked: '#1a93b7',
                unchecked: '#cccccc',
                disabled: '#aaaaaa'
              }"
              v-model="manageParkings"
              :sync="true"
            />
            <div class="comment">Parkings</div>
          </div>
          <div class="item flex even">
            <toggle-button
              class="hueButton"
              :height="14"
              :width="28"
              :color="{
                checked: '#1a93b7',
                unchecked: '#cccccc',
                disabled: '#aaaaaa'
              }"
              v-model="manageRestaurants"
              :sync="true"
            />
            <div class="comment">Restaurants</div>
          </div>
        </div>
      </div>
      <div class="section users w-full">
        <div class="title text-left pl-3 flex justify-between">
          <div>Users management</div>
          <div class="actions flex">
            <div class="search">
              <input
                class="appareance-none pl-2 rounded border focus:outline-none"
                placeholder="filter..."
                v-model="filter"
              />
            </div>
            <span class="button ml-1" @click="reloadUsers()">
              <font-awesome-icon class="menu" icon="sync-alt"></font-awesome-icon>&nbsp;reload
            </span>
            <div class="button save" @click="saveUsers()">
              <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;save
            </div>
          </div>
        </div>
        <div class="content">
          <div v-for="(users, role) of roles" :key="role">
            <div class="role-title flex justify-between">
              <div class="value">{{ role }}</div>
              <div class="actions flex">
                <template v-if="editedRole != role">
                  <div class="button save" @click="editUsers(role)">
                    <font-awesome-icon icon="pen"></font-awesome-icon>&nbsp;edit
                  </div>
                </template>
                <template v-if="editedRole == role">
                  <div class="button save" @click="cancelUsersEdition()">
                    <font-awesome-icon icon="times"></font-awesome-icon>&nbsp;cancel
                  </div>
                  <div class="button save" @click="saveUsersEdition()">
                    <font-awesome-icon icon="check"></font-awesome-icon>&nbsp;ok
                  </div>
                </template>
              </div>
            </div>
            <div v-if="editedRole == role" class="user-editor-wrapper">
              <textarea
                class="w-full user-editor"
                v-model="editedUsers"
                placeholder="ajoutez plusieurs lignes"
              ></textarea>
            </div>
            <div class="users flex flex-wrap">
              <template v-for="user of users">
                <div
                  v-if="isFiltered(user, filter)"
                  class="user"
                  :class="[user.status]"
                  :key="user.name"
                >{{ user.name }}</div>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import store from "../store/index";

export default {
  name: "Settings",
  store,
  computed: {
    ...mapGetters(["hue", "settings", "userRoles"])
  },
  data() {
    return {
      manageOnSites: null,
      manageArrivals: null,
      manageDepartures: null,
      manageParkings: null,
      manageRestaurants: null,
      roles: null,
      filter: null,
      editedRole: null,
      editedUsers: null
    };
  },
  watch: {
    settings(val) {
      this.updateFromSettings(val);
    },
    userRoles(val) {
      this.updateFromUserRoles(val);
      this.$forceUpdate();
    }
  },
  mounted() {
    this.updateFromUserRoles(this.userRoles);
    this.updateFromSettings(this.settings);
  },
  methods: {
    isFiltered(value, filter) {
      return filter == null || filter == "" || value.name.indexOf(filter) >= 0;
    },
    updateFromUserRoles(userRoles) {
      if (userRoles) {
        this.roles = { admin: [], user: [], reporter: [] };
        for (let user of userRoles) {
          if (!this.roles[user.role]) {
            this.roles[user.role] = [];
          }
          this.roles[user.role].push({ name: user.user, status: "ok" });
        }
        for (let role in this.roles) {
          this.roles[role].sort();
        }
      }
    },
    updateFromSettings(settings) {
      if (settings) {
        for (let setting of settings) {
          if (setting.key == "manageOnSites") {
            this.manageOnSites = setting.value == "true" ? true : false;
          } else if (setting.key == "manageArrivals") {
            this.manageArrivals = setting.value == "true" ? true : false;
          } else if (setting.key == "manageDepartures") {
            this.manageDepartures = setting.value == "true" ? true : false;
          } else if (setting.key == "manageParkings") {
            this.manageParkings = setting.value == "true" ? true : false;
          } else if (setting.key == "manageRestaurants") {
            this.manageRestaurants = setting.value == "true" ? true : false;
          }
        }
      }
    },
    saveSettings() {
      let vm = this;
      let s = [
        {
          key: "manageOnSites",
          value: vm.manageOnSites ? "true" : "false"
        },
        {
          key: "manageArrivals",
          value: vm.manageArrivals ? "true" : "false"
        },
        {
          key: "manageDepartures",
          value: vm.manageDepartures ? "true" : "false"
        },
        {
          key: "manageParkings",
          value: vm.manageParkings ? "true" : "false"
        },
        {
          key: "manageRestaurants",
          value: vm.manageRestaurants ? "true" : "false"
        }
      ];
      store.dispatch("saveSettings", s);
    },
    reloadUsers() {
      store.dispatch("loadUserRoles");
    },
    editUsers(role) {
      if (this.editedRole == role) {
        this.editedRole = null;
        this.editedUsers = null;
      } else {
        this.editedUsers = "";
        for (let user of this.roles[role]) {
          this.editedUsers += user.name + " ";
        }
        this.editedRole = role;
      }
    },
    cancelUsersEdition() {
      this.editedRole = null;
      this.editedUsers = null;
    },
    saveUsersEdition() {
      let currentUsers = [];
      if (this.editedUsers && this.editedRole) {
        for (let user of this.userRoles) {
          if (user.role == this.editedRole) {
            currentUsers.push({ name: user.user, status: "ok" });
          }
        }
        let savedUsers = [];
        if (this.editedUsers) {
          let usrs = this.editedUsers.split(/[^A-Za-z_]/);
          for (let usr of usrs) {
            if (usr.length > 0) {
              // User name sanitization
              let sanitizedUser = usr
                .toLowerCase()
                .normalize("NFD")
                .replace(/[\u0300-\u036f]/g, "");

              let found = false;
              // First, check that user does not already exist
              for (let savedUser of savedUsers) {
                if (savedUser.name == sanitizedUser) {
                  found = true;
                }
              }

              // If not found, check if exsting in previous version
              // and take it if so.
              if (!found) {
                for (let i = 0; i < currentUsers.length; i++) {
                  if (currentUsers[i].name == sanitizedUser) {
                    savedUsers.push(currentUsers[i]);
                    currentUsers.splice(i, 1);
                    found = true;
                    break;
                  }
                }
              }

              // If still not found, create new
              if (!found) {
                savedUsers.push({ name: sanitizedUser, status: "new" });
              }
            }
          }
          // Finally, mark previous existing as delete
          for (let currentUser of currentUsers) {
            currentUser.status = "delete";
            savedUsers.push(currentUser);
          }
        }
        this.roles[this.editedRole] = savedUsers;
      }

      this.editedRole = null;
      this.editedUsers = null;
    },
    saveUsers() {
      let userRoles = [];
      for (let role in this.roles) {
        for (let user of this.roles[role]) {
          if (user.status != "delete") {
            userRoles.push({ user: user.name, role: role });
          }
        }
      }
      store.dispatch("saveUserRoles", userRoles);
    }
  }
};
</script>

<style lang="scss" scoped>
@import "@/scss/_variables.scss";

#settings {
  &.light {
    border-color: #cacaca;

    .section {
      .title {
        border-bottom: 1px solid #cacaca;
      }
      .item.even {
        background-color: #c3f0fd91;
      }

      .button {
        border: 1px solid #4ca9e0;
        color: #4ca9e0;
        background-color: white;
      }

      .role-title {
        color: #1675a5;
        border-bottom: 1px solid #cacaca;
      }
    }

    .users {
      .search {
        input {
          border: 1px solid #4ca9e0;
        }
      }
      .user-editor {
        background-color: white;
      }
      .user {
        color: white;
        &.new {
          background-color: #73cc73;
        }
        &.ok {
          background-color: #5ca6ca;
        }
        &.delete {
          background-color: #f17979;
        }
      }
    }
  }

  &.dark {
    border-color: #464646;

    .section {
      .title {
        border-bottom: 1px solid #464646;
      }
    }
    .item.even {
      background-color: #252a2f;
    }
    .button {
      border: 1px solid #636262;
      background-color: #191919;
    }

    .users {
      .search {
        input {
          background-color: transparent !important;
          border: 1px solid #464646;
          color: white;
        }
      }
      .role-title {
        color: #5ca6ca;
        border-bottom: 1px solid #464646;
      }
      .user {
        &.new {
          background-color: #378a37;
          color: white;
        }
        &.ok {
          background-color: #1675a5;
          color: white;
        }
        &.delete {
          background-color: #c23c3c;
          color: white;
        }
      }
      .user-editor-wrapper {
        .user-editor {
          background-color: #252a2f;
        }
      }
    }
  }

  border-right: 1px solid;
  .section {
    .title {
      margin-bottom: 15px;
    }
    &.features {
      .content {
        .item {
          font-size: 0.7rem;
          font-weight: bold;
          padding: 10px 25px;
          .hueButton {
            padding-top: 1px;
            padding-right: 5px;
          }
        }
        margin-bottom: 50px;
      }
    }
    &.users {
      max-height: 500px;
      .role-title {
        margin: 30px 0 0 20px;
        font-size: 1rem;
        font-weight: 100;
      }
      .search {
        margin-top: -1px;
        input {
          font-size: 0.8rem;
          height: 25px;
        }
      }
      .users {
        margin: 10px 0 0 20px;
        .user {
          border-radius: 4px;
          margin-right: 4px;
          margin-bottom: 4px;
          padding: 1px 4px;
          font-size: 0.7rem;
          font-weight: bold;
        }
      }
      .user-editor-wrapper {
        margin: 10px 0 10px 0px;
        .user-editor {
          font-size: 0.8rem;
          padding: 5px;
          height: 300px;
        }
      }
    }
    .button {
      cursor: pointer;
      font-size: 0.8rem;
      font-weight: bold;
      padding: 2px 4px;
      margin-right: 4px;
      margin-bottom: 4px;
      border-radius: 4px;

      &:hover {
        color: white !important;
        border-color: $primaryColor !important;
        background-color: $primaryColor !important;
      }
    }
  }

  padding-top: 200px;
  overflow-y: auto;
}
</style>
