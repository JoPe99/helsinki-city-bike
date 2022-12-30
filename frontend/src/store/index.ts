import { defineStore } from "pinia";

export const useStore = defineStore("store", {
  state: () => ({
    totalJourneys: 8,
  }),
  getters: {
    testGetter: (state) => state.totalJourneys,
  },
  actions: {
    addJourney() {
      this.totalJourneys++;
    },
  },
});
