<template>
  <v-container fluid class="pa-0 fill-height">
    <!-- Default layout for desktops and larger viewports -->
    <v-row v-if="$vuetify.breakpoint.lgAndUp" class="fill-height" no-gutters>
      <v-col class="py-0 fill-height" cols="6">
        <station-details-card
          :station="selectedStation"
          style="height: 480px"
        ></station-details-card>
        <station-data-table
          class="fill-height"
          @selectedStation="stationSelected"
          @unselectedStation="stationUnselected"
        >
        </station-data-table>
      </v-col>
      <v-col class="py-0 fill-height" cols="6">
        <map-component :markers="markers" class="fill-height"></map-component>
      </v-col>
    </v-row>
    <v-col v-else class="pa-0 fill-height">
      <map-component :markers="markers" style="height: 500px"></map-component>
      <v-row no-gutters>
        <v-col cols="6">
          <station-data-table
            class="fill-height"
            @selectedStation="stationSelected"
            @unselectedStation="stationUnselected"
          >
          </station-data-table>
        </v-col>
        <v-col cols="6">
          <station-details-card
            :station="selectedStation"
          ></station-details-card>
        </v-col>
      </v-row>
    </v-col>
  </v-container>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import MapComponent from "../components/MapComponent.vue";
import StationDataTable from "@/components/station/StationDataTable.vue";
import StationDetailsCard from "@/components/station/StationDetailsCard.vue";

import { StationLocation } from "@/helpers/list-view-helpers";
import { SingleStationType } from "@/helpers/backend-data-types";
import { useStore } from "@/store";

export default defineComponent({
  name: "StationView",
  components: { MapComponent, StationDataTable, StationDetailsCard },

  data: () => ({
    store: useStore(),
    allStationMarkers: [] as StationLocation[],
    markers: [] as StationLocation[],
    selectedStation: null as SingleStationType | null,
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
  },
});
</script>
