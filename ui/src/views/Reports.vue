<template>
  <div class="reports w-full flex" :class="[hue]">
    <div class="center-panel w-full">
      <div class="section">
        <div class="title">Sites</div>
        <div v-for="site of sites" class="report" :key="site.definition.id">
          <div class="subtitle w-1/2 text-left">{{ site.definition.name }}</div>
          <div class="content">
            <SiteReport
              :values="siteData[site.definition.id]"
              :type="'site'"
              :name="'site-' + site.definition.id"
              :startDate="date"
              :nb="5"
            ></SiteReport>
          </div>
        </div>
      </div>
      <div class="section">
        <div class="title">Restaurants</div>
        <div v-for="restaurant of restaurants" class="report" :key="restaurant.definition.id">
          <div class="subtitle w-1/2 text-left">{{ restaurant.definition.name }}</div>
          <div class="content">
            <RestaurantReport
              :values="restaurantData[restaurant.definition.id]"
              :name="'restaurant-' + restaurant.definition.id"
              :startDate="date"
              :nb="5"
            ></RestaurantReport>
          </div>
        </div>
      </div>
      <div class="section">
        <div class="title">Parkings</div>
        <div v-for="parking of parkings" class="report" :key="parking.definition.id">
          <div class="subtitle w-1/2 text-left">{{ parking.definition.name }}</div>
          <div class="content">
            <ParkingReport
              :values="parkingData[parking.definition.id]"
              :name="'parking-' + parking.definition.id"
              :startDate="date"
              :nb="5"
            ></ParkingReport>
          </div>
        </div>
      </div>
    </div>
    <div class="right-panel select-none overflow-y-auto">
      <div class="date-wrapper">
        <date-pick v-model="date" :hasInputElement="false"></date-pick>
      </div>

      <div class="excel-wrapper flex justify-end">
        <div class="button flex" :class="{ disabled: !date }" @click="exportWeekly()">
          <img src="../assets/excel-48.png" />
          <div class="value">Download weekly report</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import store from "../store/index";
import axios from "axios";
import moment from "moment";
import DatePick from "vue-date-pick";
import "vue-date-pick/dist/vueDatePick.css";
import SiteReport from "@/components/report/SiteReport";
import RestaurantReport from "@/components/report/RestaurantReport";
import ParkingReport from "@/components/report/ParkingReport";

export default {
  store,
  components: { SiteReport, RestaurantReport, ParkingReport, DatePick },
  computed: {
    ...mapGetters([
      "hue",
      "sites",
      "siteDefinitions",
      "restaurants",
      "restaurantDefinitions",
      "parkings",
      "parkingDefinitions"
    ])
  },
  watch: {
    date(val) {
      this.updateDataReport(val ? val : moment().format("YYYY-MM-DD"));
    }
  },
  data() {
    return {
      reportSite: null,
      date: null,
      siteData: {},
      restaurantData: {},
      parkingData: {}
    };
  },
  methods: {
    setReportSite(val) {
      this.reportSite = val;
      this.reportRestaurant = null;
    },
    setReportRestaurant(val) {
      this.reportRestaurant = val;
      this.reportSite = null;
    },
    updateDataReport(date) {
      let vm = this;
      if (vm.sites) {
        for (let site of vm.sites) {
          axios
            .get(
              "api/usage/site/" +
                site.definition.id +
                "?startDate=" +
                date +
                "&limit=4"
            )
            .then(response => {
              vm.siteData[site.definition.id] = response.data;
              vm.$forceUpdate();
            });
        }
      }
      if (vm.restaurants) {
        for (let restaurant of vm.restaurants) {
          axios
            .get(
              "api/usage/restaurant/" +
                restaurant.definition.id +
                "?startDate=" +
                date +
                "&limit=4"
            )
            .then(response => {
              vm.restaurantData[restaurant.definition.id] = response.data;
              vm.$forceUpdate();
            });
        }
      }
      if (vm.parkings) {
        for (let parking of vm.parkings) {
          axios
            .get(
              "api/usage/parking/" +
                parking.definition.id +
                "?startDate=" +
                date +
                "&limit=4"
            )
            .then(response => {
              vm.parkingData[parking.definition.id] = response.data;
              vm.$forceUpdate();
            });
        }
      }
    },
    exportWeekly() {
      if (this.date) {
        window.location.href = "/api/report/weekly?startDate=" + this.date;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "@/scss/_variables.scss";

.reports {
  &.dark {
    .center-panel {
      .title,
      .subtitle {
        border-bottom: 1px solid #464646;
      }
    }
    .right-panel {
      border-color: #464646;
      .title {
        border-bottom: 1px solid #464646;
      }
    }

    .date-wrapper {
      ::v-deep {
        .vdpInnerWrap {
          border-color: #464646;
          background-color: #2d2d2d;
          .vdpHeader {
            background-color: #242424;
            .vdpPeriodControl {
              button {
                color: white;
              }
            }
          }

          .vdpCell {
            color: white;
            &.outOfRange {
              color: #6d6d6d;
            }
          }
        }
      }
    }

    .excel-wrapper {
      .button {
        border: 1px solid #464646;
        color: #c1c1c1;
      }
    }
  }

  &.light {
    .center-panel {
      .title,
      .subtitle {
        border-bottom: 1px solid #cacaca;
      }
    }
    .right-panel {
      border-color: #cacaca;
      .title {
        border-bottom: 1px solid #cacaca;
      }
      .excel-wrapper {
        .button {
          border: 1px solid #cacaca;
        }
      }
    }
  }

  height: 100vh;

  div.description {
    font-size: 0.6rem;
    color: #0c6498;
  }

  div.tag {
    font-size: 0.7rem;
    font-weight: bold;
    color: #0c6498;
  }

  .center-panel {
    padding-top: 180px;
    overflow-y: auto;
    .title {
      text-align: left;
      padding-left: 15px;
      font-size: 1rem;
      font-weight: 100;
    }

    .section {
      padding-bottom: 70px;
    }

    .subtitle {
      text-align: left;
      padding-left: 15px;
      margin-top: 20px;
      font-size: 0.8rem;
      font-weight: bold;
    }
  }

  .right-panel {
    width: 400px;
    padding-top: 200px;
    border-left: 1px solid;

    .date-wrapper {
      margin-left: 15px;
    }
    .excel-wrapper {
      margin-top: 20px;
      .button {
        cursor: pointer;
        font-size: 0.8rem;
        font-weight: bold;
        border-radius: 4px;
        padding: 1px 6px;
        img {
          height: 32px;
          width: auto;
        }
        .value {
          padding-top: 8px;
        }
        &:not(.disabled):hover {
          background-color: $primaryColor;
          border-color: $primaryColor;
          color: white;
        }

        &.disabled {
          opacity: 0.5;
        }
      }
    }
  }
}
</style>
