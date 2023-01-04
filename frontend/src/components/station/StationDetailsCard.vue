<template>
  <v-card class="fill-height elevation-0">
    <div v-if="stationSelected">
      <v-container>
        <v-row>
          <!-- Addresses in all languages -->
          <v-col>
            <v-card class="elevation-4" color="primary">
              <v-card-title>
                Station information ID {{ station.id }}:
                {{ station.nameFi }}</v-card-title
              >
              <v-card-subtitle
                >SE: {{ station.nameSe }} EN: {{ station.nameEn }}
              </v-card-subtitle>
            </v-card>
          </v-col>
        </v-row>
        <!-- Total journeys and average distance/duration-->
        <v-row>
          <v-col>
            <v-card class="elevation-4" color="primary">
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
                {{ formatDuration(station.averageReturnDuration) }}
              </v-card-subtitle>
            </v-card>
          </v-col>
        </v-row>
        <!-- Top stations to depart to -->
        <v-row>
          <v-col>
            <v-card class="elevation-4" color="primary">
              <v-row>
                <v-col>
                  <v-card-title>Top stations</v-card-title>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-card-subtitle>Departures</v-card-subtitle>
                  <top-stations-table :topStations="station.topDepartStations">
                  </top-stations-table>
                </v-col>
                <v-col>
                  <v-card-subtitle>Returns</v-card-subtitle>
                  <top-stations-table :topStations="station.topReturnStations">
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
import { SingleStationType } from "@/helpers/backend-data-types";
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
