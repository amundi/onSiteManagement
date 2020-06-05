<template>
  <div ref="main" class="graph entity flex justify-center" :class="[hue]" :id="name + '-entity'"></div>
</template>

<script>
import moment from "moment";
import * as d3 from "d3";
import { mapGetters } from "vuex";

export default {
  props: ["values", "type", "name", "startDate", "nb"],
  computed: {
    ...mapGetters(["hue"])
  },
  watch: {
    startDate(val) {
      this.updateView(val, this.values);
    },
    values(val) {
      this.updateView(this.startDate, val);
    }
  },
  data() {
    return {
      maxValue: 0,
      svg: null,
      x: null,
      y: null,
      localIndex: 0
    };
  },
  methods: {
    updateView(startDate, values) {
      let vm = this;
      if (startDate && values) {
        let dates = vm.getDates(startDate, vm.nb);
        let max = vm.getMax(values);

        //         let mainWidth = this.$refs.main.getBoundingClientRect().width;
        let mainWidth = 700;
        let mainHeight = 200;

        let margin = { top: 10, right: 10, bottom: 10, left: 10 },
          width = mainWidth - margin.left - margin.right,
          height = mainHeight - margin.top - margin.bottom;

        // Empty and setup svg -------------------------------------------
        d3.select("#" + vm.name + "-entity")
          .selectAll("svg")
          .remove();

        vm.svg = d3
          .select("#" + vm.name + "-entity")
          .append("svg")
          .attr("width", mainWidth)
          .attr("height", mainHeight)
          .append("g")
          .attr(
            "transform",
            "translate(" + margin.left + "," + margin.top + ")"
          );

        // Add axis ------------------------------------------------------
        vm.x = d3
          .scaleLinear()
          .domain([0, 100 * vm.nb]) // each day has a value of 100
          .range([0, width]);
        let unit = (100 * vm.nb) / width;

        let h = height - 20;
        vm.y = d3
          .scaleLinear()
          .domain([0, max])
          .range([h, 5]);

        let index = 0;
        for (let date of dates) {
          if (index % 200 == 0) {
            vm.svg
              .append("rect")
              .attr("x", vm.x(index))
              .attr("y", 0)
              .attr("width", vm.x(index + 100) - vm.x(index + 5))
              .attr("height", height)
              .attr("class", "odd");
          }

          // Values
          for (let val of values) {
            if (date == val.date) {
              vm.localIndex = index;

              // Presence ------------------------------------
              if (val["onSite"] && val["onSite_u"] && val["onSite"] > 0) {
                vm.addRect(
                  0,
                  h,
                  3 * unit,
                  (val["onSite_u"] > val["onSite"]
                    ? 1
                    : val["onSite_u"] / val["onSite"]) * max,
                  "on usage"
                );
                vm.addRect(
                  2,
                  h,
                  3 * unit,
                  (val["onSite_u"] > val["onSite"]
                    ? val["onSite"] / val["onSite_u"]
                    : 1) * max,
                  "on def"
                );
              }

              vm.svg
                .append("line")
                .attr("class", "sepline")
                .attr("x1", vm.x(6 + vm.localIndex))
                .attr("y1", 0)
                .attr("x2", vm.x(6 + vm.localIndex))
                .attr("y2", h);

              // Arrivals ------------------------------------
              vm.addRect(8, h, 3 * unit, val["h700_u"], "arr usage");
              vm.addRect(10, h, 3 * unit, val["h700"], "arr def");
              vm.addRect(12, h, 3 * unit, val["h730_u"], "arr usage");
              vm.addRect(14, h, 3 * unit, val["h730"], "arr def");
              vm.addRect(16, h, 3 * unit, val["h800_u"], "arr usage");
              vm.addRect(18, h, 3 * unit, val["h800"], "arr def");
              vm.addRect(20, h, 3 * unit, val["h830_u"], "arr usage");
              vm.addRect(22, h, 3 * unit, val["h830"], "arr def");
              vm.addRect(24, h, 3 * unit, val["h900_u"], "arr usage");
              vm.addRect(26, h, 3 * unit, val["h900"], "arr def");
              vm.addRect(28, h, 3 * unit, val["h930_u"], "arr usage");
              vm.addRect(30, h, 3 * unit, val["h930"], "arr def");
              vm.addRect(32, h, 3 * unit, val["h1000_u"], "arr usage");
              vm.addRect(34, h, 3 * unit, val["h1000"], "arr def");

              vm.svg
                .append("line")
                .attr("class", "sepline")
                .attr("x1", vm.x(38 + vm.localIndex))
                .attr("y1", 0)
                .attr("x2", vm.x(38 + vm.localIndex))
                .attr("y2", h);

              // Restaurants -----------------------------------
              vm.addRect(40, h, 3 * unit, val["h1130_u"], "rest usage");
              vm.addRect(42, h, 3 * unit, val["h1130"], "rest def");
              vm.addRect(44, h, 3 * unit, val["h1200_u"], "rest usage");
              vm.addRect(46, h, 3 * unit, val["h1200"], "rest def");
              vm.addRect(48, h, 3 * unit, val["h1230_u"], "rest usage");
              vm.addRect(50, h, 3 * unit, val["h1230"], "rest def");
              vm.addRect(52, h, 3 * unit, val["h1300_u"], "rest usage");
              vm.addRect(54, h, 3 * unit, val["h1300"], "rest def");
              vm.addRect(56, h, 3 * unit, val["h1330_u"], "rest usage");
              vm.addRect(58, h, 3 * unit, val["h1330"], "rest def");

              vm.svg
                .append("line")
                .attr("class", "sepline")
                .attr("x1", vm.x(62 + vm.localIndex))
                .attr("y1", 0)
                .attr("x2", vm.x(62 + vm.localIndex))
                .attr("y2", h);

              // Departures -----------------------------------
              vm.addRect(64, h, 3 * unit, val["h1700_u"], "dep usage");
              vm.addRect(66, h, 3 * unit, val["h1700"], "dep def");
              vm.addRect(68, h, 3 * unit, val["h1730_u"], "dep usage");
              vm.addRect(70, h, 3 * unit, val["h1730"], "dep def");
              vm.addRect(72, h, 3 * unit, val["h1800_u"], "dep usage");
              vm.addRect(74, h, 3 * unit, val["h1800"], "dep def");
              vm.addRect(76, h, 3 * unit, val["h1830_u"], "dep usage");
              vm.addRect(78, h, 3 * unit, val["h1830"], "dep def");
              vm.addRect(80, h, 3 * unit, val["h1900_u"], "dep usage");
              vm.addRect(82, h, 3 * unit, val["h1900"], "dep def");

              vm.svg
                .append("line")
                .attr("class", "sepline")
                .attr("x1", vm.x(86 + vm.localIndex))
                .attr("y1", 0)
                .attr("x2", vm.x(86 + vm.localIndex))
                .attr("y2", h);

              // Parkings -------------------------------------
              vm.addRect(88, h, 3 * unit, val["car_u"], "park usage");
              vm.addRect(90, h, 3 * unit, val["car"], "park def");
              vm.addRect(92, h, 3 * unit, val["moto_u"], "park usage");
              vm.addRect(94, h, 3 * unit, val["moto"], "park def");

              vm.svg
                .append("text")
                .attr("x", vm.x(vm.localIndex + 15))
                .attr("y", height - 8)
                .attr("dy", ".35em")
                .attr("class", "date")
                .text(date);
            }
          }
          index += 100;
        }

        vm.svg
          .append("line")
          .attr("class", "baseline")
          .attr("x1", vm.x(0))
          .attr("y1", h)
          .attr("x2", width)
          .attr("y2", h);

        vm.svg
          .append("text")
          .attr("x", -10)
          .attr("y", 5)
          .attr("dy", ".35em")
          .attr("class", "max")
          .text(max);

        vm.svg
          .append("line")
          .attr("class", "maxline")
          .attr("x1", vm.x(10))
          .attr("y1", 5)
          .attr("x2", width)
          .attr("y2", 5);
      }
    },
    addRect(xVal, yVal, w, h, cls) {
      let vm = this;
      vm.svg
        .append("rect")
        .attr("x", vm.x(xVal + vm.localIndex))
        .attr("y", vm.y(h))
        .attr("width", w)
        .attr("height", yVal - vm.y(h))
        .attr("class", cls);
    },
    getDates(startDate, count) {
      let dates = [];
      for (let i = 0; i < count; i++) {
        dates.push(
          moment(startDate)
            .add(i, "days")
            .format("YYYY-MM-DD")
        );
      }
      return dates;
    },
    getMax(values) {
      let max = 0;
      let keys = [
        "h800",
        "h830",
        "h900",
        "h930",
        "h1000",
        "h1130",
        "h1200",
        "h1230",
        "h1300",
        "h1330",
        "h1700",
        "h1730",
        "h1800",
        "h1830",
        "h1900",
        "car",
        "moto"
      ];
      for (let value of values) {
        for (let key of keys) {
          max = Math.max(max, value[key], value[key + "_u"]);
        }
      }
      return max;
    }
  }
};
</script>

