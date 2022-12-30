<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="items"
      :options.sync="options"
      :server-items-length="totalJourneys"
      class="elevation-1"
      :footer-props="footerProps"
      :loading="table_loading"
    ></v-data-table>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { getJourneysCount, getJourneys } from "../../helpers/api-functions";
import {
  formatJourneyTypeArray,
  pageToOffset,
  sortByArrayToString,
  timestampToDate,
} from "../../helpers/list-view-helpers";
import {
  FormattedJourneyType,
  JourneyType,
} from "../../helpers/backend-data-types";

export default Vue.extend({
  name: "JourneyDataTable",

  data: () => ({
    headers: [
      {
        text: "Departure time",
        align: "start",
        value: "departureDateTime",
      },
      {
        text: "Return time",
        align: "start",
        value: "returnDateTime",
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
        text: "Distance",
        align: "start",
        value: "distance",
      },
      {
        text: "Duration",
        align: "start",
        value: "duration",
      },
    ],
    options: {
      itemsPerPage: 15,
      page: 1,
      sortBy: ["departureTime"],
      sortDesc: true,
    },
    totalJourneys: 0,
    items: [] as FormattedJourneyType[],
    footerProps: {
      "items-per-page-options": [15, 30, 50, 100],
      showFirstLastPage: true,
    },
    table_loading: false,
  }),

  watch: {
    options: {
      handler() {
        this.getJourneysFromAPI();
      },
      deep: true,
    },
  },

  mounted() {
    getJourneysCount().then((response) => {
      this.totalJourneys = response.data;
    });
  },

  components: {},

  computed: {},

  methods: {
    getJourneysFromAPI() {
      this.table_loading = true;
      let pageSize = this.options.itemsPerPage;
      let offset = pageToOffset(this.options.page, pageSize);
      let sortBy = sortByArrayToString(this.options.sortBy);

      getJourneys(pageSize, offset, sortBy).then((response) => {
        this.handleResponse(response.data, response.status);
        this.table_loading = false;
      });
    },
    handleResponse(data: JourneyType[], status: any) {
      // Handle status here
      if (status != 200) {
        return;
      }

      let formattedArray = formatJourneyTypeArray(data);

      console.log(formattedArray);
      this.items = formattedArray;
    },
  },
});
</script>

<style scoped></style>
