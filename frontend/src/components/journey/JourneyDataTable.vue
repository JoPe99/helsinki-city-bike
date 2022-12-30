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
      @click:row="handleRowClick"
      :item-class="isSelected"
    >
      <!-- Datetime columns are customized to be more readable  -->
      <template v-slot:item.departureDateTime="{ item }">
        <v-list-item-title class="text-subtitle-2">{{
          item.departureDateTime.day +
          "." +
          item.departureDateTime.month +
          "." +
          item.departureDateTime.year
        }}</v-list-item-title>
        <v-list-item-subtitle class="grey--text text-subtitle-2">
          {{
            padTime(item.departureDateTime.hours) +
            ":" +
            padTime(item.departureDateTime.minutes) +
            ":" +
            padTime(item.departureDateTime.seconds)
          }}
        </v-list-item-subtitle>
      </template>
      <template v-slot:item.returnDateTime="{ item }">
        <v-list-item-title class="text-subtitle-2">{{
          item.returnDateTime.day +
          "." +
          item.returnDateTime.month +
          "." +
          item.returnDateTime.year
        }}</v-list-item-title>
        <v-list-item-subtitle class="grey--text text-subtitle-2">
          {{
            padTime(item.returnDateTime.hours) +
            ":" +
            padTime(item.returnDateTime.minutes) +
            ":" +
            padTime(item.returnDateTime.seconds)
          }}
        </v-list-item-subtitle>
      </template>
    </v-data-table>
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
      sortDesc: [true],
    },
    totalJourneys: 0,
    items: [] as FormattedJourneyType[],
    footerProps: {
      "items-per-page-options": [15, 30, 50, 100],
      showFirstLastPage: true,
    },
    selectedJourney: 0,
    table_loading: true,
  }),

  watch: {
    options: {
      handler() {
        // Drop selected journey, get new journeys when table options are changed
        this.selectedJourney = 0;
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
    // Sets the table to loading, formats parameters,
    // calls the API and handles response
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

    // Handles API call status and formats the received data
    handleResponse(data: JourneyType[], status: any) {
      // Handle status here
      if (status != 200) {
        return;
      }
      this.items = formatJourneyTypeArray(data);
    },

    /** Pads time with zero to always be 2 digits.
     *  Example: 6 is padded to 06, 12 stays as 12
     */
    padTime(time: number) {
      return time.toString().padStart(2, "0");
    },

    // If row is selected, apply class to it
    isSelected(item: FormattedJourneyType) {
      if (item.id == this.selectedJourney) {
        return "selectedJourney";
      }
    },

    // Clicked row is stored as selected journey
    handleRowClick(item: FormattedJourneyType) {
      if (this.selectedJourney == item.id) {
        this.selectedJourney = 0;
        this.$emit("journeyUnselected");
      } else {
        this.selectedJourney = item.id;
        this.$emit("selectedJourney", item);
      }
    },
  },
});
</script>

<style>
.selectedJourney {
  filter: brightness(50%);
}
</style>
