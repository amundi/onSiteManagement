<template>
  <div class="calendarEditor select-none" :class="[hue]">
    <div class="title text-left">{{ monthName }}</div>

    <div class="header flex w-full px-4">
      <div class="day-col">Mon</div>
      <div class="day-col">Tue</div>
      <div class="day-col">Wed</div>
      <div class="day-col">Thu</div>
      <div class="day-col">Fri</div>
      <div class="day-col">Sat</div>
      <div class="day-col">Sun</div>
    </div>
    <div class="content px-4" v-if="calendarData">
      <div class="week flex" v-for="(week, index) of calendarData" :key="index">
        <div
          v-for="(day, i) of week"
          :key="day.day"
          class="day-col day"
          :class="{
            notADay: !day.day,
            beforeNow: day.status == 'beforeNow',
            notOpen: !day.usage,
            selected: day.selected,
            right: i == 6,
            left: i == 0,
            bottom: index == calendarData.length - 1
          }"
          @click="updateSelectStatus(day)"
        >
          <div class="dayValue">{{ day.day }}</div>
          <div v-if="day.currentUserUsage" class="currentUserUsage">
            <div
              v-if="((day.currentUserUsage.onSite || day.currentUserUsage.arrival) && getSettingProperty('manageOnSites') && !getSettingProperty('manageArrivals'))
              || (day.currentUserUsage.onSite && !day.currentUserUsage.arrival && getSettingProperty('manageArrivals'))"
              class="text-right"
            >
              <font-awesome-icon icon="map-marker-alt"></font-awesome-icon>
            </div>
            <div
              v-if="day.currentUserUsage.arrival && getSettingProperty('manageArrivals')"
              class="flex justify-between"
            >
              <font-awesome-icon icon="plane-arrival" class="mr-2"></font-awesome-icon>
              &nbsp;{{ getAsTime(day.currentUserUsage.arrival) }}
            </div>
            <div
              v-if="day.currentUserUsage.restaurant && getSettingProperty('manageRestaurants')"
              class="flex justify-between"
            >
              <font-awesome-icon icon="drumstick-bite" class="mr-2"></font-awesome-icon>
              &nbsp;{{ getAsTime(day.currentUserUsage.restaurant) }}
            </div>
            <div
              v-if="day.currentUserUsage.departure && getSettingProperty('manageDepartures')"
              class="flex justify-between"
            >
              <font-awesome-icon icon="plane-departure" class="mr-2"></font-awesome-icon>
              &nbsp;{{ getAsTime(day.currentUserUsage.departure) }}
            </div>
            <div
              v-if="day.currentUserUsage.car && getSettingProperty('manageParkings')"
              class="text-right"
            >
              <font-awesome-icon icon="car"></font-awesome-icon>
            </div>
            <div
              v-if="day.currentUserUsage.moto && getSettingProperty('manageParkings')"
              class="text-right"
            >
              <font-awesome-icon icon="motorcycle"></font-awesome-icon>
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
import moment from "moment";
import bus from "@/bus";

