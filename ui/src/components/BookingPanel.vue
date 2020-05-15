<template>
  <div id="booking" :class="[hue]">
    <template v-if="selectedSite && getDayCount() > 0">
      <div
        v-if="getSettingProperty('manageOnSites') 
              && !getSettingProperty('manageArrivals')
              && !getSettingProperty('manageDepartures')"
        class="onSites"
      >
        <div class="title">
          <font-awesome-icon icon="map-marker-alt"></font-awesome-icon>&nbsp;On Site
        </div>
        <div class="content">
          <template v-if="isAvailable('onSite')">
            <div class="item ok" :class="{ selected: onSite == true }" @click="onSite = !onSite">
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              present
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              present
            </div>
          </template>
        </div>
      </div>

      <div v-if="getSettingProperty('manageArrivals')" class="arrivals">
        <div class="title">
          <font-awesome-icon icon="plane-arrival"></font-awesome-icon>&nbsp;Arrival
        </div>
        <div class="content">
          <template v-if="isAvailable('h700')">
            <div
              class="item ok"
              :class="{ selected: arrival == 700 }"
              @click="arrival = arrival != 700 ? 700 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              before 7:30
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              before 7:30
            </div>
          </template>
          <template v-if="isAvailable('h730')">
            <div
              class="item ok"
              :class="{ selected: arrival == 730 }"
              @click="arrival = arrival != 730 ? 730 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              7:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 8:00
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              7:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 8:00
            </div>
          </template>
          <template v-if="isAvailable('h800')">
            <div
              class="item ok"
              :class="{ selected: arrival == 800 }"
              @click="arrival = arrival != 800 ? 800 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              8:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 8:30
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              8:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 8:30
            </div>
          </template>
          <template v-if="isAvailable('h830')">
            <div
              class="item ok"
              :class="{ selected: arrival == 830 }"
              @click="arrival = arrival != 830 ? 830 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              8:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 9:00
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              8:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 9:00
            </div>
          </template>
          <template v-if="isAvailable('h900')">
            <div
              class="item ok"
              :class="{ selected: arrival == 900 }"
              @click="arrival = arrival != 900 ? 900 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              9:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 9:30
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              9:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 9:30
            </div>
          </template>
          <template v-if="isAvailable('h930')">
            <div
              class="item ok"
              :class="{ selected: arrival == 930 }"
              @click="arrival = arrival != 930 ? 930 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              9:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 10:00
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              9:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 10:00
            </div>
          </template>
          <template v-if="isAvailable('h1000')">
            <div
              class="item ok last"
              :class="{ selected: arrival == 1000 }"
              @click="arrival = (arrival = 1000) ? 1000 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              10:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 10:30
            </div>
          </template>
          <template v-else>
            <div class="item ko last">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              10:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 10:30
            </div>
          </template>
          <template v-if="isAvailable('h1030')">
            <div
              class="item ok last"
              :class="{ selected: arrival == 1030 }"
              @click="arrival = (arrival = 1030) ? 1030 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              after 10:30
            </div>
          </template>
          <template v-else>
            <div class="item ko last">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              after 10:30
            </div>
          </template>
        </div>
      </div>
      <div v-if="getSettingProperty('manageRestaurants')" class="restaurants">
        <div class="title">
          <font-awesome-icon icon="drumstick-bite"></font-awesome-icon>&nbsp;Lunch
        </div>
        <div class="content">
          <template v-if="isAvailable('h1130')">
            <div
              class="item ok"
              :class="{ selected: lunch == 1130 }"
              @click="lunch = lunch != 1130 ? 1130 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              11:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 12:00
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              11:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 12:00
            </div>
          </template>
          <template v-if="isAvailable('h1200')">
            <div
              class="item ok"
              :class="{ selected: lunch == 1200 }"
              @click="lunch = lunch != 1200 ? 1200 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              12:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 12:30
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              12:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 12:30
            </div>
          </template>
          <template v-if="isAvailable('h1230')">
            <div
              class="item ok"
              :class="{ selected: lunch == 1230 }"
              @click="lunch = lunch != 1230 ? 1230 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              12:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 13:00
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              12:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 13:00
            </div>
          </template>
          <template v-if="isAvailable('h1300')">
            <div
              class="item ok"
              :class="{ selected: lunch == 1300 }"
              @click="lunch = lunch != 1300 ? 1300 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              13:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 13:30
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              13:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 13:30
            </div>
          </template>
          <template v-if="isAvailable('h1330')">
            <div
              class="item ok last"
              :class="{ selected: lunch == 1330 }"
              @click="lunch = lunch != 1330 ? 1330 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              13:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 14:00
            </div>
          </template>
          <template v-else>
            <div class="item ko last">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              13:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 14:00
            </div>
          </template>
        </div>
      </div>
      <div v-if="getSettingProperty('manageDepartures')" class="departures">
        <div class="title">
          <font-awesome-icon icon="plane-departure"></font-awesome-icon>&nbsp;Departure
        </div>
        <div class="content">
          <template v-if="isAvailable('h1700')">
            <div
              class="item ok"
              :class="{ selected: departure == 1700 }"
              @click="departure = departure != 1700 ? 1700 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              17:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 17:30
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              17:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 17:30
            </div>
          </template>
          <template v-if="isAvailable('h1730')">
            <div
              class="item ok"
              :class="{ selected: departure == 1730 }"
              @click="departure = departure != 1730 ? 1730 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              17:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 18:00
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              17:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 18:00
            </div>
          </template>
          <template v-if="isAvailable('h1800')">
            <div
              class="item ok"
              :class="{ selected: departure == 1800 }"
              @click="departure = departure != 1800 ? 1800 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              18:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 18:30
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              18:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 18:30
            </div>
          </template>
          <template v-if="isAvailable('h1830')">
            <div
              class="item ok"
              :class="{ selected: departure == 1830 }"
              @click="departure = departure != 1830 ? 1830 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              18:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 19:00
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              18:30&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 19:00
            </div>
          </template>
          <template v-if="isAvailable('h1900')">
            <div
              class="item ok last"
              :class="{ selected: departure == 1900 }"
              @click="departure = departure != 1900 ? 1900 : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              19:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 19:30
            </div>
          </template>
          <template v-else>
            <div class="item ko last">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              19:00&nbsp;
              <font-awesome-icon icon="arrow-right"></font-awesome-icon>&nbsp; 19:30
            </div>
          </template>
        </div>
      </div>
      <div v-if="getSettingProperty('manageParkings')" class="parkings">
        <div class="title">
          <font-awesome-icon icon="parking"></font-awesome-icon>&nbsp;Parking
        </div>
        <div class="content">
          <template v-if="isAvailable('car')">
            <div
              class="item ok"
              :class="{ selected: parking == 'car' }"
              @click="parking = parking != 'car' ? 'car' : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              <font-awesome-icon icon="car"></font-awesome-icon>&nbsp;car
            </div>
          </template>
          <template v-else>
            <div class="item ko">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              <font-awesome-icon icon="car"></font-awesome-icon>&nbsp;car
            </div>
          </template>
          <template v-if="isAvailable('moto')">
            <div
              class="item ok last"
              :class="{ selected: parking == 'moto' }"
              @click="parking = parking != 'moto' ? 'moto' : null"
            >
              <font-awesome-icon class="status" icon="check-circle"></font-awesome-icon>&nbsp;
              <font-awesome-icon icon="motorcycle"></font-awesome-icon>&nbsp;motorcycle
            </div>
          </template>
          <template v-else>
            <div class="item ko last">
              <font-awesome-icon class="status" icon="times-circle"></font-awesome-icon>&nbsp;
              <font-awesome-icon icon="motorcycle"></font-awesome-icon>&nbsp;motorcycle
            </div>
          </template>
        </div>
      </div>
    </template>
    <div class="actions flex justify-end" v-if="getDayCount() > 0">
      <div class="button delete" @click="deleteBookings()">remove</div>
      <div class="button" @click="saveBookings()">save</div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import store from "../store/index";
