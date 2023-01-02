<template>
  <v-container fluid class="pa-0 fill-height">
    <!-- Default layout for desktops and larger viewports -->
    <v-row v-if="$vuetify.breakpoint.lgAndUp" class="pa-0 fill-height">
      <v-col cols="6" lg="7" xl="5" class="pa-0 fill-height">
        <journey-data-table
          @unselectedJourney="journeyUnselected"
          @selectedJourney="journeySelected"
        ></journey-data-table>
      </v-col>
      <v-col cols="6" lg="5" xl="7" class="pa-0 fill-height">
        <map-component :markers="markers" class="fill-height" />
      </v-col>
    </v-row>
    <!-- Vertical layout in case of a smaller viewport -->
    <v-col v-if="$vuetify.breakpoint.mdAndDown" class="pa-0 fill-height">
      <map-component :markers="markers" style="height: 500px" />
      <journey-data-table
        @unselectedJourney="journeyUnselected"
        @selectedJourney="journeySelected"
      ></journey-data-table>
    </v-col>
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
    // Clear markers from map when journey unselected
    journeyUnselected() {
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
