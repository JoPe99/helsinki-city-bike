import {
  getAllStations,
  getEarliestJourney,
  getJourneysCount,
  getLatestJourney,
  getLongestDistance,
  getLongestDuration,
} from "@/helpers/api-functions";
import { JourneyType, StationType } from "@/helpers/backend-data-types";
import { StationLocation } from "@/helpers/list-view-helpers";
import { defineStore } from "pinia";

export const useStore = defineStore("store", {
  state: () => ({
    totalJourneys: 0 as number,

    earliestJourney: {} as JourneyType,
    latestJourney: {} as JourneyType,

    longestJourneyByDistance: {} as JourneyType,
    longestJourneyByDuration: {} as JourneyType,

    stations: [] as StationType[],
    allStationMarkers: [] as StationLocation[],
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
        this.setupStationMarkers(response.data);
      });
    },
    setupStationMarkers(stations: StationType[]) {
      this.allStationMarkers = [] as StationLocation[];
      for (const station of stations) {
        this.allStationMarkers.push({
          id: station.id,
          name: station.nameFi,
          latLong: [Number(station.latitude), Number(station.longitude)],
        });
      }
      console.log(this.allStationMarkers);
    },
  },
});
