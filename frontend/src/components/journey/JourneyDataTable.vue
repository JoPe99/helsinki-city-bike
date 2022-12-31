<template>
  <div class="fill-height" v-resize="onResize">
    <v-card class="elevation-0">
      <journey-filters></journey-filters>
    </v-card>
    <div class="fill-height" ref="resizableDiv">
      <v-data-table
        :height="tableHeight"
        :headers="headers"
        :items="items"
        :options.sync="options"
        :server-items-length="totalJourneys"
        class="elevation-0"
        :footer-props="footerProps"
        :loading="tableLoading"
        @click:row="handleRowClick"
        :item-class="isSelected"
        :mobile-breakpoint="0"
        fixed-header
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
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { getJourneysCount, getJourneys } from "../../helpers/api-functions";

import {
  formatJourneyTypeArray,
  pageToOffset,
  sortByArrayToString,
  sortDescArrayToString,
} from "../../helpers/list-view-helpers";

import {
  FormattedJourneyType,
  JourneyType,
} from "../../helpers/backend-data-types";

import JourneyFilters from "./JourneyFilters.vue";

export default Vue.extend({
  name: "JourneyDataTable",
  components: { JourneyFilters },

  data: () => ({
    headers: [
      {
        text: "Departure time",
        align: "start",
        value: "departureDateTime",
        width: "15%",
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
      itemsPerPage: 25,
      page: 1,
      sortBy: ["departureTime"],
      sortDesc: [true],
      debouncedSearch: "",
    },
    items: [] as FormattedJourneyType[],
    footerProps: {
      "items-per-page-options": [15, 20, 25, 30, 50, 100],
      showFirstLastPage: true,
    },
    totalJourneys: 0,
    selectedJourney: 0,
    tableHeight: 0,
    tableLoading: true,
  }),

  watch: {
    // Watch options for changes, call API for new journeys if options change
    options: {
      handler() {
        // Drop selected journey, get new journeys when table options are changed
        this.selectedJourney = 0;
        console.log(this.options);
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

  methods: {
    // Sets the table to loading, formats parameters,
    // calls the API and handles response
    getJourneysFromAPI() {
      this.tableLoading = true;
      let pageSize = this.options.itemsPerPage;
      let offset = pageToOffset(this.options.page, pageSize);
      let sortBy = sortByArrayToString(this.options.sortBy);
      let sortDesc = sortDescArrayToString(this.options.sortDesc);
      let search = this.options.debouncedSearch;

      getJourneys(pageSize, offset, sortBy, sortDesc, search).then(
        (response) => {
          this.handleResponse(response.data, response.status);
          this.tableLoading = false;
        }
      );
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
        this.$emit("unselectedJourney");
      } else {
        this.selectedJourney = item.id;
        this.$emit("selectedJourney", item);
      }
    },

    // This makes sure that the table fits under filters and map,
    // sizing the table exactly right. The app bar is always 56px high.
    onResize() {
      const resizableDiv = this.$refs.resizableDiv as HTMLElement;
      if (resizableDiv) {
        this.tableHeight =
          window.innerHeight - resizableDiv.getBoundingClientRect().y - 56;
      }
    },
  },
});
</script>

<!-- Doesn't work if scoped -->
<style>
.selectedJourney {
  filter: brightness(50%);
}
</style>
