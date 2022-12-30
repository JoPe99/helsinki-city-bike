<template>
  <v-container class="pa-0" fluid height="100%">
    <map-component :markers="markers" style="height: 500px" />
    <journey-data-table
      @unselectedJourney="journeyRemoved"
      @selectedJourney="journeySelected"
    ></journey-data-table>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import MapComponent from "@/components/MapComponent.vue";
import JourneyDataTable from "@/components/journey/JourneyDataTable.vue";
import { FormattedJourneyType } from "@/helpers/backend-data-types";
import { StationLocation } from "@/helpers/list-view-helpers";

export default Vue.extend({
  name: "JourneyListView",
  components: { MapComponent, JourneyDataTable },

  data: () => ({
    markers: [] as StationLocation[],
  }),

  methods: {
    // Empties markers
    journeyRemoved() {
      console.log("Removed");
      this.markers = [];
    },

    // Get departure/return locations from the
    // item, add locations to markers
    journeySelected(item: FormattedJourneyType) {
      let departureStation: StationLocation = {
        id: item.departureStationId,
        name: item.departureStationName,
        latLong: [item.departureStationY, item.departureStationX],
      };

      let returnStation: StationLocation = {
        id: item.returnStationId,
        name: item.returnStationName,
        latLong: [item.returnStationY, item.returnStationX],
      };

      this.markers = [departureStation, returnStation];
      console.log(this.markers);
    },
  },
});
</script>
