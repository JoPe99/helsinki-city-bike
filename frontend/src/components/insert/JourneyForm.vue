<template>
  <v-card color="primary" class="pa-4 elevation-4 fill-height">
    <v-container class="py-1">
      <v-card-title class="py-3 px-0">Insert journey</v-card-title>

      <div class="py-5 px-0">
        <date-picker
          id="departure"
          label="Departure date"
          :defaultDate="defaultDate"
          @newData="handleNewDate"
        ></date-picker>
      </div>

      <time-picker
        id="departure"
        default-time="00:00:00"
        label="Departure time"
        @newData="handleNewTime"
      ></time-picker>

      <div class="py-5 px-0">
        <date-picker
          id="return"
          label="Return date"
          :defaultDate="defaultDate"
          @newData="handleNewDate"
        ></date-picker>
      </div>

      <time-picker
        id="return"
        default-time="00:00:00"
        label="Return time"
        @newData="handleNewTime"
      ></time-picker>

      <v-autocomplete
        return-object
        v-model="departureStation"
        :items="stations"
        outlined
        color="highlight"
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
        color="highlight"
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
        color="highlight"
        v-model="distanceMetres"
        hint="Distance covered as meters"
        type="number"
        min="10"
        suffix="meters"
        required
      ></v-text-field>

      <v-text-field
        readonly
        disabled
        label="Duration"
        single-line
        outlined
        color="highlight"
        v-model="getFormatSeconds"
        suffix="Duration"
      ></v-text-field>
      <v-btn @click="submitJourney">Submit journey</v-btn>
    </v-container>
  </v-card>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useStore } from "@/store";
import { InsertJourneyType, StationType } from "@/helpers/backend-data-types";
import TimePicker from "./TimePicker.vue";
import DatePicker from "@/components/DatePicker.vue";
import { formatSeconds } from "@/helpers/list-view-helpers";
import { insertJourney } from "@/helpers/api-functions";

export default defineComponent({
  name: "JourneyForm",
  components: { TimePicker, DatePicker },

  created() {
    this.stations = this.store.stations;
    this.departureDate = this.defaultDate;
    this.returnDate = this.defaultDate;
    this.departureDateTime = this.formDateTimeFromString(
      this.formDateTimeString("departure")
    );
    this.returnDateTime = this.formDateTimeFromString(
      this.formDateTimeString("return")
    );
  },

  data: () => ({
    store: useStore(),
    defaultTime: "00:00:00",
    stations: null as StationType[] | null,

    currentJourney: {} as InsertJourneyType,
    departureDate: "",
    departureTime: "00:00:00",
    departureDateTime: null as Date | null,

    returnDate: "",
    returnTime: "00:00:00",
    returnDateTime: null as Date | null,

    departureStation: null as StationType | null,
    returnStation: null as StationType | null,

    distanceMetres: 0,
  }),

  computed: {
    defaultDate(): string {
      return this.store.earliestJourney.departureTime.substring(0, 10);
    },

    validDateRange(): boolean {
      let departureDate = this.departureDateTime;
      let returnDate = this.returnDateTime;
      let seconds = this.getSecondsBetweenDates(departureDate!, returnDate!);

      if (departureDate! < returnDate! && seconds >= 10) {
        return true;
      } else {
        return false;
      }
    },

    getFormatSeconds(): string {
      let seconds = this.getSecondsBetweenDates(
        this.departureDateTime!,
        this.returnDateTime!
      );
      return formatSeconds(seconds);
    },
  },

  methods: {
    submitJourney() {
      this.formJourney();
      console.log("Submitting", this.currentJourney);
      this.postJourneyToAPI(this.currentJourney);
    },

    formJourney() {
      this.currentJourney.departureTime = this.formDateTimeString("departure");
      this.currentJourney.returnTime = this.formDateTimeString("return");
      this.currentJourney.durationSeconds = this.formDuration();
      this.currentJourney.distanceCovered = Number(this.distanceMetres);

      // Station data is inserted into
      // currentJourney inside the function
      this.formStationData("departure");
      this.formStationData("return");
    },

    postJourneyToAPI(journey: InsertJourneyType) {
      insertJourney(journey).then((response) => {
        console.log(response);
      });
    },

    formDateTimeString(type: "departure" | "return") {
      if (type == "departure") {
        return (this.currentJourney.departureTime = `${this.departureDate}T${this.departureTime}`);
      } else {
        return (this.currentJourney.returnTime = `${this.returnDate}T${this.returnTime}`);
      }
    },

    formDateTimeFromString(datetimeString: string) {
      return Date.parse(datetimeString);
    },

    formStationData(type: "departure" | "return") {
      if (type == "departure") {
        this.currentJourney.departureStationId = this.departureStation
          ?.id as number;
        this.currentJourney.departureStationName = this.departureStation
          ?.nameFi as string;
      } else {
        this.currentJourney.returnStationId = this.returnStation?.id as number;
        this.currentJourney.returnStationName = this.returnStation
          ?.nameFi as string;
      }
    },

    formDuration() {
      let departDate = this.ISOStringToDate(this.currentJourney.departureTime);
      let returnDate = this.ISOStringToDate(this.currentJourney.returnTime);
      let seconds = this.getSecondsBetweenDates(departDate, returnDate);
      return seconds;
    },

    getSecondsBetweenDates(returnDate: Date, departureDate: Date): number {
      return Math.abs(returnDate.getTime() - departureDate.getTime()) / 1000;
    },

    ISOStringToDate(date: string): Date {
      return Date.parse(date);
    },

    // Add new times to models and update datetimes
    handleNewTime(id: "departure" | "return", time: string) {
      if (id == "departure") {
        this.departureTime = time;
      } else if (id == "return") {
        this.returnTime = time;
      }
      this.updateDateTimes();
    },

    // Add new dates to models and update datetimes
    handleNewDate(id: "departure" | "return", time: string) {
      if (id == "departure") {
        this.departureDate = time;
      } else if (id == "return") {
        this.returnDate = time;
      }
      this.updateDateTimes();
    },

    // Update datetimes from
    updateDateTimes() {
      console.log("updating");
      let departureDateTime = this.formDateTimeFromString(
        this.formDateTimeString("departure")
      );
      let returnDateTime = this.formDateTimeFromString(
        this.formDateTimeString("return")
      );
      if (returnDateTime < departureDateTime) {
        alert("Return time must be after departure time!");
        return;
      }

      this.departureDateTime = departureDateTime;
      this.returnDateTime = returnDateTime;
    },

    // Used to filter data of station pickers
    filterData(station: StationType, search: string) {
      return (
        station.nameFi.toLowerCase().includes(search.toLowerCase()) ||
        station.id.toString().includes(search)
      );
    },
  },
});
</script>
