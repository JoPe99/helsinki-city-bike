<template>
  <v-container fluid>
    <v-card color="primary" dark>
      <v-card-title dark>Welcome to my app!</v-card-title>
      <v-card-text>
        This app has information of {{ totalJourneys }} journeys done with
        Helsinki city bikes and {{ totalStations }} bike stations. The data used
        in this application is gathered from Helsingin Seudun Liikenne, which is
        a public transportation organization working in Helsinki. The data
        included is from a timespan of {{ getEarliestJourneyDate }} to
        {{ getLatestJourneyDate }}.
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import { useStore } from "../../store/index";

export default Vue.extend({
  name: "WelcomeCard",
  data: () => ({
    store: useStore(),
  }),
  computed: {
    totalJourneys() {
      return this.store.totalJourneys;
    },
    totalStations() {
      return this.store.stations.length;
    },
    getEarliestJourneyDate(): string {
      return Date.parse(this.store.earliestJourney.departureTime).toString(
        "d.M.yyyy"
      );
    },
    getLatestJourneyDate(): string {
      return Date.parse(this.store.latestJourney.departureTime).toString(
        "d.M.yyyy"
      );
    },
  },
  methods: {},
});
</script>