<style lang="scss">
.graph.entity {
  &.dark {
    svg {
      .odd {
        fill: white;
      }
      .date {
        fill: white;
      }
      .baseline {
        stroke: #afb6bb;
      }
      .maxline {
        stroke: #afb6bb;
      }
      .sepline {
        stroke: #213d50;
      }
      .date,
      .max {
        fill: #b4c9e2;
      }
      .def {
        fill: white;
      }
    }
  }

  &.light {
    svg {
      .baseline {
        stroke: #546d8a;
      }
      .maxline {
        stroke: #92b6d4;
      }
      .sepline {
        stroke: #cce1e6;
      }
      .date,
      .max {
        fill: #546d8a;
      }
      .def {
        fill: black;
      }
    }
  }

  svg {
    .def {
      opacity: 0.2;
    }
    .on {
      // onsite
      fill: #55afde;
    }
    .arr {
      // arrival
      fill: #2e6a8a;
    }

    .rest {
      // arrival
      fill: #86d60b;
    }
    .dep {
      // arrival
      fill: orange;
    }
    .park {
      // arrival
      fill: rgb(161, 67, 204);
    }

    .date {
      font-size: 0.7rem;
    }

    .max {
      font-size: 0.5rem;
    }

    .maxline {
      stroke-dasharray: 4;
    }

    .odd {
      opacity: 0.05;
    }
  }
}
</style>
