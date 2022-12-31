<template>
  <v-container>
    <v-row>
      <v-col>
        <v-card-subtitle class="subtitle-2 pa-0"
          >Distance filter</v-card-subtitle
        >
        <vue-slider
          v-model.lazy="distanceSlider"
          :min="10"
          :max="getLongestJourneyByDistance"
        ></vue-slider>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-card-subtitle class="subtitle-2 pa-0"
          >Duration filter</v-card-subtitle
        >
        <vue-slider
          v-model.lazy="durationSlider"
          :min="10"
          :max="getLongestJourneyByDuration"
        ></vue-slider>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";

import VueSlider from "vue-slider-component";
import "vue-slider-component/theme/antd.css";

export default Vue.extend({
  name: "FilterSliders",
  components: { VueSlider },

  data: () => ({
    distanceSlider: [10, 10],
    durationSlider: [10, 10],
  }),

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
