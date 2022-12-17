<template>
  <v-container fluid>
    <welcome-bar />
    <stat-bar />
    <actions-list />
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import ActionsList from "../components/homepage/ActionsList.vue";
import StatBar from "../components/homepage/StatBar.vue";
import WelcomeBar from "../components/homepage/WelcomeBar.vue";

export default Vue.extend({
  name: "HomeView",

  components: {
    StatBar,
    WelcomeBar,
    ActionsList,
  },
});
</script>
