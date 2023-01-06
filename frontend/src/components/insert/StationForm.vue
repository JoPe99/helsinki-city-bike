<template>
  <v-card color="primary" class="pa-4 elevation-4 fill-height">
    <v-container class="py-1">
      <v-card-title class="py-3 px-0">Insert station</v-card-title>
      <v-text-field
        label="ID"
        single-line
        outlined
        color="black"
        v-model="currentStation.id"
        hint="ID, can't be already taken"
        required
      ></v-text-field>

      <v-text-field
        label="Name (FI)"
        single-line
        outlined
        color="black"
        v-model="currentStation.nameFi"
        hint="Name in Finnish"
        required
      ></v-text-field>

      <v-text-field
        label="Name (SE)"
        single-line
        outlined
        color="black"
        v-model="currentStation.nameSe"
        hint="Name in Swedish"
        required
      ></v-text-field>

      <v-text-field
        label="Name (EN)"
        single-line
        outlined
        color="black"
        v-model="currentStation.nameEn"
        hint="Name in English"
        required
      ></v-text-field>

      <v-text-field
        label="Address (FI)"
        single-line
        outlined
        color="black"
        v-model="currentStation.addressFi"
        hint="Address in Finnish"
        required
      ></v-text-field>
      <v-text-field
        label="Address (SE)"
        single-line
        outlined
        color="black"
        v-model="currentStation.addressSe"
        hint="Address in Swedish"
        required
      ></v-text-field>

      <v-text-field
        label="Operator"
        single-line
        outlined
        color="black"
        v-model="currentStation.operator"
        hint="Operator of the station"
        required
      ></v-text-field>

      <v-text-field
        label="Capacity"
        single-line
        outlined
        color="black"
        v-model="currentStation.capacity"
        suffix="bikes"
        hint="Capacity of the station"
        required
      ></v-text-field>
    </v-container>
  </v-card>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useStore } from "@/store";
import { StationType } from "@/helpers/backend-data-types";

export default defineComponent({
  name: "StationForm",

  components: {},

  mounted() {
    this.getStationIds(this.store.stations);
  },

  data: () => ({
    store: useStore(),
    stationIds: [] as number[],
    currentStation: {} as StationType,
  }),

  methods: {
    getStationIds(stations: StationType[]) {
      console.log(stations);
    },
  },
});
</script>
