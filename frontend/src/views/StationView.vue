<template>
  <v-container fluid class="pa-0 fill-height">
    <v-row v-if="true" class="pa-0 fill-height" no-gutters>
      <v-col cols="6" lg="7" xl="5" class="pa-0 fill-height">
        <v-row no-gutters class="pa-0">
          <v-col cols="12" class="pa-0">
            <station-details-card
              :station="selectedStation"
              style="height: 480px"
            ></station-details-card>
          </v-col>
        </v-row>
        <v-row class="pa-0 mt-0 fill-height">
          <v-col cols="12" class="pt-0 fill-height">
            <station-data-table
              @selectedStation="stationSelected"
              @unselectedStation="stationUnselected"
            ></station-data-table>
          </v-col>
        </v-row>
      </v-col>
      <v-col cols="6" lg="5" xl="7" class="pa-0 fill-height">
        <map-component :markers="markers" class="fill-height" />
      </v-col>
    </v-row>
    <!-- Default layout for desktops and larger viewports -->
    <v-row
      v-else-if="$vuetify.breakpoint.lgAndUp"
      class="pa-0 pt-1 fill-height"
      no-gutters
    >
      <v-col cols="6" lg="7" xl="5" class="pa-0 fill-height">
        <v-row no-gutters class="pa-0">
          <v-col cols="12" class="pa-0">
            <station-details-card
              :station="selectedStation"
              style="height: 480px"
            ></station-details-card>
          </v-col>
        </v-row>
        <v-row class="pa-0 mt-0 fill-height">
          <v-col cols="12" class="pt-0 fill-height">
            <station-data-table
              @selectedStation="stationSelected"
              @unselectedStation="stationUnselected"
            ></station-data-table>
          </v-col>
        </v-row>
      </v-col>
      <v-col cols="6" lg="5" xl="7" class="pa-0 fill-height">
        <map-component :markers="markers" class="fill-height" />
      </v-col>
    </v-row>
    <!-- Vertical layout in case of a smaller viewport -->
    <v-col v-if="$vuetify.breakpoint.mdAndDown" class="pa-0 fill-height">
      <map-component :markers="markers" style="height: 500px" />
      <v-row no-gutters>
        <v-col cols="6">
          <station-data-table
            @selectedStation="stationSelected"
            @unselectedStation="stationUnselected"
          ></station-data-table>
        </v-col>
        <v-col cols="6">
          <station-details-card
            :station="selectedStation"
            class="fill-height"
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
import { SingleStationType, StationType } from "@/helpers/backend-data-types";
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
