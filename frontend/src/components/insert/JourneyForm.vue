<template>
  <v-card color="primary" class="pa-4 elevation-4 fill-height">
    <v-container class="py-1">
      <v-card-title class="py-3 px-0">Insert journey</v-card-title>

      <v-text-field
        label="Departure date"
        single-line
        outlined
        color="black"
        v-model="departureDate"
        hint="Departure date as 'YYYY-MM-DD'"
        required
      ></v-text-field>

      <v-text-field
        label="Departure time"
        single-line
        outlined
        color="black"
        v-model="departureTime"
        hint="Departure time as 'HH:MM:SS'"
        required
      ></v-text-field>

      <v-text-field
        label="Return date"
        single-line
        outlined
        color="black"
        v-model="returnDate"
        hint="Return date as 'YYYY-MM-DD'"
        required
      ></v-text-field>

      <v-text-field
        label="Return time"
        single-line
        outlined
        color="black"
        v-model="returnTime"
        hint="Return time as 'HH:MM:SS'"
        required
      ></v-text-field>

      <v-autocomplete
        return-object
        v-model="departureStation"
        :items="stations"
        outlined
        color="black"
        label="Departure station"
        :filter="filterData"
        clearable
        required
      >
        <template v-slot:selection="data">
          {{ data.item.id }}: {{ data.item.nameFi }}
        </template>
        <template v-slot:item="data">
          <v-list-item-content>
            <v-list-item-title>{{ data.item.id }}</v-list-item-title>
            <v-list-item-subtitle>{{ data.item.nameFi }}</v-list-item-subtitle>
          </v-list-item-content>
        </template>
      </v-autocomplete>

      <v-autocomplete
        return-object
        v-model="returnStation"
        :items="stations"
        outlined
        color="black"
        label="Return station"
        :filter="filterData"
        clearable
        required
      >
        <template v-slot:selection="data">
          {{ data.item.id }}: {{ data.item.nameFi }}
        </template>
        <template v-slot:item="data">
          <v-list-item-content>
            <v-list-item-title>{{ data.item.id }}</v-list-item-title>
            <v-list-item-subtitle>{{ data.item.nameFi }}</v-list-item-subtitle>
          </v-list-item-content>
        </template>
      </v-autocomplete>

      <v-text-field
        label="Distance"
        single-line
        outlined
        color="black"
        v-model="currentJourney.distanceCovered"
        hint="Distance covered as meters"
        suffix="meters"
        required
      ></v-text-field>

      <v-text-field
        label="Duration"
        single-line
        outlined
        color="black"
        v-model="currentJourney.durationSeconds"
        hint="Duration of the journey in seconds"
        suffix="seconds"
        required
      ></v-text-field>

      <v-btn>Submit journey</v-btn>
    </v-container>
  </v-card>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useStore } from "@/store";
import { InsertJourneyType, StationType } from "@/helpers/backend-data-types";
import { StatOptions } from "fs";

export default defineComponent({
  name: "JourneyForm",

  mounted() {
    this.stations = this.store.stations;
  },

  data: () => ({
    store: useStore(),
    currentJourney: {} as InsertJourneyType,
    stations: null as StationType[] | null,
    departureDate: "",
    departureTime: "",
    returnDate: "",
    returnTime: "",
    departureStation: null as StationType | null,
    returnStation: null as StationType | null,
  }),

  methods: {
    filterData(station: StationType, search: string) {
      return (
        station.nameFi.toLowerCase().includes(search.toLowerCase()) ||
        station.id.toString().includes(search)
      );
    },
  },
});
</script>
