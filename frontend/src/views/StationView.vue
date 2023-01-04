<template>
  <v-container class="pa-0" fluid>
    <map-component :markers="markers" style="height: 500px" />
    <station-data-table></station-data-table>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import MapComponent from "../components/MapComponent.vue";
import StationDataTable from "@/components/station/StationDataTable.vue";
import { StationLocation } from "@/helpers/list-view-helpers";
import { useStore } from "@/store";

export default Vue.extend({
  name: "StationView",
  components: { MapComponent, StationDataTable },

  data: () => ({
    store: useStore(),
    allStationMarkers: [] as StationLocation[],
    markers: [] as StationLocation[],
  }),

  mounted() {
    console.log(this.store.allStationMarkers);
    this.allStationMarkers = this.store.allStationMarkers;
    this.markers = this.allStationMarkers;
  },

  methods: {},
});
</script>
