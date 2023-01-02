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
            :max="getLongestJourneyByDistance"
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
            :max="getLongestJourneyByDuration"
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

export default defineComponent({
  name: "FilterSliders",
  components: { VueSlider },

  data: () => ({
    sliders: {
      distanceSlider: [10, 45500],
      durationSlider: [10, 45500],
    },
  }),

  watch: {
    sliders: {
      handler() {
        this.$emit("newData", this.sliders);
      },
      deep: true,
    },
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
