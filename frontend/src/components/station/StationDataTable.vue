<template>
  <div class="fill-height" v-resize="onResize">
    <div class="fill-height" ref="resizableDiv">
      <v-container fluid class="pa-0">
        <v-row no-gutters>
          <v-col cols="8">
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
          </v-col>
          <v-col cols="4">
            <station-details-card
              :station="selectedStationDetails"
            ></station-details-card>
          </v-col>
        </v-row>
      </v-container>
    </div>
  </div>
</template>

<script lang="ts">
import { getSingleStation } from "@/helpers/api-functions";
import { SingleStationType, StationType } from "@/helpers/backend-data-types";
import { useStore } from "@/store";
import { defineComponent } from "vue";

import StationDetailsCard from "./StationDetailsCard.vue";

export default defineComponent({
  name: "StationDataTable",
  components: { StationDetailsCard },

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
      sortBy: ["nameFi"],
      sortDesc: [false],
    },
    footerProps: {
      "items-per-page-options": [15, 20, 25, 30, 50, 100],
      showFirstLastPage: true,
    },
    store: useStore(),
    stations: [] as StationType[],
    selectedStation: null as number | null,
    selectedStationDetails: null as SingleStationType | null,
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
    handleRowClick(station: StationType) {
      if (this.selectedStation == station.id) {
        this.selectedStation = null;
        this.selectedStationDetails = null;
        this.$emit("unselectedStation");
      } else {
        this.selectedStation = station.id;
        this.getSingleStationDetails(station.id);
        this.$emit("selectedStation", station);
      }
    },
    getSingleStationDetails(stationId: number) {
      getSingleStation(stationId).then((response) => {
        this.selectedStationDetails = response.data;
      });
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
