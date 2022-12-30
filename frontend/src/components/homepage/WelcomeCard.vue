<template>
  <v-container fluid>
    <v-card color="primary" dark>
      <v-card-title dark>Welcome to my app!</v-card-title>
      <v-card-text>
        This app has information about 157034 journeys done by Helsinki city
        bikes, and 156 bike stations. The data used in this application is
        gathered from Helsingin Seudun Liikenne, which is a public
        transportation organization working in Helsinki.
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import { useStore } from "../../store/index";

export default Vue.extend({
  name: "WelcomeCard",
  data: () => ({
    store: useStore(),
  }),
  computed: {
    totalJourneys() {
      return this.store.totalJourneys;
    },
  },
  methods: {},
});
</script>
