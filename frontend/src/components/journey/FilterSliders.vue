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
            :data="distanceData"
            :tooltip-formatter="formatDistance"
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
            :data="durationData"
            :tooltip-formatter="formatDuration"
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
import { formatDistance, formatSeconds } from "@/helpers/list-view-helpers";

export default defineComponent({
  name: "FilterSliders",
  components: { VueSlider },

  data: () => ({
    sliders: {
      distanceSlider: [10, 10] as number[],
      durationSlider: [10, 10] as number[],
    },
    durationData: null as number[] | null,
    distanceData: null as number[] | null,
    store: useStore(),
  }),

  mounted() {
    this.durationData = this.createDurationIntervals;
    this.sliders.durationSlider = this.maxOutSlider(this.durationData);

    this.distanceData = this.createIntervals(this.distanceSliderIntervals);
    this.sliders.distanceSlider = this.maxOutSlider(this.distanceData);
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
    durationSliderIntervals() {
      return [
        {
          value: 10,
          step: 1,
        },
        {
          value: 60,
          step: 10,
        },
        {
          value: 1800,
          step: 30,
        },
        {
          value: 3600,
          step: 60,
        },
        {
          value: 86400,
          step: 43200,
        },
        {
          value: Number(this.getLongestJourneyDuration) + 43200,
          step: 86400,
        },
      ];
    },
    createDurationIntervals() {
      let result = [] as number[];

      this.durationSliderIntervals.forEach((point, idx) => {
        const lastPointValue =
          this.durationSliderIntervals[this.durationSliderIntervals.length - 1]
            .value;

        if (point.value === lastPointValue) {
          return;
        } else {
          const nextPoint = this.durationSliderIntervals[idx + 1];

          for (let i = point.value; i <= nextPoint.value; i += point.step) {
            result.push(i);
          }
        }
      });

      const uniqueValues = new Set(result);
      console.log(uniqueValues);
      return [...uniqueValues];
    },
    distanceSliderIntervals() {
      return [
        {
          value: 10,
          step: 1,
        },
        {
          value: 100,
          step: 10,
        },
        {
          value: 1000,
          step: 50,
        },
        {
          value: 10000,
          step: 100,
        },
        {
          value: 100000,
          step: 500,
        },
        {
          value: Number(this.getLongestJourneyDistance) + 500,
          step: 1,
        },
      ];
    },
    createDistanceIntervals() {
      let result = [] as number[];

      this.distanceSliderIntervals.forEach((point, idx) => {
        const lastPointValue =
          this.distanceSliderIntervals[this.distanceSliderIntervals.length - 1]
            .value;

        if (point.value === lastPointValue) {
          return;
        } else {
          const nextPoint = this.distanceSliderIntervals[idx + 1];

          for (let i = point.value; i <= nextPoint.value; i += point.step) {
            result.push(i);
          }
        }
      });

      const uniqueValues = new Set(result);
      console.log(uniqueValues);
      return [...uniqueValues];
    },
  },

  methods: {
    formatDuration(value: number) {
      return formatSeconds(value);
    },
    formatDistance(value: number) {
      return formatDistance(value);
    },
    maxOutSlider(data: number[]) {
      return [10, data[data.length - 1]] as number[];
    },
    createIntervals(data: { value: number; step: number }[]) {
      let result = [] as number[];

      data.forEach((point, idx) => {
        const lastPointValue = data[data.length - 1].value;

        if (point.value === lastPointValue) {
          return;
        } else {
          const nextPoint = data[idx + 1];

          for (let i = point.value; i <= nextPoint.value; i += point.step) {
            result.push(i);
          }
        }
      });

      const uniqueValues = new Set(result);
      console.log(uniqueValues);
      return [...uniqueValues];
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
