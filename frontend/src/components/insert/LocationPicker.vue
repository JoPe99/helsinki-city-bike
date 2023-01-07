<template>
  <v-dialog v-model="dialog" max-width="70%">
    <template v-slot:activator="{ on, attrs }">
      <v-container fluid class="pa-0">
        <v-row>
          <v-col cols="4">
            <v-btn
              style="height: 85%; width: 90%"
              class="no-text-transform"
              color="secondary"
              dark
              v-bind="attrs"
              v-on="on"
            >
              <span class="text-wrap" style="max-width: 90%">
                Pick location
              </span>
            </v-btn>
          </v-col>
          <v-col class="py-5" cols="8">
            <v-text-field
              label="Latitude"
              outlined
              color="highlight"
              readonly
              type="number"
              v-model="displayLat"
            ></v-text-field>

            <v-text-field
              label="Longitude"
              outlined
              color="highlight"
              readonly
              type="number"
              v-model="displayLng"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-container>
    </template>
    <v-card style="height: 800px">
      <l-map ref="map" style="height: 750px" :zoom="zoom" :center="center">
        <l-tile-layer :url="url"></l-tile-layer>
        <l-marker :draggable="true" :lat-lng.sync="markerPosition"></l-marker>
      </l-map>
      <v-card-actions style="height: 50px">
        <v-spacer></v-spacer>
        <v-btn color="highlight" text @click="dialog = false"> Close </v-btn>
        <v-btn color="highlight" text @click="saveLocation"> Save </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import Vue from "vue";
import { defineComponent } from "vue";
import { LMap, LMarker, LTileLayer } from "vue2-leaflet";
import "leaflet/dist/leaflet.css";
import { Icon } from "leaflet";

// This block of code is from leaflet docs to fix a problem with missing Leaflet markers.
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

export default defineComponent({
  name: "LocationPicker",
  props: ["refresh"],

  components: { LMap, LTileLayer, LMarker },

  data: () => ({
    dialog: false,
    url: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
    zoom: 12,
    center: [60.192059, 24.945831],
    // Map takes markers as array and syncs as object.
    markerPosition: [60.192059, 24.945831] as unknown,
    displayLat: null as number | null,
    displayLng: null as number | null,
  }),

  watch: {
    dialog(visible) {
      if (visible) {
        this.$nextTick(() => {
          setTimeout(this.invalidateSize, 100);
        });
      }
    },
    refresh: function () {
      this.displayLat = null;
      this.displayLng = null;
    },
  },

  computed: {},

  methods: {
    invalidateSize() {
      let mapObject = (this.$refs.map as any).mapObject;
      (mapObject as Vue & { invalidateSize(): void }).invalidateSize();
    },

    // markerPosition is array if not moved, as object
    // {lat: number, lng: number} if moved
    saveLocation() {
      let marker = this.markerPosition;
      let markerObject: { lat: number; lng: number };
      if (Array.isArray(marker)) {
        markerObject = { lat: marker[0], lng: marker[1] };
      } else {
        markerObject = marker as { lat: number; lng: number };
      }

      this.displayLat = markerObject.lat;
      this.displayLng = markerObject.lng;

      this.$emit("newData", markerObject);
      this.dialog = false;
    },
  },
});
</script>
