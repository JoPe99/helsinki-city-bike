<template>
  <v-card class="fill-height elevation-0">
    <div v-if="stationSelected">
      <!-- Names in all languages -->
      <!-- Addresses in all languages -->
      <!-- Total journeys -->
      <!-- Average distance/duration -->
      <!-- Top stations to depart to -->
      <!-- Top stations to return from -->
    </div>
    <div
      v-else
      class="fill-height d-flex flex-column align-center justify-center"
      style="text-align-last: center"
    >
      <v-card-title class="d-flex align-center">
        No station selected
      </v-card-title>
      <v-card-subtitle class="d-flex align-center">
        Select a station from the map or the list to see awesome details!
      </v-card-subtitle>
    </div>
  </v-card>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "StationDetailsCard",
  props: ["station"],

  data: () => ({}),
  computed: {
    stationSelected() {
      if (this.$props.station == null) {
        return false;
      } else {
        return true;
      }
    },
  },
  methods: {},
});
</script>
