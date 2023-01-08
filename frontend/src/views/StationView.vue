<template>
  <v-container fluid class="pa-0 fill-height">
    <single-station-modal
      :modal="showModal"
      :station="selectedStation"
      @modalClosed="handleModalClosed"
    ></single-station-modal>
    <!-- Default layout for desktops and larger viewports -->
    <v-row v-if="$vuetify.breakpoint.lgAndUp" class="fill-height" no-gutters>
      <v-col class="py-0 fill-height" cols="6">
        <station-data-table
          class="fill-height"
          @selectedStation="stationSelected"
          @unselectedStation="stationUnselected"
          @showModal="handleShowModal"
          :clickedMarker="clickedMarker"
        >
        </station-data-table>
      </v-col>
      <v-col class="pa-0 fill-height" cols="6">
        <map-component
          :markers="markers"
          @markerClicked="handleClickedMarker"
          class="fill-height"
        ></map-component>
      </v-col>
    </v-row>
    <!-- Vertical layout in case of a smaller viewport -->
    <v-col v-else class="pa-0 fill-height">
      <map-component
        :markers="markers"
        @markerClicked="handleClickedMarker"
        style="height: 500px"
      ></map-component>
      <v-row no-gutters>
        <v-col>
          <station-data-table
            class="fill-height"
            @selectedStation="stationSelected"
            @unselectedStation="stationUnselected"
            @showModal="handleShowModal"
            :clickedMarker="clickedMarker"
          >
          </station-data-table>
        </v-col>
      </v-row>
    </v-col>
  </v-container>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import MapComponent from "../components/MapComponent.vue";
import StationDataTable from "@/components/station/StationDataTable.vue";
import SingleStationModal from "@/components/station/SingleStationModal.vue";

import { StationLocation } from "@/helpers/list-view-helpers";
import { SingleStationType } from "@/helpers/backend-data-types";
import { useStore } from "@/store";

export default defineComponent({
  name: "StationView",
  components: {
    MapComponent,
    StationDataTable,
    SingleStationModal,
  },

  data: () => ({
    store: useStore(),
    allStationMarkers: [] as StationLocation[],
    markers: [] as StationLocation[],
    selectedStation: null as SingleStationType | null,
    clickedMarker: {} as { id: number; name: string },
    showModal: false,
  }),

  mounted() {
    this.allStationMarkers = this.store.allStationMarkers;
    this.markers = this.allStationMarkers;
  },

  methods: {
    stationUnselected() {
      this.markers = this.allStationMarkers;
      this.selectedStation = null;
    },
    stationSelected(station: SingleStationType) {
      this.selectedStation = station;
      let id = station.id;
      let name = station.nameFi;
      let latLong = [Number(station.latitude), Number(station.longitude)];
      this.markers = [{ id: id, name: name, latLong: latLong }];
    },
    handleClickedMarker(clickedMarker: { id: number; name: string }) {
      this.clickedMarker = clickedMarker;
    },
    handleShowModal() {
      this.showModal = true;
    },
    handleModalClosed() {
      this.showModal = false;
    },
  },
});
</script>
