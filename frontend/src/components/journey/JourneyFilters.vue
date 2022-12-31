<template>
  <div class="fill-height">
    <v-card class="elevation-0">
      <v-container class="mx-0">
        <v-col>
          <v-row>
            <v-card-title style="width: 100%">
              <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="Search"
                single-line
                hide-details
              ></v-text-field>
            </v-card-title>
          </v-row>
          <v-row>
            <date-picker></date-picker>
            <date-picker></date-picker>
          </v-row>
          <!-- Sliders for distance & duration filtering -->
          <v-row>
            <filter-sliders></filter-sliders>
          </v-row>
        </v-col>
      </v-container>
    </v-card>
  </div>
</template>

<script lang="ts">
import Vue from "vue";

import DatePicker from "../DatePicker.vue";
import FilterSliders from "./FilterSliders.vue";

export default Vue.extend({
  name: "JourneyFilters",
  components: { DatePicker, FilterSliders },

  data: () => ({
    options: {
      startTime: Date() as unknown,
      endTime: Date() as unknown,
      debouncedSearch: "",
    },
    search: "",
    timeout: {} as any,
    distanceSlider: [10, 10],
    durationSlider: [10, 10],
  }),

  watch: {
    // Watch options for changes, call API for new journeys if options change
    options: {
      handler() {
        console.log(this.options);
      },
      deep: true,
    },
    // Debounce search so it doesn't call API instantly after every change
    search: {
      handler() {
        if (this.timeout) clearTimeout(this.timeout);

        this.timeout = setTimeout(() => {
          this.options.debouncedSearch = this.search;
        }, 500);
      },
    },
  },

  mounted() {
    this.options.startTime = Date.now();
    this.options.endTime = Date.now();
  },

  computed: {
    getLongestJourneyByDistance() {
      return 45550;
    },

    getLongestJourneyByDuration() {
      return 45550;
    },
  },

  methods: {},
});
</script>

<!-- Doesn't work if scoped -->
<style>
.selectedJourney {
  filter: brightness(50%);
}
</style>
