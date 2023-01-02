import {
  getAllStations,
  getEarliestJourney,
  getJourneysCount,
  getLatestJourney,
  getLongestDistance,
  getLongestDuration,
} from "@/helpers/api-functions";
import { JourneyType, StationType } from "@/helpers/backend-data-types";
import { defineStore } from "pinia";

export const useStore = defineStore("store", {
  state: () => ({
    totalJourneys: 0 as number,

    earliestJourney: {} as JourneyType,
    latestJourney: {} as JourneyType,

    longestJourneyByDistance: {} as JourneyType,
    longestJourneyByDuration: {} as JourneyType,

    stations: [] as StationType[],
  }),
  getters: {},
  actions: {
    setupStore() {
      getJourneysCount().then((response) => {
        this.totalJourneys = response.data;
      });

      getEarliestJourney().then((response) => {
        this.earliestJourney = response.data[0];
      });

      getLatestJourney().then((response) => {
        this.latestJourney = response.data[0];
      });

      getLongestDistance().then((response) => {
        this.longestJourneyByDistance = response.data[0];
      });

      getLongestDuration().then((response) => {
        this.longestJourneyByDuration = response.data[0];
      });

      getAllStations().then((response) => {
        this.stations = response.data;
      });
    },
  },
});
