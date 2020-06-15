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
            notOpen: !day.site,
            selected: day.selected,
            right: i == 6,
            left: i == 0,
            bottom: index == calendarData.length - 1
          }"
          @click="updateSelectStatus(day)"
        >
          <div class="dayValue">{{ day.day }}</div>
          
          <template v-if="day.currentUserUsage" >
            <div v-if="day.currentUserUsage.offSite" class="currentUserUsage off">
              <div><font-awesome-icon icon="house-user"></font-awesome-icon></div>
            </div>
            <div v-if="!day.currentUserUsage.offSite" class="currentUserUsage">
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
                v-if="day.currentUserUsage.lunch && getSettingProperty('manageRestaurants')"
                class="flex justify-between"
              >
                <font-awesome-icon icon="drumstick-bite" class="mr-2"></font-awesome-icon>
                &nbsp;{{ getAsTime(day.currentUserUsage.lunch) }}
              </div>
              <div
                v-if="day.currentUserUsage.departure && getSettingProperty('manageDepartures')"
                class="flex justify-between"
              >
                <font-awesome-icon icon="plane-departure" class="mr-2"></font-awesome-icon>
                &nbsp;{{ getAsTime(day.currentUserUsage.departure) }}
              </div>
              <div
                v-if="day.currentUserUsage.parkingType && getSettingProperty('manageParkings')"
                class="text-right"
              >
                <font-awesome-icon v-if="day.currentUserUsage.parkingType==='car'" icon="car"></font-awesome-icon>
                <font-awesome-icon
                  v-if="day.currentUserUsage.parkingType==='moto'"
                  icon="motorcycle"
                ></font-awesome-icon>
                <font-awesome-icon v-if="day.currentUserUsage.parkingType==='bike'" icon="bicycle"></font-awesome-icon>
              </div>
            </div>
          </template>
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
      "settings",
      "siteUsages",
      "restaurantUsages",
      "parkingUsages",
      "selectedSite",
      "selectedRestaurant",
      "selectedParking",
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
      let vm = this;
      vm.updateCalendarData(
        val,
        vm.restaurantUsages,
        vm.parkingUsages,
        vm.currentUserUsages
      );
      vm.$forceUpdate();
    },
    restaurantUsages(val) {
      let vm = this;
      vm.updateCalendarData(
        vm.siteUsages,
        val,
        vm.parkingUsages,
        vm.currentUserUsages
      );
      vm.$forceUpdate();
    },
    parkingUsages(val) {
      let vm = this;
      vm.updateCalendarData(
        vm.siteUsages,
        vm.restaurantUsages,
        val,
        vm.currentUserUsages
      );
      vm.$forceUpdate();
    },
    currentUserUsages(val) {
      let vm = this;
      vm.updateCalendarData(
        vm.siteUsages,
        vm.restaurantUsages,
        vm.parkingUsages,
        val
      );
      vm.$forceUpdate();
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
      // if(vm.calendarData) {
      //   for(let row of vm.calendarData) {
      //     for(let day of row) {
      //       day.selected = false;
      //     }
      //   }
      // }
      vm.$forceUpdate();
    });
    vm.updateCalendarData(
      vm.siteUsages,
      vm.restaurantUsages,
      vm.parkingUsages,
      vm.currentUserUsages
    );
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
    updateCalendarData(
      siteUsages,
      restaurantUsages,
      parkingUsages,
      currentUserUsages
    ) {
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
                  day.site = usage;
                }
              }
            }
          }
        }
        // Adding restaurantUsages
        if (restaurantUsages) {
          for (let usage of restaurantUsages) {
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
                    day.rest = usage;
                  }
                }
              }
            }
          }
        }

        // Adding parkingUsages
        if (parkingUsages) {
          for (let usage of parkingUsages) {
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
                    day.park = usage;
                  }
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
                  usage.siteId &&
                  vm.selectedSite == usage.siteId
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
      } else if (day.day && day.status != "beforeNow" && day.site) {
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
        border-color: #d0dee8 !important;
        color: #2b67b3;
        &.beforeNow {
          color: #00b6ed;
          background-color: white;
        }
        &.notOpen:not(.beforeNow):not(.notADay) {
          color: #00b6ed;
          background-color: #d8eef9;
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
          &.off {
            background-color: #bf80ac;
          }
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
        border-color: #2f414a !important;
        &.beforeNow {
          color: #909090;
        }
        &.notOpen:not(.beforeNow):not(.notADay) {
          color: #909090;
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
        &.off {
          background-color: #7a457d;
        }
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
          #61a7de2e 8px,
          #61a7de2e 16px
        );
        // background: repeating-linear-gradient(
        //   -60deg,
        //   transparent,
        //   transparent 8px,
        //   #40404020 8px,
        //   #40404020 16px
        // );
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
