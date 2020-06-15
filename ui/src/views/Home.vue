<template>
  <div class="home w-full flex" :class="[hue]">
    <div class="center-panel w-full">
      <div class="no-info flex justify-end" v-if="!selectedSite">
        <div class="main w-full">
          <div class="title flex justify-between">
            <div>
              <span class="onsite">#OnSite</span>
              <span class="management">Management</span>
            </div>
            <div class="arrow-right"></div>
          </div>
          <div class="content text-left">
            <font-awesome-icon icon="lightbulb"></font-awesome-icon>
            <span>First, select your working site to start your registration.</span>
          </div>
        </div>
      </div>
      <div class="months" v-if="startsOfMonth.length > 0">
        <div v-for="startOfMonth of startsOfMonth" :key="startOfMonth">
          <CalendarEditor :startOfMonth="startOfMonth"></CalendarEditor>
        </div>
      </div>
      <div class="booking-panel-wrapper">
        <BookingPanel></BookingPanel>
      </div>
    </div>
    <div class="right-panel select-none overflow-y-auto">
      <div class="title">
        <font-awesome-icon icon="building"></font-awesome-icon>&nbsp;My site
      </div>
      <v-select
        :value="getObj(siteDefinitions, selectedSite)"
        :options="siteDefinitions"
        label="name"
        @input="setSelectedSite"
      >
        <template #option="option">
          <h3 style="margin: 0">{{ option.name }}</h3>
          <div class="description">{{ option.description }}</div>
          <div class="tag">{{ option.tags }}</div>
        </template>
      </v-select>

      <template v-if="getSettingProperty('manageRestaurants')">
        <div class="title">
          <font-awesome-icon icon="utensils"></font-awesome-icon>&nbsp;My restaurant
        </div>
        <v-select
          :value="getObj(restaurantDefinitions, selectedRestaurant)"
          :options="restaurantDefinitions"
          label="name"
          @input="setSelectedRestaurant"
        >
          <template #option="option">
            <h3 style="margin: 0">{{ option.name }}</h3>
            <div class="description">{{ option.description }}</div>
            <div class="tag">{{ option.tags }}</div>
          </template>
        </v-select>
      </template>

      <template v-if="getSettingProperty('manageParkings')">
        <div class="title">
          <font-awesome-icon icon="parking"></font-awesome-icon>&nbsp;My parking
        </div>
        <v-select
          :value="getObj(parkingDefinitions, selectedParking)"
          :options="parkingDefinitions"
          label="name"
          @input="setSelectedParking"
        >
          <template #option="option">
            <h3 style="margin: 0">{{ option.name }}</h3>
            <div class="description">{{ option.description }}</div>
            <div class="tag">{{ option.tags }}</div>
          </template>
        </v-select>
      </template>

    </div>
  </div>
</template>

<script>
import CalendarEditor from "@/components/CalendarEditor.vue";
import BookingPanel from "@/components/BookingPanel.vue";

import { mapGetters } from "vuex";
import store from "../store/index";
import moment from "moment";

