<template>
  <v-container fluid class="pa-0 fill-height" style="align-items: flex-start">
    <!-- <v-row no-gutters> -->
    <v-col cols="12" class="pa-0">
      <v-card tile class="elevation-0 fill-height">
        <v-row class="pt-3 pl-4 pr-5">
          <v-col>
            <v-text-field
              class="pa-2 mt-0"
              style="height: 48px"
              v-model="search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
            ></v-text-field>
          </v-col>
          <v-col align="end" cols="5">
            <v-btn
              color="secondary"
              :disabled="selectedStation == null"
              @click="showModal()"
              >Show station details</v-btn
            >
          </v-col>
        </v-row>
        <div v-resize="onResize" ref="resizableDiv">
          <v-data-table
            :height="tableHeight"
            :headers="headers"
            :items="stations"
            :options.sync="options"
            class="elevation-0"
            :footer-props="footerProps"
            :loading="tableLoading"
            :search="search"
            @click:row="handleRowClick"
            :item-class="isSelected"
            :mobile-breakpoint="0"
            fixed-header
          >
          </v-data-table>
        </div>
      </v-card>
    </v-col>
    <!-- </v-row> -->
  </v-container>
</template>

<script lang="ts">
import { getSingleStation } from "@/helpers/api-functions";
import { SingleStationType, StationType } from "@/helpers/backend-data-types";
import { useStore } from "@/store";
import { defineComponent } from "vue";

export default defineComponent({
  name: "StationDataTable",

  props: ["clickedMarker"],

  data: () => ({
    headers: [
      {
        text: "ID",
        align: "start",
        value: "id",
      },
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
    search: "" as string,
    store: useStore(),
    stations: null as StationType[] | null,
    selectedStation: null as number | null,
    selectedStationDetails: null as SingleStationType | null,
    totalStations: null as number | null,
    tableHeight: null as number | null,
    tableLoading: false,
  }),

  watch: {
    clickedMarker(clickedMarker: { id: number; name: string }) {
      // Add station name to search to make sure
      // the station is also visible in the data table
      this.search = clickedMarker.name;
      this.getDetailsForId(clickedMarker.id);
    },
  },

  created() {
    this.stations = this.store.stations;
    this.totalStations = this.store.stations.length;
  },

  methods: {
    // This makes sure that the table fits perfectly with the map,
    // sizing the table exactly right. The 59px reduced from height is the height
    // of the table footer, allowing the footer to be displayed as well as it's not
    // included in the height of the table.
    onResize() {
      const resizableDiv = this.$refs.resizableDiv as HTMLElement;
      if (resizableDiv) {
        this.tableHeight =
          window.innerHeight - resizableDiv.getBoundingClientRect().y - 59;
      }
    },

    // Clicked row is stored as selected journey
    handleRowClick(station: StationType) {
      this.getDetailsForId(station.id);
    },

    // Marker click and row click both call this
    // function to get single station data
    getDetailsForId(stationId: number) {
      if (this.selectedStation == stationId) {
        this.selectedStation = null;
        this.selectedStationDetails = null;
        this.search = "";
        this.$emit("unselectedStation");
      } else {
        this.selectedStation = stationId;
        this.getSingleStationDetails(stationId);
      }
    },

    getSingleStationDetails(stationId: number) {
      getSingleStation(stationId).then((response) => {
        this.$emit("selectedStation", response.data);
      });
    },

    showModal() {
      this.$emit("showModal");
    },

    isSelected(item: StationType) {
      if (item.id == this.selectedStation) {
        return "selectedJourney";
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
