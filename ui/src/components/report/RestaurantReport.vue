<template>
  <div ref="main" class="graph restaurant flex justify-center" :class="[hue]" :id="name + '-restaurant'"></div>
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
      y2: null,
      localIndex: 0,
      keys: [
        "h1130",
        "h1130_u",
        "h1200",
        "h1200_u",
        "h1230",
        "h1230_u",
        "h1300",
        "h1300_u",
        "h1330",
        "h1330_u"
      ]
    };
  },
  methods: {
    updateView(startDate, values) {
      let vm = this;
      if (startDate && values) {
        let dates = vm.getDates(startDate, vm.nb);
        let max = vm.getMax(values, vm.keys);

        let mainWidth = 700;
        let mainHeight = 140;

        let margin = { top: 10, right: 10, bottom: 10, left: 10 },
          width = mainWidth - margin.left - margin.right,
          height = mainHeight - margin.top - margin.bottom;

        // Empty and setup svg -------------------------------------------
        d3.select("#" + vm.name + "-restaurant")
          .selectAll("svg")
          .remove();

        vm.svg = d3
          .select("#" + vm.name + "-restaurant")
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

        let h = height - 15;
       
        vm.y = d3
          .scaleLinear()
          .domain([0, max])
          .range([h, 15]);

        let index = 0;
        for (let date of dates) {
          if (index % 200 == 0) {
            vm.svg
              .append("rect")
              .attr("x", vm.x(index))
              .attr("y", 0)
              .attr("width", vm.x(index + 100) - vm.x(index))
              .attr("height", height)
              .attr("class", "odd");
          }

          // Values
          for (let val of values) {
            if (date == val.date) {
              vm.localIndex = index;

              // Arrivals ------------------------------------
              vm.addRect(10, h, 20 * unit, val["h1130"], "def");
              vm.addRect(17, h, 10 * unit, val["h1130_u"], "usage");
              vm.addRect(24, h, 20 * unit, val["h1200"], "def");
              vm.addRect(31, h, 10 * unit, val["h1200_u"], "usage");
              vm.addRect(38, h, 20 * unit, val["h1230"], "def");
              vm.addRect(45, h, 10 * unit, val["h1230_u"], "usage");
              vm.addRect(52, h, 20 * unit, val["h1300"], "def");
              vm.addRect(59, h, 10 * unit, val["h1300_u"], "usage");
              vm.addRect(66, h, 20 * unit, val["h1330"], "def");
              vm.addRect(73, h, 10 * unit, val["h1330_u"], "usage");
          
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
          .attr("y", 15)
          .attr("dy", ".35em")
          .attr("class", "max")
          .text(max);

        vm.svg
          .append("line")
          .attr("class", "maxline")
          .attr("x1", vm.x(5))
          .attr("y1", 15)
          .attr("x2", width)
          .attr("y2", 15);

        vm.svg
          .append("text")
          .attr("x", vm.x(450))
          .attr("y", 5)
          .attr("dy", ".35em")
          .attr("class", "info")
          .text("restaurants");
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
    getMax(values, keys) {
      let max = 0;
      for (let value of values) {
        for (let key of keys) {
          max = Math.max(max, value[key]);
        }
      }
      return max;
    }
  }
};
</script>

<style lang="scss">
.graph.restaurant {
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
      .max,
      .info {
        fill: #b4c9e2;
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
      .max,
      .info {
        fill: #546d8a;
      }
    }
  }

  svg {
    .def {
      opacity: 0.2;
    }
  
    .usage, .def {
      fill: #2e6a89;
    }

    .date,
    .info {
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
