<template>
  <div class="home w-full flex" :class="[hue]">
    <div class="center-panel w-full">
      <div class="no-info flex justify-end" v-if="!selectedSite">
        <div class="main w-full">
          <div class="title flex justify-between">
            <div>
              <span class="post">#POST</span>
              <span class="covid">COVID</span>
            </div>
            <div class="arrow-right"></div>
          </div>
          <div class="content text-left">
            <font-awesome-icon icon="lightbulb"></font-awesome-icon>
            <span>First, select your working site to start your registration.</span>
          </div>
        </div>
      </div>
      <div class="months" v-if="startsOfMonth.length > 0">
        <div v-for="startOfMonth of startsOfMonth" :key="startOfMonth">
          <CalendarEditor :startOfMonth="startOfMonth"></CalendarEditor>
        </div>
      </div>
    </div>
    <div class="right-panel select-none overflow-y-auto">
      <div class="title"><font-awesome-icon icon="building"></font-awesome-icon>&nbsp;Site</div>
      <v-select
        :value="selectedSite"
        :options="siteDefinitions"
        label="name"
        @input="setSelectedSite"
      >
        <template #option="option">
          <h3 style="margin: 0">{{ option.name }}</h3>
          <div class="description">{{ option.description }}</div>
          <div class="tag">{{ option.path }}</div>
        </template>
      </v-select>
      <div class="booking-panel-wrapper">
        <BookingPanel></BookingPanel>
      </div>
    </div>
  </div>
</template>

<script>
import CalendarEditor from "@/components/CalendarEditor.vue";
import BookingPanel from "@/components/BookingPanel.vue";

import { mapGetters } from "vuex";
import store from "../store/index";
import moment from "moment";

export default {
  name: "Home",
  store,
  components: {
    CalendarEditor,
    BookingPanel
  },
  computed: {
    ...mapGetters(["hue", "siteDefinitions", "selectedSite", "siteUsages"]),
    startsOfMonth() {
      let startsOfMonth = [];
      for (let siteUsage of this.siteUsages) {
        let startOfMonth = moment(siteUsage.date)
          .startOf("month")
          .format("YYYY-MM-DD");
        if (startsOfMonth.indexOf(startOfMonth) < 0) {
          startsOfMonth.push(startOfMonth);
        }
      }
      startsOfMonth.sort();
      return startsOfMonth;
    }
  },
  watch: {
    siteDefinitions(val) {
      this.updateListSites(val);
    }
  },
  data() {
    return {
      listSites: null
    };
  },
  beforeMount() {
    if (this.siteDefinitions) {
      this.updateListSites(this.siteDefinitions);
    }
  },
  methods: {
    updateListSites(sites) {
      if (sites) {
        this.listSites = [];
        sites.forEach(site => {
          this.listSites.push({
            code: site.id,
            label: site.name
          });
        });
      }
    },
    setSelectedSite(val) {
      store.dispatch("changeSelectedSite", val);
    }
  }
};
</script>

<style lang="scss" scoped>
.home {
  &.dark {
    .center-panel {
      .no-info {
        .main {
          background-color: #313131;
          .post {
            color: #00b6ed;
          }
          .arrow-right {
            border-left: 20px solid #313131;
          }
        }
      }
      .months {
        background-color: #1d1d1d;
      }
    }
    .right-panel {
      border-color: #464646;
      .title {
        border-bottom: 1px solid #464646;
      }
    }

    .item.selected {
      background-color: #2d2d2d;
    }
  }

  &.light {
    .center-panel {
      .no-info {
        .main {
          background-color: #ececec;
          .post {
            color: #00b6ed;
          }
          .arrow-right {
            border-left: 20px solid #ececec;
          }
        }
      }
      .months {
        background-color: #f5f5f5;
      }
    }
    .right-panel {
      border-color: #cacaca;
      .title {
        border-bottom: 1px solid #cacaca;
      }
    }

    .item.selected {
      background-color: #f1f1f1;
    }
  }

  div.description {
    font-size: 0.6rem;
    color: #0c6498;
  }

  div.tag {
    font-size: 0.7rem;
    font-weight: bold;
    color: #0c6498;
  }

  height: 100vh;

  .center-panel {
    // overflow-y: overlay;;
    .no-info {
      margin-top: 110px;
      margin-right: 10px;
      margin-left: 20px;
      .main {
        padding: 30px 0 30px 30px;
        z-index: 1;
        border-radius: 8px;
        .title {
          text-align: left;
          font-size: 1.5rem;
          letter-spacing: -2px;
          .post {
            font-weight: bold;
          }
        }
        .content {
          font-size: 0.9rem;
          svg {
            font-size: 1.2rem;
            margin-right: 10px;
            color: #d8a718;
          }
        }
        .arrow-right {
          margin-left: 30px;
          margin-right: -20px;
          height: 40px;
          border-top: 20px solid transparent;
          border-bottom: 20px solid transparent;
        }
      }
    }

    .months {
      height: 100vh;
      padding: 150px 0px 10px 0px;
      overflow-y: auto;
    }
  }

  .right-panel {
    width: 350px;
    margin-top: 100px;
    border-left: 1px solid;
    .title {
      text-align: left;
      padding: 5px 10px;
      font-size: 0.8rem;
      font-weight: bold;
      margin-bottom: 15px;
    }
    .v-select {
      margin-left: 15px;
      font-size: 0.9rem;
    }
  }
}
</style>
