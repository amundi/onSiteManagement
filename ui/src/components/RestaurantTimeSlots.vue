<template>
  <div class="restaurant-time w-full flex" :class="[hue]">
    <div class="title">Lunch:</div>
    <div class="content w-full">
      <div class="w-full flex headers">
        <div class="w-1/6 header ml-auto">12h00</div>
        <div class="w-1/6 header">12h30</div>
        <div class="w-1/6 header">13h00</div>
        <div class="w-1/6 header">13h30</div>
        <div class="w-1/6 header mr-auto">&nbsp;</div>
      </div>
      <div class="w-full flex vals">
        <div
          class="w-1/6 val first ml-auto"
          :class="getClass('h1130', 1130)"
          @click="setLunch(1130, $event)"
        >&nbsp;</div>
        <div
          class="w-1/6 val"
          :class="getClass('h1200', 1200)"
          @click="setLunch(1200, $event)"
        >&nbsp;</div>
        <div
          class="w-1/6 val"
          :class="getClass('h1230', 1230)"
          @click="setLunch(1230, $event)"
        >&nbsp;</div>
        <div
          class="w-1/6 val"
          :class="getClass('h1300', 1300)"
          @click="setLunch(1300, $event)"
        >&nbsp;</div>
        <div
          class="w-1/6 val last mr-auto"
          :class="getClass('h1330', 1330)"
          @click="setLunch(1330, $event)"
        >&nbsp;</div>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import bus from "@/bus";

export default {
  computed: {
    ...mapGetters(["hue", "selectedRestaurant"])
  },
  props: ["lunch", "days"],
  mounted() {
    let vm = this;
    bus.$on("selectedDays", function() {
      vm.$forceUpdate();
    });
  },
  methods: {
    setLunch(val, e) {
      if (e.srcElement.classList.contains("available")) {
        this.$emit("setLunch", val);
      }
    },
    getClass(prop, val) {
      let cls = "";
      let empty = true;
      let available = true;
      for (let startOfMonth in this.days) {
        for (let day of this.days[startOfMonth]) {
          empty = false;
          if (
            !day.rest ||
            (day.rest[prop] - day.rest[prop + "_u"] <= 0 &&
              (!day.currentUserUsage ||
                day.currentUserUsage.restaurantId !== this.selectedRestaurant ||
                day.currentUserUsage.lunch !== val))
          ) {
            available = false;
          }
        }
      }

      if (!empty && available) {
        cls += "available ";
      }

      if (val === this.lunch) {
        cls += "selected ";
      }
      return cls;
    }
  }
};
</script>

<style lang="scss" scoped>
.restaurant-time {
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
            background: linear-gradient(130deg, #242525, $availableColor);
          }
          &.last:not(:hover):not(.selected) {
            background: linear-gradient(200deg, #242525, $availableColor);
          }
          &:hover {
            background-color: #0ab8ec;
          }
        }
        &:not(.available) {
          $notAvailableColor: #6f2b2a;
          background-color: $notAvailableColor;
          &.first {
            background: linear-gradient(130deg, #242525, $notAvailableColor);
          }
          &.last {
            background: linear-gradient(200deg, #242525, $notAvailableColor);
          }
        }
        &.selected {
          background-color: #28a2dc;
        }
      }
    }
  }

  margin: 10px;
  .restaurants {
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