import { defineStore } from "pinia";

export const useStore = defineStore("store", {
  state: () => ({
    totalTrips: 8,
  }),
  getters: {
    testGetter: (state) => state.totalTrips,
  },
  actions: {
    addTrip() {
      this.totalTrips++;
    },
  },
});
