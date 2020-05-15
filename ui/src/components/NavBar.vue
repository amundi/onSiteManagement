<template>
  <div id="navBar" :class="[hue]">
    <div class="main-container container flex justify-between mx-auto px-1">
      <div class="logo flex select-none" @click="sendToHome()">
        <img src="../assets/logo.png" />
        <div class="title">
          <span class="post">#OnSite</span>
          <span class="covid">Management</span>
        </div>
      </div>
      <div class="actionBar flex">
        <div class="user select-none cursor-pointer" v-if="user">
          <div class="menu flex justify-end" @click="menuExpanded = !menuExpanded">
            <div class="content flex">
              <font-awesome-icon class="user-logo" icon="user-circle"></font-awesome-icon>
              <div class="user-name">
                <div class="name">{{ user.firstName + " " + user.lastName }}</div>
                <div v-if="overridenUser != ''" class="overriden flex justify-end">
                  <span>{{ overridenUser }}</span>
                </div>
              </div>
              <template v-if="user.role == 'admin'">
                <font-awesome-icon
                  v-if="!menuExpanded"
                  class="menu-caret"
                  icon="caret-up"
                ></font-awesome-icon>
                <font-awesome-icon
                  v-if="menuExpanded"
                  class="menu-caret"
                  icon="caret-down"
                ></font-awesome-icon>
              </template>
            </div>
          </div>
          <div v-if="menuExpanded && user != null && user.role == 'admin'" class="menu-content">
            <div class="text-left">
              <label class="text-xs" for="override-input">Override</label>
              <input
                v-model="overridenUserInput"
                class="appearance-none block w-full border rounded py-1 px-2 leading-tight focus:outline-none focus:border-gray-500"
                id="override-input"
                type="text"
                placeholder="..."
              />
            </div>
            <div class="buttons text-right">
              <span class="button stop" @click="stopOverride()">stop</span>
              <span class="button apply" @click="applyOverride(overridenUserInput)">apply</span>
            </div>
          </div>
        </div>
        <div class="hue">
          <toggle-button
            class="hueButton"
            :height="14"
            :width="28"
            :color="{
              checked: '#AAAAAA',
              unchecked: '#3c4f54',
              disabled: '#aaaaaa'
            }"
            @change="changeHue"
          />
        </div>
      </div>
    </div>
    <div class="info" :class="infoType" v-if="info != null" @mouseover="removeInfo()">
      <font-awesome-icon
        class="user-logo"
        icon="times-circle"
        @click="removeInfo()"
      ></font-awesome-icon>
      <span>{{ info }}</span>
    </div>
  </div>
</template>

<script>
import store from "../store/index";
import { mapGetters } from "vuex";
import bus from "@/bus";