import moment from "moment";
import bus from "@/bus";

export default {
  store,
  computed: {
    ...mapGetters(["hue", "currentUser", "selectedSite", "settings"])
  },
  watch: {
    selectedSite() {
      this.days = {};
    }
  },
  data() {
    return {
      days: {},
      arrivalValues: ['h700', 'h730', 'h800', 'h830', 'h900', 'h930', 'h1000', 'h1030'],
      arrival: null,
      lunch: null,
      departure: null,
      parking: null,
      onSite: null
    };
  },
  mounted() {
    let vm = this;
    bus.$on("selectedDays", function(days) {
      vm.days[days.startOfMonth] = days.selectedDays;
      vm.arrival = null;
      vm.lunch = null;
      vm.departure = null;
      vm.parking = null;
      vm.onSite = false;
      vm.$forceUpdate();
    });
    bus.$on("clearPendingActions", function() {
      vm.days = {};
      vm.$forceUpdate();
      bus.$emit("clearDaySelections");
    });
  },
  methods: {
    getDayCount() {
      let i = 0;
      for (let startOfMonth in this.days) {
        i += this.days[startOfMonth].length;
      }
      return i;
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
    isAvailable(prop) {
      if (this.getDayCount() == 0) {
        return false;
      }
      for (let startOfMonth in this.days) {
        for (let day of this.days[startOfMonth]) {
          if (day.usage[prop] - day.usage[prop + "_u"] <= 0) {
            return false;
          }
        }
      }
      if(this.arrivalValues.indexOf(prop) >= 0 && this.getSettingProperty('manageOnSites')) {
        return this.isAvailable('onSite');
      }
      return true;
    },
    saveBookings() {
      let vm = this;
      if (vm.getSettingProperty("manageArrivals") && !vm.arrival) {
        store.commit("SET_INFO", "Error: missing arrival info");
      } else if (vm.getSettingProperty("manageDepartures") && !vm.departure) {
        store.commit("SET_INFO", "Error: missing departure info");
      } else {
        let bookings = [];
        let startDate = moment();
        for (let startOfMonth in this.days) {
          let sm = moment(startOfMonth);
          if (sm.isBefore(startDate)) {
            startDate = sm;
          }
          for (let day of this.days[startOfMonth]) {
            bookings.push({
              user: vm.currentUser,
              site: { id: vm.selectedSite.id },
              date: day.usage.date,
              arrival: vm.arrival,
              moto: vm.parking == "moto",
              car: vm.parking == "car",
              restaurant: vm.lunch,
              departure: vm.departure,
              onSite: vm.onSite || !!vm.arrival
            });
          }
        }

        store.dispatch("saveCurrentUserUsages", {
          user: vm.currentUser,
          site: vm.selectedSite.id,
          startDate: startDate.format("YYYY-MM-DD"),
          workingDays: bookings
        });
        vm.days = {};
        bus.$emit("clearDaySelections");
      }
    },
    deleteBookings() {
      let vm = this;
      let bookings = [];
      let startDate = moment();
      for (let startOfMonth in this.days) {
        let sm = moment(startOfMonth);
        if (sm.isBefore(startDate)) {
          startDate = sm;
        }
        for (let day of this.days[startOfMonth]) {
          bookings.push({
            user: vm.currentUser,
            site: { id: vm.selectedSite.id },
            date: day.usage.date
          });
        }
      }

      store.dispatch("deleteCurrentUserUsages", {
        user: vm.currentUser,
        site: vm.selectedSite.id,
        startDate: startDate.format("YYYY-MM-DD"),
        workingDays: bookings
      });
      vm.days = {};
      bus.$emit("clearDaySelections");
    }
  }
};
</script>

<style lang="scss" scoped>
#booking {
  &.light {
    .no-select {
      color: #464646;
      border-color: #cacaca;
    }

    .onSites,
    .arrivals,
    .restaurants,
    .departures,
    .parkings {
      border-color: #cacaca;
      .title {
        background-color: #f1f1f1;
        color: #5f5f5f;
      }
      .content {
        .item {
          border-color: #ececec;
          color: #689427;
          &.ko {
            color: #d27777;
          }
          &.ok {
            .status {
              color: #689427;
            }
            &.selected {
              color: white;
              background-color: #7da73e;
              .status {
                color: white;
              }
            }
            &:hover {
              background-color: #2980a9;
            }
          }
        }
      }
    }

    .actions {
      .button {
        color: white;
        background-color: #57add6;
        &.delete {
          border: 1px solid #fd6d6d;
          color: #fd6d6d;
          background-color: transparent;
        }
      }
    }
  }

  &.dark {
    .no-select {
      color: #cacaca;
      border-color: #464646;
    }

    .onSites,
    .arrivals,
    .restaurants,
    .departures,
    .parkings {
      border-color: #464646;
      .title {
        background-color: #2b2b2b;
      }
      .content {
        .item {
          border-color: #2b2b2b;
          color: #9e9e9e;
          &.ko {
            color: #653535;
          }
          &.ok {
            color: #868686;
            .status {
              color: #6d8e3c;
            }
            &.selected {
              color: white;
              background-color: #7da73e;
              .status {
                color: white;
              }
            }
            &:hover {
              background-color: #2980a9;
            }
          }
        }
      }
    }

    .actions {
      .button {
        color: white;
        background-color: #2980a9;
        &.delete {
          background-color: #b76161;
          color: white;
          border: 1px solid #b76161;
        }
      }
    }
  }

  overflow-y: auto;

  .no-select {
    margin: 30px 0px 10px 5px;
    padding: 40px 10px 400px 10px;
    border-radius: 12px;
    border: 1px dashed;
    font-size: 0.8rem;
  }

  .onSites,
  .arrivals,
  .restaurants,
  .departures,
  .parkings {
    margin: 5px 0px 0px 15px;
    border: 1px solid;
    border-radius: 4px;
  }

  .title {
    text-align: left;
    padding: 10px 0 5px 10px;
    font-size: 0.8rem;
    font-weight: bold;
    border-top-left-radius: 4px;
    border-top-right-radius: 4px;
  }

  .content {
    font-size: 0.8rem;
    .item {
      padding: 3px 0 3px 10px;
      text-align: left;
      border-top: 1px solid;
      &.last {
        border-bottom-left-radius: 4px;
        border-bottom-right-radius: 4px;
      }

      &.ok {
        cursor: pointer;
        &:hover {
          color: white;
          .status {
            color: white !important;
          }
        }
      }
    }
  }

  .actions {
    border-radius: 4px;
    margin-left: 15px;
    padding: 5px 0;

    .button {
      font-size: 0.8rem;
      padding: 1px 10px;
      border-radius: 4px;
      font-weight: bolder;
      cursor: pointer;
      &.delete {
        margin-right: 5px;
      }
    }
  }
}
</style>
