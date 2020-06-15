<template>
  <div id="booking" :class="[hue]">
    <template v-if="selectedSite && getDayCount() > 0">
      <div class="container mx-auto">
        <div class="wrapper">
          <div class="onsite flex">
            <toggle-button
              class="onSiteButton"
              :labels="{checked: 'On site', unchecked: 'Off site'}"
              :width="70"
              :color="{
              checked: '#26a2dc',
              unchecked: '#3c4f54',
            }"
              :sync="true"
              v-model="onSite"
            />
          </div>
          <div
            v-if="onSite && (getSettingProperty('manageArrivals') || getSettingProperty('manageDepartures'))"
            class="site flex"
          >
            <SiteTimeSlots
              :manageOnSites="getSettingProperty('manageOnSites')"
              :manageArrival="getSettingProperty('manageArrivals')"
              :manageDeparture="getSettingProperty('manageDepartures')"
              :arrival="arrival"
              :departure="departure"
              :days="days"
              @setArrival="setArrival"
              @setDeparture="setDeparture"
            />
          </div>
          <div class="rest flex justify-between">
            <div v-if="onSite && getSettingProperty('manageRestaurants')" class="w-5/12">
              <RestaurantTimeSlots :lunch="lunch" :days="days" @setLunch="setLunch" />
            </div>
            <div v-if="onSite && getSettingProperty('manageParkings')" class="w-1/4">
              <ParkingSlots :parking="parking" :days="days" @setParking="setParking" />
            </div>
            <div
              v-if="(!getSettingProperty('manageParkings') && !getSettingProperty('manageRestaurants')) || !onSite"
            >&nbsp;</div>
            <div class="actions flex justify-end" v-if="getDayCount() > 0">
              <div class="button delete" @click="deleteBookings()">remove</div>
              <div class="button" @click="saveBookings()">save</div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import store from "../store/index";
import moment from "moment";
import bus from "@/bus";
import SiteTimeSlots from "@/components/SiteTimeSlots";
import RestaurantTimeSlots from "@/components/RestaurantTimeSlots";
import ParkingSlots from "@/components/ParkingSlots";

export default {
  store,
  components: { SiteTimeSlots, RestaurantTimeSlots, ParkingSlots },
  computed: {
    ...mapGetters([
      "hue",
      "currentUser",
      "selectedSite",
      "selectedRestaurant",
      "selectedParking",
      "settings"
    ])
  },
  watch: {
    selectedSite() {
      this.days = {};
    }
  },
  data() {
    return {
      days: {},
      arrivalValues: [
        "h700",
        "h730",
        "h800",
        "h830",
        "h900",
        "h930",
        "h1000",
        "h1030"
      ],
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
      vm.onSite = true;
      vm.$forceUpdate();
    });
    bus.$on("clearPendingActions", function() {
      vm.days = {};
      vm.$forceUpdate();
      bus.$emit("clearDaySelections");
    });
  },
  methods: {
    setArrival(val) {
      this.arrival = this.arrival === val ? null: val;
    },
    setDeparture(val) {
      this.departure = this.departure === val ? null: val;
    },
    setLunch(val) {
      this.lunch = this.lunch === val ? null: val;
    },
    setParking(val) {
      this.parking = this.parking === val ? null : val;
    },
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
    saveBookings() {
      let vm = this;
      if (vm.getSettingProperty("manageArrivals") && vm.onSite && !vm.arrival) {
        store.commit("SET_INFO", "Error: missing arrival info");
      } else if (vm.getSettingProperty("manageDepartures") && vm.onSite && !vm.departure) {
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
              siteId: vm.selectedSite,
              restaurantId: vm.selectedRestaurant,
              parkingId: vm.selectedParking,
              date: day.site.date,
              arrival: vm.arrival,
              departure: vm.departure,
              lunch: vm.lunch,
              parkingType: vm.parking,
              onSite: vm.onSite || !!vm.arrival,
              offSite: !vm.onSite
            });
          }
        }

        store.dispatch("saveCurrentUserUsages", {
          user: vm.currentUser,
          startDate: startDate.format("YYYY-MM-DD"),
          userDays: bookings
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
            date: day.site.date
          });
        }
      }

      store.dispatch("deleteCurrentUserUsages", {
        user: vm.currentUser,
        site: vm.selectedSite.id,
        startDate: startDate.format("YYYY-MM-DD"),
        userDays: bookings
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
    border-top-color: #cecece;
    background: linear-gradient(60deg, #ffffff 0%, #fafafa 35%, #ffffff 100%);

    .actions {
      .button.delete {
        background-color: #ff6363;
      }
    }
  }

  &.dark {
    border-top-color: #4a4a4a;
    background: linear-gradient(60deg, #1d1d1d 0%, #212121 35%, #1d1e1f 100%);

    .actions {
      .button.delete {
        background-color: #a54343;
      }
    }
  }
  border-top: 1px solid;
  box-shadow: 0 0px 10px 0 rgba(12, 13, 14, 0.18);

  .wrapper {
    z-index: 10;
    margin: 0 230px 0 130px;
    padding: 5px;
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;

    margin-top: 10px;
  }

  .actions {
    .button {
      color: white;
      background-color: #57add6;
      height: max-content;
      padding: 2px 6px;
      margin: 10px 5px 10px 5px;
      border-radius: 5px;
      border: 1px solid transparent;
      opacity: 0.9;
      cursor: pointer;
      &.delete {
        border: 1px solid #f74a4a;
        color: white;
      }
      &:hover {
        opacity: 1;
      }
    }
  }

  z-index: 10;
  overflow-y: auto;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
}
</style>
