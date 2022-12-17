<template>
  <div>
    <l-map style="height: 100%" :zoom="zoom" :center="center">
      <l-tile-layer :url="url"></l-tile-layer>
      <l-marker :lat-lng="markerLatLng"></l-marker>
      <l-marker :lat-lng="markerLatLng2"></l-marker>
    </l-map>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { LMap, LTileLayer, LMarker } from "vue2-leaflet";
import "leaflet/dist/leaflet.css";
import { Icon } from "leaflet";

// This block of code is from leaflet docs to fix a problem with Leaflet markers.
// https://vue2-leaflet.netlify.app/quickstart/#marker-icons-are-missing
type D = Icon.Default & {
  _getIconUrl?: string;
};

delete (Icon.Default.prototype as D)._getIconUrl;

declare var require: any;

Icon.Default.mergeOptions({
  iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
  iconUrl: require("leaflet/dist/images/marker-icon.png"),
  shadowUrl: require("leaflet/dist/images/marker-shadow.png"),
});
// Leaflet fix ends here

export default Vue.extend({
  name: "MapComponent",

  components: {
    LMap,
    LTileLayer,
    LMarker,
  },

  data: () => ({
    url: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
    zoom: 12,
    center: [60.192059, 24.945831],
    markerLatLng: [60.192059, 24.945831],
    markerLatLng2: [60.142059, 24.845831],
  }),

  computed: {},

  methods: {},
});
</script>

<style scoped></style>
