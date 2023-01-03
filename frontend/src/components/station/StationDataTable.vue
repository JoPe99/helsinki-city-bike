<!-- Component includes filters and the data table -->
<template>
  <div class="fill-height" v-resize="onResize">
    <div class="fill-height" ref="resizableDiv">
      <v-data-table
        :height="tableHeight"
        :headers="headers"
        :items="stations"
        :options.sync="options"
        class="elevation-0"
        :footer-props="footerProps"
        :loading="tableLoading"
        @click:row="handleRowClick"
        :item-class="isSelected"
        :mobile-breakpoint="0"
        fixed-header
      >
      </v-data-table>
    </div>
  </div>
</template>

<script lang="ts">
import { SingleStationType, StationType } from "@/helpers/backend-data-types";
import { useStore } from "@/store";
import { defineComponent } from "vue";

export default defineComponent({
  name: "StationDataTable",
  components: {},

  data: () => ({
    headers: [
      {
        text: "Name (FI)",
        align: "start",
        value: "nameFi",
      },
      {
        text: "Address (FI)",
        align: "start",
        value: "addressFi",
      },
      {
        text: "Name (SE)",
        align: "start",
        value: "nameSe",
      },
      {
        text: "Address (SE)",
        align: "start",
        value: "addressSe",
      },
      {
        text: "Capacity",
        align: "start",
        value: "capacity",
      },
    ],
    options: {
      itemsPerPage: 25,
      page: 1,
      sortBy: ["nameFI"],
      sortDesc: [false],
    },
    footerProps: {
      "items-per-page-options": [15, 20, 25, 30, 50, 100],
      showFirstLastPage: true,
    },
    store: useStore(),
    stations: [] as StationType[],
    selectedStation: 0,
    totalStations: 0,
    tableHeight: 0,
    tableLoading: false,
  }),

  watch: {},

  mounted() {
    this.stations = this.store.stations;
    this.totalStations = this.store.stations.length;
  },

  methods: {
    // This makes sure that the table fits under filters and map,
    // sizing the table exactly right. The app bar is always 56px high.
    onResize() {
      const resizableDiv = this.$refs.resizableDiv as HTMLElement;
      if (resizableDiv) {
        this.tableHeight =
          window.innerHeight - resizableDiv.getBoundingClientRect().y - 56;
      }
    },
    // If row is selected, apply class to it
    isSelected(item: StationType) {
      if (item.id == this.selectedStation) {
        return "selectedJourney";
      }
    },
    // Clicked row is stored as selected journey
    handleRowClick(item: StationType) {
      if (this.selectedStation == item.id) {
        this.selectedStation = 0;
        this.$emit("unselectedStation");
      } else {
        this.selectedStation = item.id;
        this.$emit("selectedStation", item);
      }
    },
  },
});
</script>

<!-- Doesn't work if scoped -->
<style>
.selectedStation {
  filter: brightness(50%);
}
</style>
