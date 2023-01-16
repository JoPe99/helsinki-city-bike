<template>
  <div class="elevation-1">
    <l-map
      ref="map"
      style="height: 100%; z-index: 0"
      :zoom="zoom"
      :center="center"
      :bounds="bounds"
      @ready="invalidateSize"
    >
      <l-tile-layer :url="url"></l-tile-layer>
      <l-layer-group :ref="markers">
        <l-marker
          v-for="marker in currentMarkers"
          :key="marker.id"
          :latLng="marker.latLong"
          @click="handleMarkerClick(marker.id, marker.name)"
        >
          <l-tooltip> ID: {{ marker.id }} Name: {{ marker.name }} </l-tooltip>
        </l-marker>
        <l-polyline
          v-if="polyline.active"
          :lat-lngs="polyline.latLong"
          :color="polyline.color"
          :opacity="polyline.opacity"
        >
        </l-polyline>
      </l-layer-group>
    </l-map>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { defineComponent } from "vue";
import {
  LMap,
  LTileLayer,
  LMarker,
  LPolyline,
  LLayerGroup,
  LTooltip,
} from "vue2-leaflet";
import "leaflet/dist/leaflet.css";
import { Icon, LatLng, latLngBounds } from "leaflet";
import { StationLocation } from "@/helpers/format-helpers";

// This block of code is from leaflet docs to fix a problem with missing Leaflet markers.
// https://vue2-leaflet.netlify.app/quickstart/#marker-icons-are-missing
type D = Icon.Default & {
  _getIconUrl?: string;
};

delete (Icon.Default.prototype as D)._getIconUrl;

// Awful but works perfectly
// eslint-disable-next-line
declare var require: any;

Icon.Default.mergeOptions({
  iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
  iconUrl: require("leaflet/dist/images/marker-icon.png"),
  shadowUrl: require("leaflet/dist/images/marker-shadow.png"),
});
// Leaflet fix ends here

export default defineComponent({
  name: "MapComponent",

  props: ["markers"],

  components: {
    LMap,
    LTileLayer,
    LMarker,
    LPolyline,
    LLayerGroup,
    LTooltip,
  },

  data: () => ({
    url: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
    zoom: 12,
    center: [60.192059, 24.945831], // Coordinates to general area of bikes
    currentMarkers: [] as StationLocation[],
    mapIsReady: false,
    bounds: latLngBounds(
      { lat: 60.154097, lng: 25.175603 },
      { lat: 60.253048, lng: 24.647907 }
    ),
    default_bounds: latLngBounds(
      { lat: 60.154097, lng: 25.175603 },
      { lat: 60.253048, lng: 24.647907 }
    ),
    polyline: {
      active: false,
      latLong: [] as LatLng[],
      color: "blue",
      opacity: 0.5,
    },
  }),

  watch: {
    markers: {
      handler(newVal: StationLocation[]) {
        if (newVal.length == 0) {
          this.resetMap();
        } else {
          this.handleNewMarkers(newVal);
        }
      },
      deep: true,
    },
  },

  computed: {},

  methods: {
    // Creates bounds for the markers, and adds markers
    // to the map.
    handleNewMarkers(newMarkers: StationLocation[]) {
      let boundsMap: LatLng[] = [];
      let polylineMap: LatLng[] = [];

      for (let marker of newMarkers) {
        let currentLatLong = new LatLng(marker.latLong[0], marker.latLong[1]);

        // If only two markers (Journey markers),
        // add coordinates to polylineMap
        if (newMarkers.length == 2) {
          polylineMap.push(currentLatLong);
        }
        // Add coordinates to bounds
        boundsMap.push(currentLatLong);
      }

      // If polylinemap got the right amount of coordinates,
      // add coordinates to polyline and make it active
      if (polylineMap.length == 2) {
        this.polyline.latLong = polylineMap;
        this.polyline.active = true;
      }

      // To avoid Vue duplicate key error in cases where departure and
      // return stations are same, only push one marker on the map.
      // If not two same stations, push all markers to the map.
      if (newMarkers.length == 2 && newMarkers[0].id == newMarkers[1].id) {
        this.currentMarkers = [newMarkers[0]] as StationLocation[];
      } else {
        this.currentMarkers = newMarkers;
      }

      // Change bounds to fit markers with padding
      this.bounds = latLngBounds(boundsMap).pad(0.1);
    },

    // Reset map to default bounds, remove markers and polyline
    resetMap() {
      this.currentMarkers = [];
      this.bounds = this.default_bounds;
      this.polyline.active = false;
    },

    // Refresh map size on ready, to make sure that the center is correct
    invalidateSize() {
      // There is for sure a cleaner way to do this,
      // than with "as any".
      // eslint-disable-next-line
      let mapObject = (this.$refs.map as any).mapObject;
      (mapObject as Vue & { invalidateSize(): void }).invalidateSize();
    },

    handleMarkerClick(id: number, name: string) {
      let clickedMarker = { id: id, name: name };
      this.$emit("markerClicked", clickedMarker);
    },
  },
});
</script>

<style scoped></style>
