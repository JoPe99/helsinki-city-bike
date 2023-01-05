<template>
  <v-container fluid>
    <welcome-card />
    <information-card />
  </v-container>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import InformationCard from "../components/homepage/InformationCard.vue";
import WelcomeCard from "../components/homepage/WelcomeCard.vue";

export default defineComponent({
  name: "HomeView",

  components: {
    WelcomeCard,
    InformationCard,
  },
});
</script>
