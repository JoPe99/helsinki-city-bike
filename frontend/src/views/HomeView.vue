<template>
  <v-container fluid>
    <welcome-card />
    <actions-list />
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import ActionsList from "../components/homepage/ActionsList.vue";
import WelcomeCard from "../components/homepage/WelcomeCard.vue";

export default Vue.extend({
  name: "HomeView",

  components: {
    WelcomeCard,
    ActionsList,
  },
});
</script>
