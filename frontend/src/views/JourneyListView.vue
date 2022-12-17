<template>
  <v-container fluid>
    <map-component style="height: 500px" />
    <v-data-table color="primary"></v-data-table>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import MapComponent from "../components/MapComponent.vue";

export default Vue.extend({
  name: "JourneyListView",
  components: { MapComponent },
});
</script>
