<template>
  <div class="site-time w-full flex" :class="[hue]">
    <div v-if="manageArrival" class="arrivals flex w-7/12">
      <div class="title">Arrival:</div>
      <div class="content w-full">
        <div class="w-full flex headers">
          <div class="w-1/6 header ml-auto">7h30</div>
          <div class="w-1/12 header">8h00</div>
          <div class="w-1/12 header">8h30</div>
          <div class="w-1/12 header">9h00</div>
          <div class="w-1/12 header">9h30</div>
          <div class="w-1/12 header">10h00</div>
          <div class="w-1/12 header">10h30</div>
          <div class="w-1/6 header mr-auto">&nbsp;</div>
        </div>
        <div class="w-full flex vals">
          <div
            class="w-1/6 val first ml-auto"
            :class="getArrivalClass('h700', 700)"
            @click="e => setArrival(700, e)"
          >&nbsp;</div>
          <div
            class="w-1/12 val"
            :class="getArrivalClass('h730', 730)"
            @click="e => setArrival(730, e)"
          >&nbsp;</div>
          <div
            class="w-1/12 val"
            :class="getArrivalClass('h800', 800)"
            @click="e => setArrival(800, e)"
          >&nbsp;</div>
          <div
            class="w-1/12 val"
            :class="getArrivalClass('h830', 830)"
            @click="e => setArrival(830, e)"
          >&nbsp;</div>
          <div
            class="w-1/12 val"
            :class="getArrivalClass('h900', 900)"
            @click="e => setArrival(900, e)"
          >&nbsp;</div>
          <div
            class="w-1/12 val"
            :class="getArrivalClass('h930', 930)"
            @click="e => setArrival(930, e)"
          >&nbsp;</div>
          <div
            class="w-1/12 val"
            :class="getArrivalClass('h1000', 1000)"
            @click="e => setArrival(1000, e)"
          >&nbsp;</div>
          <div
            class="w-1/6 val last mr-auto"
            :class="getArrivalClass('h1030', 1030)"
            @click="e => setArrival(1030, e)"
          >&nbsp;</div>
        </div>
      </div>
    </div>
    <div v-if="manageDeparture" class="departures flex w-5/12">
      <div class="title">Departure:</div>
      <div class="content w-full">
        <div class="w-full flex headers">
          <div class="w-1/6 header ml-auto">17h30</div>
          <div class="w-1/6 header">18h00</div>
          <div class="w-1/6 header">18h30</div>
          <div class="w-1/6 header">19h00</div>
          <div class="w-1/6 header mr-auto">&nbsp;</div>
        </div>
        <div class="w-full flex vals">
          <div
            class="w-1/6 val first ml-auto"
            :class="getDepartureClass('h1700', 1700)"
            @click="e => setDeparture(1700, e)"
          >&nbsp;</div>
          <div
            class="w-1/6 val"
            :class="getDepartureClass('h1730', 1730)"
            @click="e => setDeparture(1730, e)"
          >&nbsp;</div>
          <div
            class="w-1/6 val"
            :class="getDepartureClass('h1800', 1800)"
            @click="e => setDeparture(1800, e)"
          >&nbsp;</div>
          <div
            class="w-1/6 val"
            :class="getDepartureClass('h1830', 1830)"
            @click="e => setDeparture(1830, e)"
          >&nbsp;</div>
          <div
            class="w-1/6 val last mr-auto"
            :class="getDepartureClass('h1900', 1900)"
            @click="e => setDeparture(1900, e)"
          >&nbsp;</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import bus from "@/bus";

