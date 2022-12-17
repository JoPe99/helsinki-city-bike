<template>
  <v-container fluid>
    <v-card color="primary" dark>
      <v-card-title>Here are things you can do with this app</v-card-title>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import { useStore } from "../../store/index";

export default Vue.extend({
  name: "ActionsList",
  data: () => ({
    store: useStore(),
  }),
  computed: {},
  methods: {},
});
</script>