export default {
  store,
  props: ["startOfMonth"],
  computed: {
    ...mapGetters([
      "hue",
      "siteUsages",
      "settings",
      "selectedSite",
      "currentUserUsages"
    ]),
    monthName() {
      return this.startOfMonth
        ? moment(this.startOfMonth).format("MMMM")
        : null;
    }
  },
  watch: {
    siteUsages(val) {
      this.updateCalendarData(val, this.currentUserUsages);
      this.$forceUpdate();
    },
    currentUserUsages(val) {
      this.updateCalendarData(this.siteUsages, val);
      this.$forceUpdate();
    }
  },
  beforeMount() {
    this.updateCalendarData(this.siteUsages, this.currentUserUsages);
  },
  data() {
    return {
      calendarData: null,
      selectedDays: []
    };
  },
  mounted() {
    let vm = this;
    bus.$on("clearDaySelections", function() {
      vm.selectedDays = [];
      vm.$forceUpdate();
    });
  },
  methods: {
    getAsTime(val) {
      if (val) {
        let valAsStr = val.toString();
        if (val == 1030) {
          return "> 10:30";
        } else if (val == 700) {
          return "< 7:30";
        } else if (val >= 1000) {
          return valAsStr.substring(0, 2) + ":" + valAsStr.substring(2);
        } else {
          return valAsStr.substring(0, 1) + ":" + valAsStr.substring(1);
        }
      }
      return val;
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
    updateCalendarData(siteUsages, currentUserUsages) {
      let vm = this;
      const now = moment();
      if (siteUsages && currentUserUsages && vm.startOfMonth) {
        let data = [];
        let currentWeek = [{}, {}, {}, {}, {}, {}, {}];
        let st = moment(vm.startOfMonth);
        let end = st.clone().endOf("month");
        for (
          let date = st.clone();
          !date.isAfter(end);
          date = date.clone().add(1, "days")
        ) {
          currentWeek[date.isoWeekday() - 1].day = date.date();
          if (date.isBefore(now)) {
            currentWeek[date.isoWeekday() - 1].status = "beforeNow";
          }
          if (date.isoWeekday() == 7) {
            data.push(currentWeek);
            currentWeek = [{}, {}, {}, {}, {}, {}, {}];
          }
        }
        if (currentWeek[0].day) {
          data.push(currentWeek);
        }
        // Adding siteUsages
        for (let usage of siteUsages) {
          let m = moment(usage.date);
          if (
            m
              .clone()
              .startOf("month")
              .isSame(st)
          ) {
            for (let week of data) {
              for (let day of week) {
                if (m.date() == day.day) {
                  day.usage = usage;
                }
              }
            }
          }
        }
        // Adding currentUserUsages
        for (let usage of currentUserUsages) {
          let m = moment(usage.date);
          if (
            m
              .clone()
              .startOf("month")
              .isSame(st)
          ) {
            for (let week of data) {
              for (let day of week) {
                if (
                  m.date() == day.day &&
                  vm.selectedSite &&
                  usage.site &&
                  vm.selectedSite.id == usage.site.id
                ) {
                  day.currentUserUsage = usage;
                }
              }
            }
          }
        }
        vm.calendarData = data;
      }
    },
    updateSelectStatus(day) {
      let vm = this;
      if (day.selected) {
        day.selected = false;
        const index = vm.selectedDays.indexOf(day);
        if (index >= 0) {
          vm.selectedDays.splice(index, 1);
          bus.$emit("selectedDays", {
            startOfMonth: vm.startOfMonth,
            selectedDays: vm.selectedDays
          });
          this.$forceUpdate();
        }
      } else if (day.day && day.status != "beforeNow" && day.usage) {
        day.selected = true;
        vm.selectedDays.push(day);
        bus.$emit("selectedDays", {
          startOfMonth: vm.startOfMonth,
          selectedDays: vm.selectedDays
        });
        this.$forceUpdate();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.calendarEditor {
  &.light {
    .title {
      border-color: #cacaca;
    }

    .header {
      .day-col {
        color: #2a7b94;
      }
    }

    .content {
      .day-col.day {
        border-color: #8d959c !important;
        color: #2b67b3;
        &.beforeNow {
          color: #5e5e5e;
        }
        &.notOpen:not(.beforeNow):not(.notADay) {
          color: #00b6ed;
          background-color: #e6ebef;
        }
        &:not(.notOpen):not(.beforeNow):not(.notADay) {
          background-color: white;
          &.selected {
            //background-color: #bee3f3;
            background-color: #d6f2d8;
            // box-shadow: 0px 0px 8px 1px #b2efc2;
          }

          &:hover {
            background-color: #47d4ff;
          }
        }
        .currentUserUsage {
          color: white;
          background-color: #06a4d4;
        }
      }
    }
  }

  &.dark {
    .title {
      border-color: #464646;
    }

    .header {
      .day-col {
        color: #00b6ed;
      }
    }

    .content {
      .day-col.day {
        border-color: #464646 !important;
        &.beforeNow {
          color: #5e5e5e;
        }
        &.notOpen:not(.beforeNow):not(.notADay) {
          color: #5a5a5a;
          background-color: #191919;
        }
        &:not(.notOpen):not(.beforeNow):not(.notADay) {
          background-color: #242424;
          &.selected {
            background-color: #242f23;
            // box-shadow: 0px 0px 8px 1px #3b5839;
          }

          &:hover {
            background-color: #2980a9;
            color: white;
          }
        }
      }

      .currentUserUsage {
        color: white;
        background-color: #1b5688;
      }
    }
  }

  margin-bottom: 30px;

  .title {
    font-size: 0.8rem;
    font-weight: 900;
    margin-bottom: 25px;
    margin-right: 200px;
    padding: 0 0 0 15px;
    border-bottom: 1px solid;
  }

  .header {
    .day-col {
      text-align: left;
      font-weight: normal;
      font-size: 1rem;
    }
  }

  .day-col {
    width: 14.2857%;
    &.day {
      height: 120px;

      &:not(.bottom):not(.notADay) {
        border-left: 1px solid;
        border-top: 1px solid;
      }
      &.left:not(.notADay) {
        border-left: 1px solid;
      }
      &.bottom {
        border-top: 1px solid;
        &:not(.notADay) {
          border-right: 1px solid;
        }
      }

      &.right:not(.notADay) {
        border-right: 1px solid;
      }
      &.bottom:not(.notADay) {
        border-bottom: 1px solid;
      }

      .dayValue {
        text-align: right;
        font-size: 1.2rem;
        font-weight: 100;
        padding: 5px 5px 0 0;
      }

      .currentUserUsage {
        display: table;
        text-align: left;
        margin-left: 5px;
        padding: 5px;
        font-size: 0.7rem;
        border-radius: 4px;
      }

      &.beforeNow {
        background: repeating-linear-gradient(
          -60deg,
          transparent,
          transparent 8px,
          #40404020 8px,
          #40404020 16px
        );
        min-height: 100%;
      }

      &:not(.notADay):not(.beforeNow):not(.notOpen) {
        cursor: pointer;
        &.selected {
          z-index: 1;
        }

        &:hover {
          z-index: 2;
        }
      }
    }
  }

  .content {
    padding-top: 0px;
  }
}
</style>