export default {
  computed: {
    ...mapGetters(["hue", "selectedSite"])
  },
  props: [
    "manageOnSites",
    "manageArrival",
    "manageDeparture",
    "arrival",
    "departure",
    "days"
  ],
  mounted() {
    let vm = this;
    bus.$on("selectedDays", function() {
      vm.$forceUpdate();
    });
  },
  methods: {
    setArrival(val, e) {
      if (e.srcElement.classList.contains("available")) {
        this.$emit("setArrival", val);
      }
    },
    setDeparture(val, e) {
      if (e.srcElement.classList.contains("available")) {
        this.$emit("setDeparture", val);
      }
    },
    getArrivalClass(prop, val) {
      let cls = "";
      let empty = true;
      let available = true;
      for (let startOfMonth in this.days) {
        for (let day of this.days[startOfMonth]) {
          empty = false;
          if (
            day.site[prop] - day.site[prop + "_u"] <= 0 &&
            (!day.currentUserUsage ||
              day.currentUserUsage.siteId !== this.selectedSite ||
              day.currentUserUsage.arrival !== val)
          ) {
            if (
              !this.manageOnSites ||
              !day.currentUserUsage ||
              day.currentUserUsage.siteId !== this.selectedSite
            ) {
              available = false;
            }
          }
          if (
            this.manageOnSites &&
            day.site["onSite"] - day.site["onSite_u"] <= 0 &&
            (!day.currentUserUsage || day.currentUserUsage.siteId !== this.selectedSite)
          ) {
            available = false;
          }
        }
      }

      if (!empty && available) {
        cls += "available ";
      }

      if (val === this.arrival) {
        cls += "selected ";
      }
      return cls;
    },
    getDepartureClass(prop, val) {
      let cls = "";
      let empty = true;
      let available = true;
      for (let startOfMonth in this.days) {
        for (let day of this.days[startOfMonth]) {
          empty = false;
          if (
            day.site[prop] - day.site[prop + "_u"] <= 0 &&
            (!day.currentUserUsage ||
              day.currentUserUsage.siteId !== this.selectedSite ||
              day.currentUserUsage.departure !== val)
          ) {
            if (
              !this.manageOnSites ||
              !day.currentUserUsage ||
              day.currentUserUsage.siteId !== this.selectedSite
            ) {
              available = false;
            }
          }
          if (
            this.manageOnSites &&
            day.site["onSite"] - day.site["onSite_u"] <= 0 &&
            (!day.currentUserUsage || day.currentUserUsage.siteId !== this.selectedSite)
          ) {
            available = false;
          }
        }
      }

      if (!empty && available) {
        cls += "available ";
      }

      if (val === this.departure) {
        cls += "selected ";
      }
      return cls;
    }
  }
};
</script>

<style lang="scss" scoped>
.site-time {
  &.light {
    .title {
      color: #268fc3;
    }
    .vals {
      .val {
        border-color: #8298ab !important;
        &.available {
          $availableColor: #d6f2d8;
          background-color: $availableColor;
          &.first:not(:hover):not(.selected) {
            background: linear-gradient(130deg, white, $availableColor);
          }
          &.last:not(:hover):not(.selected) {
            background: linear-gradient(200deg, white, $availableColor);
          }
          &:hover {
            background-color: #0ab8ec;
          }
        }
        &:not(.available) {
          $notAvailableColor: #ffbbbb;
          background-color: $notAvailableColor;
          &.first {
            background: linear-gradient(130deg, white, $notAvailableColor);
          }
          &.last {
            background: linear-gradient(200deg, white, $notAvailableColor);
          }
        }
        &.selected {
          background-color: #28a2dc;
        }
      }
    }
  }
  &.dark {
    .title {
      color: #268fc3;
    }
    .vals {
      .val {
        border-color: #4e5358 !important;
        &.available {
          $availableColor: #2a4238;
          background-color: $availableColor;
          &.first:not(:hover):not(.selected) {
            background: linear-gradient(130deg, #1e1f20, $availableColor);
          }
          &.last:not(:hover):not(.selected) {
            background: linear-gradient(200deg, #1e1f20, $availableColor);
          }
          &:hover {
            background-color: #0ab8ec;
          }
        }
        &:not(.available) {
          $notAvailableColor: #6f2b2a;
          background-color: $notAvailableColor;
          &.first {
            background: linear-gradient(130deg, #1e1f20, $notAvailableColor);
          }
          &.last {
            background: linear-gradient(200deg, #1e1f20, $notAvailableColor);
          }
        }
        &.selected {
          background-color: #28a2dc;
        }
      }
    }
  }

  margin-bottom: 5px;
  .arrivals,
  .departures {
    margin: 10px;
  }

  .title {
    font-size: 0.9rem;
    margin-bottom: 5px;
    margin-top: 10px;
  }
  .headers {
    font-size: 0.6rem;
    margin-left: 13px;
    .header {
      text-align: right;
    }
  }
  .vals {
    .val {
      height: 14px;
      border-bottom: 1px solid;
      &.available {
        cursor: pointer;
      }
      &.start {
        border-left: 1px solid;
      }
      &:not(.last) {
        border-right: 1px solid;
      }
    }
  }
}
</style>