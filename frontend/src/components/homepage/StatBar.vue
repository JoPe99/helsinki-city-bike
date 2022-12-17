<template>
  <v-container fluid>
    <v-card color="primary" dark>
      <v-card-title>There is data about</v-card-title>
      <v-card-text>
        157034 trips done by Helsinki city bikes, and 156 bike stations.
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import { useStore } from "../../store/index";

export default Vue.extend({
  name: "StatBar",
  data: () => ({
    store: useStore(),
  }),
  computed: {
    totalTrips() {
      return this.store.totalTrips;
    },
  },
  methods: {},
});
</script>
