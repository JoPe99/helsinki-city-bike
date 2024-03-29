<template>
  <v-container fluid>
    <v-card color="primary" :loading="!cardReady">
      <template slot="progress">
        <v-progress-linear color="blue" indeterminate></v-progress-linear>
      </template>
      <v-card-title>Welcome to my app!</v-card-title>
      <v-card-text v-if="cardReady" class="text-body-1">
        This app has information of {{ totalJourneys }} journeys done with
        Helsinki city bikes and {{ totalStations }}
        bike stations. The data used in this application is gathered from
        Helsingin Seudun Liikenne, which is a public transportation organization
        working in Helsinki. The data included spans from
        {{ earliestJourneyDate }} to {{ latestJourneyDate }}.
        <p></p>
        The journey with longest distance traveled was started at station
        {{ longestJourneyByDistance.departureStationName }} and it ended at
        station {{ longestJourneyByDistance.returnStationName }}. The journey
        was {{ formatDistance(longestJourneyByDistance.distanceCovered) }} long,
        and it took
        {{ formatDuration(longestJourneyByDistance.durationSeconds) }} to
        complete the journey.
        <p></p>
        The journey which took the most time was a journey started at
        {{ longestJourneyByDuration.departureStationName }} and it ended at
        station {{ longestJourneyByDuration.returnStationName }}. The journey
        took whopping
        {{ formatDuration(longestJourneyByDuration.durationSeconds) }} to
        complete, and the journey was
        {{ formatDistance(longestJourneyByDuration.distanceCovered) }} long.
        Must have been an adventure!
      </v-card-text>
      <v-card-text v-else>
        Setting up the information, just a moment! If this doesn't stop loading
        in couple of seconds, make sure the server is up and running, and has
        finished parsing the CSV files.
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { JourneyType } from "@/types/backend-data-types";
import { formatDistance, formatSeconds } from "@/helpers/format-helpers";
import { useStore } from "../../store/index";

export default defineComponent({
  name: "WelcomeCard",
  data: () => ({
    interval: null as number | null,
    store: useStore(),
    cardReady: false,
    earliestJourneyDate: "" as string,
    latestJourneyDate: "" as string,
    longestJourneyByDistance: {} as JourneyType,
    longestJourneyByDuration: {} as JourneyType,
    totalStations: 0 as number,
    totalJourneys: 0 as number,
  }),

  created() {
    this.interval = setInterval(this.setupData, 500) as unknown as number;
  },

  computed: {
    storeReady(): boolean {
      return this.store.storeReady;
    },
  },

  methods: {
    setupData() {
      if (this.storeReady) {
        this.earliestJourneyDate = this.getEarliestJourneyDate();
        this.latestJourneyDate = this.getLatestJourneyDate();
        this.longestJourneyByDistance = this.getLongestJourneyByDistance();
        this.longestJourneyByDuration = this.getLongestJourneyByDuration();
        this.totalStations = this.getTotalStations();
        this.totalJourneys = this.getTotalJourneys();

        clearInterval(this.interval as number);
        this.cardReady = true;
      }
    },
    getTotalJourneys() {
      return this.store.totalJourneys;
    },
    getTotalStations() {
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
    getLongestJourneyByDistance(): JourneyType {
      return this.store.longestJourneyByDistance;
    },
    getLongestJourneyByDuration(): JourneyType {
      return this.store.longestJourneyByDuration;
    },
    formatDistance(distance: number) {
      return formatDistance(distance);
    },
    formatDuration(duration: number) {
      return formatSeconds(duration);
    },
  },
});
</script>
