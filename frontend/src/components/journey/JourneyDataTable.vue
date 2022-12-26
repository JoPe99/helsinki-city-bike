<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="items"
      class="elevation-1"
      :footer-props="footerProps"
      :loading="table_loading"
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
    footerProps: { "items-per-page-options": [15, 30, 50, 100] },
    table_loading: false,
  }),

  components: {},

  computed: {},

  methods: {
    runTest() {
      this.table_loading = true;
      getTrips(100, 160000, "departureTime").then((response) => {
        this.items = response.data;
        this.table_loading = false;
      });
    },
  },
});
</script>

<style scoped></style>
