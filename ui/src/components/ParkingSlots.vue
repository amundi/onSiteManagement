<template>
  <div class="parking-slots flex w-full" :class="[hue]">
    <div class="title">Parking:</div>
    <div class="content w-full">
      <div class="w-full flex vals">
        <div class="w-1/3 val start" :class="getClass('car')" @click="setParking('car', $event)">
          <font-awesome-icon icon="car"></font-awesome-icon>
        </div>
        <div class="w-1/3 val" :class="getClass('moto')" @click="setParking('moto', $event)">
          <font-awesome-icon icon="motorcycle"></font-awesome-icon>
        </div>
        <div class="w-1/3 val last" :class="getClass('bike')" @click="setParking('bike', $event)">
          <font-awesome-icon icon="bicycle"></font-awesome-icon>
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
    ...mapGetters(["hue", "selectedParking"])
  },
  props: ["parking", "days"],
  mounted() {
    let vm = this;
    bus.$on("selectedDays", function() {
      vm.$forceUpdate();
    });
  },
  methods: {
    setParking(val, e) {
      if (
        e.path[0].classList.contains("available") ||
        e.path[1].classList.contains("available") ||
        e.path[2].classList.contains("available")
      ) {
        this.$emit("setParking", val);
      }
    },
    getClass(prop) {
      let cls = "";
      let empty = true;
      let available = true;
      for (let startOfMonth in this.days) {
        for (let day of this.days[startOfMonth]) {
          empty = false;
          if (
            !day.park ||
            (day.park[prop] - day.park[prop + "_u"] <= 0 &&
              (!day.currentUserUsage ||
                day.currentUserUsage.parkingId !== this.selectedParking ||
                day.currentUserUsage.park !== prop))
          ) {
            available = false;
          }
        }
      }

      if (!empty && available) {
        cls += "available ";
      }

      if (prop === this.parking) {
        cls += "selected ";
      }
      return cls;
    }
  }
};
</script>

<style lang="scss" scoped>
.parking-slots {
  padding-top: 4px;
  &.light {
    .title {
      color: #268fc3;
    }
    .vals {
      .val {
        border-color: #8298ab !important;
        &.available {
          background-color: #d6f2d8;
          &:hover {
            background-color: #0ab8ec;
          }
        }
        &:not(.available) {
          background-color: #ffbbbb;
        }
        &.selected {
          background-color: #28a2dc;
          color: white;
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
          background-color: #2a4238;
          &:hover {
            background-color: #0ab8ec;
          }
        }
        &:not(.available) {
          background-color: #6f2b2a;
        }
        &.selected {
          background-color: #28a2dc;
        }
      }
    }
  }

  margin: 10px;
  .parkings {
    margin: 10px;
  }

  .title {
    font-size: 0.9rem;
    margin-bottom: 5px;
    margin-top: 5px;
    margin-right: 10px;
  }

  .vals {
    .val {
      height: 25px;
      border-bottom: 1px solid;
      border-top: 1px solid;
      cursor: pointer;
      &.start {
        border-left: 1px solid;
        border-top-left-radius: 4px;
        border-bottom-left-radius: 4px;
      }
      border-right: 1px solid;

      &.last {
        border-top-right-radius: 4px;
        border-bottom-right-radius: 4px;
      }
    }
  }
}
</style>