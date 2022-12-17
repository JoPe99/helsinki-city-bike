<template>
  <stat-bar />
</template>

<script lang="ts">
import Vue from "vue";
import StatBar from "../components/StatBar.vue";

export default Vue.extend({
  name: "HomeView",

  components: {
    StatBar,
  },
});
</script>
