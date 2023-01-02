<template>
  <div class="fill-height">
    <v-card class="elevation-0">
      <v-container fluid class="mx-0 px-4">
        <v-col>
          <!-- Row contains datepickers & search -->
          <v-row>
            <v-col cols="3">
              <v-card color="primary" class="elevation-4">
                <date-picker
                  @newData="handleNewDate"
                  id="start_date"
                  title="Start date"
                ></date-picker>
              </v-card>
            </v-col>
            <v-col cols="3">
              <v-card color="primary" class="elevation-4">
                <date-picker
                  @newData="handleNewDate"
                  id="end_date"
                  title="End date"
                ></date-picker>
              </v-card>
            </v-col>
            <v-col cols="6">
              <v-card color="primary" class="fill-height elevation-4">
                <v-text-field
                  class="pa-4 mt-0"
                  v-model="search"
                  append-icon="mdi-magnify"
                  label="Search stations"
                  single-line
                  hide-details
                ></v-text-field>
              </v-card>
            </v-col>
          </v-row>
          <!-- Sliders for distance & duration filtering -->
          <v-row>
            <filter-sliders @newData="handleSliderChange"></filter-sliders>
          </v-row>
        </v-col>
      </v-container>
    </v-card>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import DatePicker from "../DatePicker.vue";
import FilterSliders from "./FilterSliders.vue";

export default defineComponent({
  name: "JourneyFilters",

  components: { DatePicker, FilterSliders },

  data: () => ({
    filters: {
      startTime: Date() as unknown,
      endTime: Date() as unknown,
      debouncedSearch: "",
      distanceFilter: [10, 10] as number[],
      durationFilter: [10, 10] as number[],
    },
    search: "",
    timeout: {} as any,
  }),

  mounted() {
    this.filters.startTime = Date.now();
    this.filters.endTime = Date.now();
  },

  watch: {
    // Watch filters for changes, emit new filters when that happens
    filters: {
      handler() {
        this.$emit("newFilters", this.filters);
      },
      deep: true,
    },
    // Debounce search by 500ms so it
    // doesn't call API instantly after every change
    search: {
      handler() {
        if (this.timeout) clearTimeout(this.timeout);

        this.timeout = setTimeout(() => {
          this.filters.debouncedSearch = this.search;
        }, 500);
      },
    },
  },

  computed: {},

  methods: {
    // On new date, the date is stored as
    // "YYYY-MM-DD" in the options
    handleNewDate(id: string, date: string) {
      console.log(id + date);
      if (id == "start_date") {
        this.filters.startTime = date;
      } else if (id == "end_date") {
        this.filters.endTime = date;
      }
    },
    handleSliderChange(sliders: {
      distanceSlider: number[];
      durationSlider: number[];
    }) {
      this.filters.distanceFilter = sliders.distanceSlider;
      this.filters.durationFilter = sliders.durationSlider;
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
