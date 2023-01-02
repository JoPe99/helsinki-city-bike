<template>
  <v-container class="pa-0" fluid>
    <map-component style="height: 500px" />
    <v-data-table></v-data-table>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import MapComponent from "../components/MapComponent.vue";

export default Vue.extend({
  name: "StationView",
  components: { MapComponent },
});
</script>
