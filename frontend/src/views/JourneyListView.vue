<template>
  <v-container fluid>
    <map-component style="height: 500px" />
    <journey-data-table></journey-data-table>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import MapComponent from "@/components/MapComponent.vue";
import JourneyDataTable from "@/components/journey/JourneyDataTable.vue";

export default Vue.extend({
  name: "JourneyListView",
  components: { MapComponent, JourneyDataTable },
});
</script>
