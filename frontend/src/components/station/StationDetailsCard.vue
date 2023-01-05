<template>
  <v-card class="fill-height elevation-0">
    <div v-if="stationSelected" class="fill-height">
      <v-container>
        <v-row>
          <v-col>
            <v-card class="elevation-4" color="primary">
              <v-card-title>
                Station information:
                {{ station.nameFi }} (ID: {{ station.id }})</v-card-title
              >
            </v-card>
          </v-col>
        </v-row>
        <!-- Total journeys and average distance/duration-->
        <v-row class="mt-0">
          <v-col>
            <v-card class="elevation-4 fill-height" color="primary">
              <v-card-title>
                Station has {{ station.totalDepartJourneys }} departures and
                {{ station.totalReturnJourneys }} returns
              </v-card-title>
              <v-card-subtitle>
                Average depart journey was
                {{ formatDistance(station.averageDepartDistance) }} and average
                return journey was
                {{ formatDistance(station.averageReturnDistance) }}. Average
                duration for depart journey was
                {{ formatDuration(station.averageDepartDuration) }} and for
                return journey average duration was
                {{ formatDuration(station.averageReturnDuration) }}.
              </v-card-subtitle>
            </v-card>
          </v-col>
        </v-row>
        <!-- Top stations to depart to -->
        <v-row>
          <v-col>
            <v-card class="elevation-4" color="primary">
              <v-row>
                <v-col class="px-8">
                  <v-card-title class="pa-0 pb-3"
                    >Top departure stations</v-card-title
                  >
                  <top-stations-table
                    class="elevation-4"
                    :topStations="station.topDepartStations"
                  >
                  </top-stations-table>
                </v-col>
                <v-col class="px-8">
                  <v-card-title class="pa-0 pb-3"
                    >Top return stations</v-card-title
                  >
                  <top-stations-table
                    class="elevation-4"
                    :topStations="station.topReturnStations"
                  >
                  </top-stations-table>
                </v-col>
              </v-row>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
      <!-- Top stations to return from -->
    </div>
    <div
      v-else
      class="fill-height d-flex flex-column align-center justify-center"
      style="text-align-last: center"
    >
      <v-card-title class="d-flex align-center">
        No station selected
      </v-card-title>
      <v-card-subtitle class="d-flex align-center">
        Select a station from the map or the list to see awesome details!
      </v-card-subtitle>
    </div>
  </v-card>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { formatDistance, formatSeconds } from "@/helpers/list-view-helpers";
import TopStationsTable from "./TopStationsTable.vue";

export default defineComponent({
  name: "StationDetailsCard",
  props: ["station"],

  components: { TopStationsTable },

  data: () => ({}),

  computed: {
    stationSelected() {
      if (this.$props.station == null) {
        return false;
      } else {
        return true;
      }
    },
  },
  methods: {
    formatDistance(distance: number) {
      return formatDistance(distance) as string;
    },
    formatDuration(duration: number) {
      return formatSeconds(duration) as string;
    },
  },
});
</script>