export default {
  name: "NavBar",
  store,
  computed: {
    ...mapGetters(["hue", "user", "overridenUser", "info"])
  },
  watch: {
    info(val) {
      if (val) {
        if (val.lastIndexOf("Warning", 0) === 0) {
          this.infoType = "warn";
        } else if (val.lastIndexOf("Error", 0) === 0) {
          this.infoType = "error";
        } else {
          this.infoType = "success";
        }
      }
    }
  },
  data() {
    return {
      menuExpanded: false,
      overridenUserInput: null,
      infoType: "warn"
    };
  },
  mounted() {},
  methods: {
    sendToHome() {
      window.location.href = "/";
    },
    changeHue() {
      store.commit("CHANGE_HUE");
    },
    removeInfo() {
      store.commit("SET_INFO", null);
    },
    stopOverride() {
      bus.$emit("clearPendingActions");
      store.dispatch("changeOverridenUser", null);
      this.menuExpanded = false;
    },
    applyOverride(val) {
      bus.$emit("clearPendingActions");
      store.dispatch("changeOverridenUser", val);
      this.menuExpanded = false;
      if ("/" != this.$route.path) {
        this.$router.push({ path: "/" });
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "@/scss/_variables.scss";

#navBar {
  &.dark {
    background-color: #2b2b2b;
    border-bottom-color: #3c3c3c;
    .actionBar {
      .user {
        background-color: #2b2b2b;
        .menu {
          &:hover {
            .content {
              background-color: #223c44;
            }
          }
        }
        .menu-content {
          border-left: 1px solid #3c3c3c;
          border-bottom: 1px solid #3c3c3c;
          border-right: 1px solid #3c3c3c;
          input {
            color: black;
          }
          .buttons {
            .button {
              &.apply {
                opacity: 1;
                color: white;
                background-color: #3a6b88;
                border-color: #3a6b88;
              }
            }
          }
        }
      }
    }
  }

  &.light {
    background-color: #fafafb;
    border-bottom-color: #cccccc;
    .actionBar {
      .user {
        background-color: #fafafb;
        .menu {
          color: #3c4f54;
          &:hover {
            .content {
              background-color: #d6d6d6;
            }
          }
        }
        .menu-content {
          border-left: 1px solid #cccccc;
          border-bottom: 1px solid #cccccc;
          border-right: 1px solid #cccccc;
          .buttons {
            .button {
              &.apply {
                opacity: 1;
                color: white;
                background-color: #62afd0;
                border-color: #62afd0;
              }
            }
          }
        }
      }
    }
  }

  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  height: 48px;

  border-top: 3px solid $primaryColor;
  border-bottom: 1px solid;
  box-shadow: 0 5px 5px 0 rgba(12, 13, 14, 0.15);

  .main-container {
    .logo {
      cursor: pointer;
      padding-top: 6px;
      height: 36px;
      img {
        display: block;
        width: auto;
        height: 100%;
      }
      .title {
        padding: 4px 0 0 4px;
        font-size: 1.1rem;
        letter-spacing: -1px;
        .post {
          font-weight: bold;
          padding-right: 2px;
        }
        .covid {
          font-weight: lighter;
          color: #868686;
        }
      }
    }

    .actionBar {
      .hueButton {
        padding: 16px 0 0 4px;
      }
      .user {
        .menu {
          .content {
            margin: 6px 0 0 0;
            padding: 2px 8px;
            height: 34px;
            border-radius: 6px;
          }

          .user-logo {
            font-size: 1.5rem;
            margin-top: 3px;
          }
          .user-name {
            padding: 4px 4px;
            font-size: 1rem;
            text-align: end;
            .overriden {
              span {
                border-radius: 6px;
                color: white;
                font-weight: 900;
                background-color: red;
                padding: 0 4px;
              }

              text-align: center;
              font-size: 0.8rem;
              margin-top: -4px;
            }
          }
          .menu-caret {
            margin-top: 8px;
          }
        }

        .menu-content {
          margin-top: 4px;
          padding: 10px;
          border-bottom-left-radius: 2px;
          border-bottom-right-radius: 2px;
          box-shadow: 0 5px 5px 0 rgba(12, 13, 14, 0.15);

          .buttons {
            padding-top: 5px;
            .button {
              font-size: 0.7rem;
              font-weight: bold;
              padding: 1px 4px;
              border-radius: 4px;
              margin: 0 0 0 5px;
              border: 1px solid;
              opacity: 0.6;

              &:hover {
                opacity: 1;
                background-color: $primaryColor;
                border-color: $primaryColor;
                color: white;
              }
            }
          }
        }
      }
    }
  }

  .info {
    &.success {
      background-color: #3bad56;
    }
    &.warn,
    &.error {
      background-color: #c57a1c;
    }

    opacity: 0.8;
    color: white;
    text-align: left;
    padding: 0 10px;
    position: fixed;
    left: 0;
    right: 0;
    top: 48px;
    z-index: -1;
    box-shadow: 0 5px 5px 0 rgba(12, 13, 14, 0.15);
    span {
      padding: 5px;
    }
  }
}
</style>
