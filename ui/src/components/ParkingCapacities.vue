<template>
  <div class="capabilities" :class="[hue]">
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
  props: ["parking", "saveParking"],
  computed: {
    ...mapGetters(["hue"])
  },
  watch: {
    parking() {
      this.rowData = this.parking.capacities;
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
            width: 150,
            valueParser: this.dateParser,
            valueFormatter: this.dateFormatter
          },
          {
            headerName: "End date",
            field: "endDate",
            editable: true,
            sortable: true,
            width: 150,
            valueParser: this.dateParser,
            valueFormatter: this.dateFormatter
          }
        ]
      },
      {
        headerName: "Parking",
        children: [
          {
            headerName: "Car",
            field: "car",
            editable: true,
            width: 110,
            minwidth: 45,
            valueParser: this.numberParser
          },
          {
            headerName: "Motorcycle",
            field: "moto",
            editable: true,
            width: 110,
            minwidth: 45,
            valueParser: this.numberParser
          },
          {
            headerName: "Bike",
            field: "bike",
            editable: true,
            width: 110,
            minwidth: 45,
            valueParser: this.numberParser
          }
        ]
      }
    ];

    this.rowData = this.parking.capacities;
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
      return params.newValue != undefined ? moment(params.newValue).format("YYYY-MM-DD") : null;
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
        car: 0,
        moto: 0,
        bike: 0
      });
    }
  }
};
</script>
