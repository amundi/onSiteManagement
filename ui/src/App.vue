<template>
  <div id="app" :class="[hue]">
    <NavBar></NavBar>
    <div class="container mx-auto flex">
      <LeftMenu></LeftMenu>
      <router-view />
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import store from "@/store/index";
import NavBar from "@/components/NavBar";
import LeftMenu from "@/components/LeftMenu";

export default {
  components: { NavBar, LeftMenu },
  store,
  computed: {
    ...mapGetters(["hue"])
  },
  mounted() {
    store.dispatch("reloadAll");
  }
};
</script>

<style lang="scss">
@import "@/scss/_variables.scss";
@import "../node_modules/ag-grid-community/dist/styles/ag-grid.css";
@import "../node_modules/ag-grid-community/dist/styles/ag-theme-alpine.css";
@import "../node_modules/ag-grid-community/dist/styles/ag-theme-alpine-dark.css";

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;

  &.dark {
    background-color: #242424;
    color: white;

    input {
      color: #242424;
      background-color: #cecece !important;
      border-color: #2b2b2b;
      &:focus {
        border-color: $primaryColor;
      }
    }

    .v-select {
      &:not(.white) {
        div[role="combobox"] {
          background-color: #242424;
          border-color: #737373;
          input {
            background-color: #242424 !important;
            color: #cecece;
            border-color: transparent;
          }
          .vs__selected {
            color: white;
          }
          .vs__clear,
          .vs__open-indicator {
            fill: #737373;
          }
        }
      }
      &.white {
        div[role="combobox"] {
          background-color: #cecece;
          input {
            background-color: #cecece !important;
            color: #2b2b2b;
            border-color: transparent;
          }
        }
      }
    }

    ::-webkit-scrollbar {
      width: 6px;
      height: 6px;
    }

    ::-webkit-scrollbar-thumb {
      border-radius: 3px;
      background: #333;
    }

    ::-webkit-scrollbar-thumb:hover {
      background: #222;
    }

    ::-webkit-scrollbar-track {
      -webkit-box-shadow: inset 0 0 6px rgba(255, 255, 255, 0.05);
      box-shadow: inset 0 0 6px rgba(255, 255, 255, 0.05);
      border-radius: 10px;
    }

    ::-webkit-scrollbar-corner {
      background-color: rgba(255, 0, 0, 0);
    }
  }

  &.light {
    background-color: white;
    color: #242424;

    ::-webkit-scrollbar {
      width: 6px;
      height: 6px;
    }

    ::-webkit-scrollbar-thumb {
      border-radius: 3px;
      background: #ccc;
    }

    ::-webkit-scrollbar-thumb:hover {
      background: #bbb;
    }

    ::-webkit-scrollbar-track {
      -webkit-box-shadow: inset 0 0 6px rgba(255, 255, 255, 0.05);
      box-shadow: inset 0 0 6px rgba(255, 255, 255, 0.05);
      border-radius: 10px;
    }

    ::-webkit-scrollbar-corner {
      background-color: rgba(255, 0, 0, 0);
    }
  }

  .capabilities {
    &.dark {
      border-color: #464646;
      .ag-root-wrapper {
        border-color: #464646;
      }
      .ag-header {
        .ag-header-cell,
        .ag-header-group-cell {
          border-color: #464646 !important;
        }
      }
      .footer {
        background-color: #2b2b2b;
        border-color: #464646;
      }
    }

    &.light {
      border-color: #cacaca;
      .ag-header {
        .ag-header-cell,
        .ag-header-group-cell {
          border-color: #cacaca !important;
        }
      }
      .footer {
        background-color: #efefef;
        border-color: #cacaca;
      }
    }

    // ::-webkit-scrollbar {
    //   display: none;
    // }

    .ag-root-wrapper {
      font-family: Avenir, Helvetica, Arial, sans-serif;
      border-radius: 0 !important;
      font-size: 0.7rem;
      .ag-header {
        .ag-header-row {
          height: 20px;
        }

        .ag-header-cell,
        .ag-header-group-cell {
          padding: 0 10px;
          border-right: 1px solid;
        }
      }
      .ag-cell {
        font-size: 0.7rem;
        text-align: left;
      }
      .ag-row {
        border-top-color: transparent;
      }
    }

    .footer {
      border-left: 1px solid;
      border-bottom: 1px solid;
      border-right: 1px solid;
      border-bottom-left-radius: 6px;
      border-bottom-right-radius: 6px;
      padding: 0 5px;
      font-size: 0.8rem;

      .button {
        padding: 3px;
        cursor: pointer;
      }
    }
  }

  .vdpArrowPrev:after {
    border-right-color: #049fce;
  }

  .vdpArrowNext:after {
    border-left-color: #049fce;
  }

  .vdpCell.selectable:hover .vdpCellContent,
  .vdpCell.selected .vdpCellContent {
    background: #049fce;
  }

  .vdpCell.today {
    color: #049fce;
  }

  .vdpTimeUnit > input:hover,
  .vdpTimeUnit > input:focus {
    border-bottom-color: #049fce;
  }
}
</style>
