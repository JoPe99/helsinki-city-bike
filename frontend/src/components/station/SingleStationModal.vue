<template>
  <v-dialog
    persistent
    class="ma-0 pa-0"
    v-model="dialog"
    max-height="800px"
    max-width="800px"
  >
    <v-card class="ma-0 pa-0" :loading="loading">
      <template slot="progress">
        <v-progress-linear color="blue" indeterminate></v-progress-linear>
      </template>
      <v-container style="height: 100%">
        <!-- Title and information -->
        <v-row>
          <v-col>
            <v-card class="elevation-4" color="primary">
              <v-card-title>
                Station information:
                {{ station.nameFi }} (ID: {{ station.id }})</v-card-title
              >
            </v-card>
          </v-col>
        </v-row>
        <v-row class="mt-0">
          <v-col>
            <v-card color="primary" class="pa-3">
              <date-picker
                id="start"
                label="Start date"
                defaultDate="2021-01-01"
                @newData="handleNewDate"
              ></date-picker>
            </v-card>
          </v-col>
          <v-col>
            <v-card color="primary" class="pa-3">
              <date-picker
                id="end"
                label="End date"
                defaultDate="2021-12-31"
                @newData="handleNewDate"
              ></date-picker>
            </v-card>
          </v-col>
        </v-row>
        <!-- Total journeys and average distance/duration-->
        <v-row class="mt-0">
          <v-col>
            <v-card class="elevation-4 fill-height" color="primary">
              <v-card-title>
                During this time period station had
                {{ totalJourneys }} departures and returns.
              </v-card-title>
              <v-card-text class="text-subtitle-1">
                <div>
                  Of the total journeys, {{ station.totalDepartJourneys }} were
                  departures and {{ station.totalReturnJourneys }} were returns.
                </div>
                <div>
                  Average depart journey distance was
                  {{ formatDistance(station.averageDepartDistance) }}
                  and average return journey distance was
                  {{ formatDistance(station.averageReturnDistance) }}.
                </div>
                <div>
                  Average duration for a depart journey was
                  {{ formatDuration(station.averageDepartDuration) }} and for
                  returns the average was
                  {{ formatDuration(station.averageReturnDuration) }}.
                </div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
        <!-- Top stations -->
        <v-row>
          <v-col>
            <v-card class="elevation-4" color="primary">
              <v-row>
                <v-col class="px-8">
                  <v-card-title class="pa-0 pb-3"
                    >Top departure stations</v-card-title
                  >
                  <top-stations-table
                    class="elevation-4"
                    :topStations="station.topDepartStations"
                  >
                  </top-stations-table>
                </v-col>
                <v-col class="px-8">
                  <v-card-title class="pa-0 pb-3"
                    >Top return stations</v-card-title
                  >
                  <top-stations-table
                    class="elevation-4"
                    :topStations="station.topReturnStations"
                  >
                  </top-stations-table>
                </v-col>
              </v-row>
            </v-card>
          </v-col>
        </v-row>
        <!-- Close button -->
        <v-row class="pr-3">
          <v-col align="end">
            <v-btn color="secondary" @click="closeModal"> Close </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { formatDistance, formatSeconds } from "@/helpers/list-view-helpers";
import { SingleStationType } from "@/helpers/backend-data-types";
import { getSingleStation } from "@/helpers/api-functions";
import TopStationsTable from "./TopStationsTable.vue";
import DatePicker from "../DatePicker.vue";

export default defineComponent({
  name: "SingleStationModal",

  props: ["modal", "singleStation"],

  components: { TopStationsTable, DatePicker },

  mounted() {
    this.dialog = this.modal;
    this.station = this.singleStation;
    this.startDate = this.defaultStartDate;
    this.endDate = this.defaultEndDate;
  },

  data: () => ({
    loading: false,
    defaultStartDate: "2021-01-01",
    defaultEndDate: "2021-12-31",
    dialog: false,
    station: {} as SingleStationType,
    startDate: "" as string,
    endDate: "" as string,
  }),

  watch: {
    modal: function (modal: boolean) {
      this.dialog = modal;
    },
    singleStation: function (singleStation: SingleStationType) {
      this.station = singleStation;
    },
  },

  computed: {
    totalJourneys(): number {
      return (
        this.station.totalDepartJourneys + this.station.totalReturnJourneys
      );
    },
  },

  methods: {
    handleNewDate(id: "start" | "end", date: string) {
      console.log("New date");
      if (id == "start") {
        this.startDate = date;
      } else {
        this.endDate = date;
      }

      if (this.startDate != null && this.endDate != null) {
        this.getStationDataForDuration();
      }
    },

    getStationDataForDuration() {
      // Call API for new data
      getSingleStation(this.station.id, this.startDate, this.endDate).then(
        (response) => {
          console.log(response);
          this.station = response.data;
        }
      );
    },
    formatDistance(distance: number) {
      return formatDistance(distance) as string;
    },
    formatDuration(duration: number) {
      return formatSeconds(duration) as string;
    },
    closeModal() {
      this.$destroy;
      this.$emit("modalClosed");
    },
  },
});
</script>
