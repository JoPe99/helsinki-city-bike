<template>
  <v-container fluid>
    <v-row>
      <v-col>
        <v-card color="primary" class="elevation-4 pa-1">
          <v-card-subtitle class="subtitle-2 px-3 py-0"
            >Distance filter</v-card-subtitle
          >
          <vue-slider
            class="px-3"
            v-model="sliders.distanceSlider"
            :lazy="true"
            :min="10"
            :max="getLongestJourneyDistance"
          ></vue-slider>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-card color="primary" class="elevation-4 pa-1">
          <v-card-subtitle class="subtitle-2 px-3 py-0"
            >Duration filter</v-card-subtitle
          >
          <vue-slider
            class="px-3"
            v-model="sliders.durationSlider"
            :lazy="true"
            :min="10"
            :max="getLongestJourneyDuration"
          ></vue-slider>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import VueSlider from "vue-slider-component";
import "vue-slider-component/theme/antd.css";

import { useStore } from "@/store";

export default defineComponent({
  name: "FilterSliders",
  components: { VueSlider },

  data: () => ({
    sliders: {
      distanceSlider: [10, 10],
      durationSlider: [10, 10],
    },
    store: useStore(),
  }),

  mounted() {
    this.sliders.distanceSlider = [10, this.getLongestJourneyDistance];
    this.sliders.durationSlider = [10, this.getLongestJourneyDuration];
  },

  watch: {
    sliders: {
      handler() {
        this.$emit("newData", this.sliders);
      },
      deep: true,
    },
  },

  computed: {
    getLongestJourneyDistance(): number {
      return this.store.longestJourneyByDistance.distanceCovered;
    },

    getLongestJourneyDuration(): number {
      return this.store.longestJourneyByDuration.durationSeconds;
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
