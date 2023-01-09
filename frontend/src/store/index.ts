import {
  getAllStations,
  getEarliestJourney,
  getJourneysCount,
  getLatestJourney,
  getLongestDistance,
  getLongestDuration,
} from "@/helpers/api-functions";
import { JourneyType, StationType } from "@/helpers/backend-data-types";
import { StationLocation } from "@/helpers/format-helpers";
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
    stationIDs: [] as number[],
  }),
  getters: {},
  actions: {
    async setupStore() {
      const journeyPromise = this.updateStoreJourneyData();
      const stationPromise = this.updateStoreStationData();

      Promise.all([journeyPromise, stationPromise]).then(() => {
        this.storeReady = true;
        console.log("Store ready");
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
    },

    setupStationIDs(stations: StationType[]) {
      this.stationIDs = [] as number[];
      for (const station of stations) {
        this.stationIDs.push(station.id);
      }
      console.log(this.stationIDs);
    },

    // Called after inserting new station
    async updateStoreStationData() {
      await getAllStations().then((response) => {
        this.stations = response.data;
        this.setupStationMarkers(response.data);
        this.setupStationIDs(response.data);
      });
    },

    // Called after inserting new journey
    async updateStoreJourneyData() {
      const countPromise = getJourneysCount().then((response) => {
        this.totalJourneys = response.data;
      });

      const earliestPromise = getEarliestJourney().then((response) => {
        this.earliestJourney = response.data.journeys[0];
      });

      const latestPromise = getLatestJourney().then((response) => {
        this.latestJourney = response.data.journeys[0];
      });

      const distancePromise = getLongestDistance().then((response) => {
        this.longestJourneyByDistance = response.data.journeys[0];
      });

      const durationPromise = getLongestDuration().then((response) => {
        this.longestJourneyByDuration = response.data.journeys[0];
      });

      Promise.allSettled([
        countPromise,
        earliestPromise,
        latestPromise,
        distancePromise,
        durationPromise,
      ]);
    },
  },
});
