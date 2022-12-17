<template>
  <v-text>{{ totalTrips }}</v-text>
</template>

<script lang="ts">
import Vue from "vue";
import { useStore } from "../store/index";

export default Vue.extend({
  name: "StatBar",
  data: () => ({
    store: useStore(),
    totalTrips: 0,
  }),
  computed: {},
  methods: {},
});
</script>
