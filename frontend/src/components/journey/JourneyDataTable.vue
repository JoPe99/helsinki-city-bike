<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="items"
      class="elevation-1"
      :footer-props="footerProps"
    ></v-data-table>
    <v-btn @click="runTest()">test</v-btn>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { getTrips } from "../../helpers/api-functions";
import { JourneyType } from "../../helpers/backend-data-types";

export default Vue.extend({
  name: "JourneyDataTable",

  data: () => ({
    headers: [
      {
        text: "Departure time",
        align: "start",
        value: "departureTime",
      },
      {
        text: "Return time",
        align: "start",
        value: "returnTime",
      },
      {
        text: "Departure station",
        align: "start",
        value: "departureStationName",
      },
      {
        text: "Return station",
        align: "start",
        value: "returnStationName",
      },
      {
        text: "Distance (m)",
        align: "start",
        value: "distanceCovered",
      },
      {
        text: "Duration (s)",
        align: "start",
        value: "durationSeconds",
      },
    ],
    items: [] as JourneyType[],
    footerProps: { "items-per-page-options": [15, 30, 50, 100, -1] },
    table_loading: false,
  }),

  components: {},

  computed: {},

  methods: {
    async runTest() {
      this.table_loading = true;
      let response = await getTrips(30, 0, "departureTime");
      this.items = response.data;
      this.table_loading = false;
    },
  },
});
</script>

<style scoped></style>
