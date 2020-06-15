<template>
  <div class="capabilities center-panel" :class="[hue]">
    <ag-grid-vue
      style="height: 500px;"
      :class="{
        'ag-theme-alpine': hue == 'light',
        'ag-theme-alpine-dark': hue == 'dark',
        'w-full': true
      }"
      :columnDefs="columnDefs"
      :rowData="rowData"
      rowSelection="multiple"
      @grid-ready="onGridReady"
    ></ag-grid-vue>
    <div class="footer flex justify-end">
      <div class="button plus" @click="createRow()">
        <font-awesome-icon icon="plus"></font-awesome-icon>
      </div>
      <div class="button minus" @click="getSelectedRows()">
        <font-awesome-icon icon="minus"></font-awesome-icon>
      </div>
    </div>
  </div>
</template>

<script>
import { AgGridVue } from "ag-grid-vue";
import { mapGetters } from "vuex";
import moment from "moment";

export default {
  props: ["site", "saveSite"],
  computed: {
    ...mapGetters(["hue"])
  },
  watch: {
    site() {
      this.rowData = this.site.capacities;
    }
  },
  data() {
    return {
      columnDefs: null,
      rowData: null
    };
  },
  components: {
    AgGridVue
  },
  beforeMount() {
    this.columnDefs = [
      {
        headerName: "Date",
        children: [
          {
            headerName: "Start date",
            field: "startDate",
            editable: true,
            sortable: true,
            width: 100,
            valueParser: this.dateParser,
            valueFormatter: this.dateFormatter
          },
          {
            headerName: "End date",
            field: "endDate",
            editable: true,
            sortable: true,
            width: 100,
            valueParser: this.dateParser,
            valueFormatter: this.dateFormatter
          }
        ]
      },
      {
        headerName: "Presence",
        children: [
          {
            headerName: "",
            field: "onSite",
            editable: true,
            width: 70,
            minwidth: 60,
            valueParser: this.numberParser
          }
        ]
      },
      {
        headerName: "Arrival",
        children: [
          {
            headerName: "7:00",
            field: "arrival_700",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          },
          {
            headerName: "7:30",
            field: "arrival_730",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          },
          {
            headerName: "8:00",
            field: "arrival_800",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          },
          {
            headerName: "8:30",
            field: "arrival_830",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          },
          {
            headerName: "9:00",
            field: "arrival_900",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          },
          {
            headerName: "9:30",
            field: "arrival_930",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          },
          {
            headerName: "10:00",
            field: "arrival_1000",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          },
          {
            headerName: "10:30",
            field: "arrival_1030",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          }
        ]
      },
      {
        headerName: "Departure",
        children: [
          {
            headerName: "17:00",
            field: "departure_1700",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          },
          {
            headerName: "17:30",
            field: "departure_1730",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          },
          {
            headerName: "18:00",
            field: "departure_1800",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          },
          {
            headerName: "18:30",
            field: "departure_1830",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          },
          {
            headerName: "19:00",
            field: "departure_1900",
            editable: true,
            width: 50,
            minwidth: 40,
            valueParser: this.numberParser
          }
        ]
      }
    ];

    this.rowData = this.site.capacities;
  },
  methods: {
    onGridReady(params) {
      this.gridApi = params.api;
      this.columnApi = params.columnApi;
      this.gridApi.sizeColumnsToFit();
    },
    getSelectedRows() {
      const selectedNodes = this.gridApi.getSelectedNodes();
      selectedNodes.forEach(node => {
        this.rowData.splice(this.rowData.indexOf(node.data), 1);
      });
    },
    numberParser(params) {
      return Number(params.newValue);
    },
    dateParser(params) {
      return params.newValue != undefined
        ? moment(params.newValue).format("YYYY-MM-DD")
        : null;
    },
    dateFormatter(params) {
      return params.value ? moment(params.value).format("DD/MM/YYYY") : null;
    },
    createRow() {
      let tomorrow = moment()
        .add(1, "days")
        .format("YYYY-MM-DD");
      this.rowData.push({
        startDate: tomorrow,
        endDate: tomorrow,
        onSite: 0,
        arrival_700: 0,
        arrival_730: 0,
        arrival_800: 0,
        arrival_830: 0,
        arrival_900: 0,
        arrival_930: 0,
        arrival_1000: 0,
        arrival_1030: 0,
        departure_1700: 0,
        departure_1730: 0,
        departure_1800: 0,
        departure_1830: 0,
        departure_1900: 0
      });
    }
  }
};
</script>
