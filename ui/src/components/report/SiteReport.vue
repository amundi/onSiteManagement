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
      y2: null,
      localIndex: 0,
      slotKeys: [
        "h700",
        "h700_u",
        "h730",
        "h730_u",
        "h800",
        "h800_u",
        "h830",
        "h830_u",
        "h900",
        "h900_u",
        "h930",
        "h930_u",
        "h1000",
        "h1000_u",
        "h1030",
        "h1030_u",
        "h1700",
        "h1700_u",
        "h1730",
        "h1730_u",
        "h1800",
        "h1800_u",
        "h1830",
        "h1830_u",
        "h1900",
        "h1900_u"
      ],
      onSiteKeys: ["onSite", "onSite_u", "offSite_u"]
    };
  },
  methods: {
    updateView(startDate, values) {
      let vm = this;
      if (startDate && values) {
        let dates = vm.getDates(startDate, vm.nb);
        let max = vm.getMax(values, vm.slotKeys);
        let maxOnSite = vm.getMax(values, vm.onSiteKeys);

        //         let mainWidth = this.$refs.main.getBoundingClientRect().width;
        let mainWidth = 700;
        let mainHeight = 300;

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

        let h = height - 160;
        let h2 = height - 25;
        vm.y = d3
          .scaleLinear()
          .domain([0, max])
          .range([h, 15]);

        vm.y2 = d3
          .scaleLinear()
          .domain([0, maxOnSite])
          .range([h2, h + 25]);

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
              vm.addRect(9, h, 10 * unit, val["h700"], "arr def");
              vm.addRect(12, h, 5 * unit, val["h700_u"], "arr usage");
              vm.addRect(15, h, 10 * unit, val["h730"], "arr def");
              vm.addRect(18, h, 5 * unit, val["h730_u"], "arr usage");
              vm.addRect(21, h, 10 * unit, val["h800"], "arr def");
              vm.addRect(24, h, 5 * unit, val["h800_u"], "arr usage");
              vm.addRect(27, h, 10 * unit, val["h830"], "arr def");
              vm.addRect(30, h, 5 * unit, val["h830_u"], "arr usage");
              vm.addRect(33, h, 10 * unit, val["h900"], "arr def");
              vm.addRect(36, h, 5 * unit, val["h900_u"], "arr usage");
              vm.addRect(39, h, 10 * unit, val["h930"], "arr def");
              vm.addRect(42, h, 5 * unit, val["h930_u"], "arr usage");
              vm.addRect(45, h, 10 * unit, val["h1000"], "arr def");
              vm.addRect(48, h, 5 * unit, val["h1000_u"], "arr usage");
              vm.addRect(51, h, 10 * unit, val["h1030"], "arr def");
              vm.addRect(54, h, 5 * unit, val["h1030_u"], "arr usage");

              vm.svg
                .append("line")
                .attr("class", "sepline")
                .attr("x1", vm.x(60 + vm.localIndex))
                .attr("y1", 0)
                .attr("x2", vm.x(60 + vm.localIndex))
                .attr("y2", h);

              // Departures -----------------------------------
              vm.addRect(63, h, 10 * unit, val["h1700"], "dep def");
              vm.addRect(66, h, 5 * unit, val["h1700_u"], "dep usage");
              vm.addRect(69, h, 10 * unit, val["h1730"], "dep def");
              vm.addRect(72, h, 5 * unit, val["h1730_u"], "dep usage");
              vm.addRect(75, h, 10 * unit, val["h1800"], "dep def");
              vm.addRect(78, h, 5 * unit, val["h1800_u"], "dep usage");
              vm.addRect(81, h, 10 * unit, val["h1830"], "dep def");
              vm.addRect(84, h, 5 * unit, val["h1830_u"], "dep usage");
              vm.addRect(87, h, 10 * unit, val["h1900"], "dep def");
              vm.addRect(90, h, 5 * unit, val["h1900_u"], "dep usage");

              vm.svg
                .append("text")
                .attr("x", vm.x(vm.localIndex + 15))
                .attr("y", height - 8)
                .attr("dy", ".35em")
                .attr("class", "date")
                .text(date);

              // On site ----------------------------------------
              vm.svg
                .append("rect")
                .attr("x", vm.x(vm.localIndex + 10))
                .attr("y", vm.y2(val["onSite"]))
                .attr("width", 25)
                .attr("height", h2 - vm.y2(val["onSite"]))
                .attr("class", "onSite def");
              vm.svg
                .append("rect")
                .attr("x", vm.x(vm.localIndex + 30))
                .attr("y", vm.y2(val["onSite_u"]))
                .attr("width", 25)
                .attr("height", h2 - vm.y2(val["onSite_u"]))
                .attr("class", "onSite usage");
              vm.svg
                .append("rect")
                .attr("x", vm.x(vm.localIndex + 50))
                .attr("y", vm.y2(val["offSite_u"]))
                .attr("width", 25)
                .attr("height", h2 - vm.y2(val["offSite_u"]))
                .attr("class", "offSite usage");
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
          .attr("x", vm.x(420))
          .attr("y", 5)
          .attr("dy", ".35em")
          .attr("class", "info")
          .text("arrivals / departures");

        vm.svg
          .append("text")
          .attr("x", -10)
          .attr("y", h +25)
          .attr("dy", ".35em")
          .attr("class", "max")
          .text(maxOnSite);

        vm.svg
          .append("line")
          .attr("class", "baseline")
          .attr("x1", vm.x(5))
          .attr("y1", h2)
          .attr("x2", width)
          .attr("y2", h2);

        vm.svg
          .append("line")
          .attr("class", "maxline")
          .attr("x1", vm.x(5))
          .attr("y1", h + 25)
          .attr("x2", width)
          .attr("y2", h + 25);

        vm.svg
          .append("text")
          .attr("x", vm.x(410))
          .attr("y", h + 15)
          .attr("dy", ".35em")
          .attr("class", "info")
          .text("limit / on site / off site");
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
      .max,
      .info {
        fill: #b4c9e2;
      }
      .onSite.def {
        opacity: 0.2;
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
      .dep {
        fill: #c78100;
      }
      .onSite.def {
        opacity: 1;
        fill: #c1d4de;
      }
    }
  }

  svg {
    .def {
      opacity: 0.1;
    }
    .onSite {
      // onsite
      fill: #55afde;
    }
    .offSite {
      fill: #a971b5;
    }
    .arr {
      // arrival
      fill: #2e6a8a;
    }

    .dep {
      // arrival
      fill: orange;
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
