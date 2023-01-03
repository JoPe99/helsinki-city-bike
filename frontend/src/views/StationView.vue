<template>
  <v-container class="pa-0" fluid>
    <map-component style="height: 500px" />
    <station-data-table></station-data-table>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import MapComponent from "../components/MapComponent.vue";
import StationDataTable from "@/components/station/StationDataTable.vue";

export default Vue.extend({
  name: "StationView",
  components: { MapComponent, StationDataTable },
});
</script>