export default {
  name: "Home",
  store,
  components: {
    CalendarEditor,
    BookingPanel
  },
  computed: {
    ...mapGetters([
      "hue",
      "settings",
      "siteDefinitions",
      "restaurantDefinitions",
      "parkingDefinitions",
      "selectedSite",
      "selectedRestaurant",
      "selectedParking",
      "siteUsages",
      "restaurantUsages",
      "parkingUsages"
    ]),
    startsOfMonth() {
      let startsOfMonth = [];
      for (let siteUsage of this.siteUsages) {
        let startOfMonth = moment(siteUsage.date)
          .startOf("month")
          .format("YYYY-MM-DD");
        if (startsOfMonth.indexOf(startOfMonth) < 0) {
          startsOfMonth.push(startOfMonth);
        }
      }
      startsOfMonth.sort();
      return startsOfMonth;
    }
  },
  watch: {
    siteDefinitions(val) {
      if (val && this.selectedSite) {
        this.checkDefaults(this.getObj(val, this.selectedSite));
      }
    },
    selectedSite(val) {
      if (val && this.siteDefinitions) {
        this.checkDefaults(this.getObj(this.siteDefinitions, val));
      }
    }
  },
  data() {
    return {
      listSites: null
    };
  },
  methods: {
    getObj(defs, id) {
      if (defs) {
        for (let d of defs) {
          if (d.id === id) {
            return d;
          }
        }
      }
      return null;
    },
    getSettingProperty(prop) {
      if (this.settings) {
        for (let setting of this.settings) {
          if (setting.key == prop) {
            return setting.value == "true";
          }
        }
      }
      return false;
    },
    checkDefaults(site) {
      let vm = this;
      if (vm.selectedRestaurant == null && site.defaultRestaurant) {
        vm.setSelectedRestaurant(site.defaultRestaurant);
      }
      if (vm.selectedParking == null && site.defaultParking) {
        vm.setSelectedParking(site.defaultParking);
      }
    },
    setSelectedSite(val) {
      if (val) {
        store.dispatch("changeSelectedSite", val.id);
      }
    },
    setSelectedRestaurant(val) {
      if (val) {
        store.dispatch("changeSelectedRestaurant", val.id);
      }
    },
    setSelectedParking(val) {
      if (val) {
        store.dispatch("changeSelectedParking", val.id);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.home {
  &.dark {
    .center-panel {
      background: linear-gradient(60deg, #1d1d1d 0%, #191919 35%, #111b1f 100%);
      .no-info {
        .main {
          background-color: #313131;
          .post {
            color: #00b6ed;
          }
          .arrow-right {
            border-left: 20px solid #313131;
          }
        }
      }
    }
    .right-panel {
      border-color: #464646;
      .title {
        color: #5eb1cc;
      }
      .v-select {
        color: #464646;
      }
    }

    .item.selected {
      background-color: #2d2d2d;
    }
  }

  &.light {
    .center-panel {
      .no-info {
        .main {
          background-color: #bad8e6;
          .onsite {
            color: #00b6ed;
          }
          .arrow-right {
            border-left: 20px solid #bad8e6;
          }
        }
      }
    }
    .right-panel {
      border-color: #cacaca;
      .title {
        color: #11a3d2;
      }
      .v-select {
        color: #e0e5e6;
      }
    }

    .item.selected {
      background-color: #f1f1f1;
    }
  }

  div.description {
    font-size: 0.6rem;
    color: #0c6498;
  }

  div.tag {
    font-size: 0.7rem;
    font-weight: bold;
    color: #0c6498;
  }

  height: 100vh;

  .center-panel {
    // overflow-y: overlay;;
    .no-info {
      margin-top: 110px;
      margin-right: 10px;
      margin-left: 20px;
      .main {
        padding: 30px 0 30px 30px;
        z-index: 1;
        border-radius: 8px;
        .title {
          text-align: left;
          font-size: 1.5rem;
          letter-spacing: -2px;
          .onsite {
            font-weight: bold;
          }
        }
        .content {
          font-size: 0.9rem;
          svg {
            font-size: 1.2rem;
            margin-right: 10px;
            color: #d8a718;
          }
        }
        .arrow-right {
          margin-left: 30px;
          margin-right: -20px;
          height: 40px;
          border-top: 20px solid transparent;
          border-bottom: 20px solid transparent;
        }
      }
    }

    .months {
      height: 100vh;
      padding: 150px 0px 300px 0px;
      overflow-y: auto;
    }
  }

  .right-panel {
    width: 350px;
    padding-top: 150px;
    border-left: 1px solid;
    .title {
      text-align: left;
      padding: 5px 10px 0 15px;
      font-size: 0.8rem;
      svg {
        font-size: 3rem;
        margin-left: -25px;
        transform: rotate(25deg);
        opacity: 0.27;
      }
    }
    .v-select {
      margin-left: 15px;
      margin-bottom: 35px;
      font-size: 0.9rem;
      ::v-deep .vs__dropdown-toggle {
        border-radius: 0;
        border: none;
        border-bottom: 1px solid;
      }
    }
  }
}
</style>
