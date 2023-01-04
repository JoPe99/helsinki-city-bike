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
    storeReady: false as boolean,

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
    async setupStore() {
      await getJourneysCount().then((response) => {
        this.totalJourneys = response.data;
      });

      await getEarliestJourney().then((response) => {
        this.earliestJourney = response.data.journeys[0];
      });

      await getLatestJourney().then((response) => {
        this.latestJourney = response.data.journeys[0];
      });

      await getLongestDistance().then((response) => {
        this.longestJourneyByDistance = response.data.journeys[0];
      });

      await getLongestDuration().then((response) => {
        this.longestJourneyByDuration = response.data.journeys[0];
      });

      await getAllStations().then((response) => {
        this.stations = response.data;
        this.setupStationMarkers(response.data);
      });
      this.storeReady = true;
      console.log("Store ready");
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
    },
  },
});
