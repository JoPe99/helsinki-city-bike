<template>
  <v-dialog
    persistent
    class="ma-0 pa-0"
    v-model="dialog"
    max-height="800px"
    max-width="800px"
  >
    <v-card class="ma-0 pa-0" style="height: 800px; width: 800px">
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
        <!-- Total journeys and average distance/duration-->
        <v-row class="mt-0">
          <v-col>
            <v-card class="elevation-4 fill-height" color="primary">
              <v-card-title>
                Station has {{ station.totalDepartJourneys }} departures and
                {{ station.totalReturnJourneys }} returns
              </v-card-title>
              <v-card-subtitle>
                Average depart journey was
                {{ formatDistance(station.averageDepartDistance) }} and average
                return journey was
                {{ formatDistance(station.averageReturnDistance) }}. Average
                duration for depart journey was
                {{ formatDuration(station.averageDepartDuration) }} and for
                return journey average duration was
                {{ formatDuration(station.averageReturnDuration) }}.
              </v-card-subtitle>
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
import StationDetailsCard from "@/components/station/StationDetailsCard.vue";
import { SingleStationType } from "@/helpers/backend-data-types";
import TopStationsTable from "./TopStationsTable.vue";

export default defineComponent({
  name: "SingleStationModal",

  props: ["modal", "station"],

  components: { TopStationsTable },

  mounted() {
    this.dialog = this.modal;
    this.singleStation = this.station;
  },

  data: () => ({
    dialog: false,
    singleStation: {} as SingleStationType,
  }),

  watch: {
    modal: function (modal: boolean) {
      this.dialog = modal;
    },
    station: function (station: SingleStationType) {
      this.singleStation = station;
    },
  },

  computed: {
    stationSelected() {
      if (this.$props.station == null) {
        return false;
      } else {
        return true;
      }
    },
  },

  methods: {
    formatDistance(distance: number) {
      return formatDistance(distance) as string;
    },
    formatDuration(duration: number) {
      return formatSeconds(duration) as string;
    },
    closeModal() {
      this.$emit("modalClosed");
    },
  },
});
</script>
