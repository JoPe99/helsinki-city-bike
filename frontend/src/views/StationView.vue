<template>
  <v-container class="pa-0" fluid>
    <map-component :markers="markers" style="height: 500px" />
    <station-data-table
      @selectedStation="stationSelected"
      @unselectedStation="stationUnselected"
    ></station-data-table>
  </v-container>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import MapComponent from "../components/MapComponent.vue";
import StationDataTable from "@/components/station/StationDataTable.vue";
import { StationLocation } from "@/helpers/list-view-helpers";
import { StationType } from "@/helpers/backend-data-types";
import { useStore } from "@/store";

export default defineComponent({
  name: "StationView",
  components: { MapComponent, StationDataTable },

  data: () => ({
    store: useStore(),
    allStationMarkers: [] as StationLocation[],
    markers: [] as StationLocation[],
  }),

  mounted() {
    this.allStationMarkers = this.store.allStationMarkers;
    this.markers = this.allStationMarkers;
  },

  methods: {
    stationUnselected() {
      this.markers = this.allStationMarkers;
    },
    stationSelected(station: StationType) {
      let id = station.id;
      let name = station.nameFi;
      let latLong = [Number(station.latitude), Number(station.longitude)];

      this.markers = [{ id: id, name: name, latLong: latLong }];
    },
  },
});
</script>
