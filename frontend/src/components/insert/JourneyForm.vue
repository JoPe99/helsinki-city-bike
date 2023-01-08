<template>
  <v-card color="primary" class="pa-4 elevation-4 fill-height">
    <!-- Status message -->
    <v-snackbar
      v-model="insertResultMessage.show"
      absolute
      top
      right
      :color="insertResultMessage.color"
      timeout="2000"
    >
      <span v-if="insertResultMessage.success">{{
        insertResultMessage.text
      }}</span>
      <span v-else>{{ insertResultMessage.text }}</span>
      <v-icon v-if="insertResultMessage.success" dark>
        mdi-checkbox-marked-circle
      </v-icon>
    </v-snackbar>

    <!-- Form items -->
    <v-container class="py-1 fill-height">
      <v-row>
        <v-col>
          <v-card-title class="py-3 px-0">Insert journey</v-card-title>
        </v-col>
        <v-col align="end">
          <v-btn color="info" @click="clearForm">Clear form</v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <date-picker
            id="departure"
            label="Departure date"
            :defaultDate="defaultDate"
            :refresh="refreshPickers"
            @newData="handleNewDate"
          ></date-picker>
        </v-col>
        <v-col>
          <time-picker
            id="departure"
            :default-time="defaultTime"
            label="Departure time"
            :refresh="refreshPickers"
            @newData="handleNewTime"
          ></time-picker>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <date-picker
            id="return"
            label="Return date"
            :defaultDate="defaultDate"
            :refresh="refreshPickers"
            @newData="handleNewDate"
          ></date-picker>
        </v-col>
        <v-col>
          <time-picker
            id="return"
            :default-time="defaultTime"
            label="Return time"
            :refresh="refreshPickers"
            @newData="handleNewTime"
          ></time-picker>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
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
                <v-list-item-subtitle>{{
                  data.item.nameFi
                }}</v-list-item-subtitle>
              </v-list-item-content>
            </template>
          </v-autocomplete>
        </v-col></v-row
      >
      <v-row>
        <v-col>
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
                <v-list-item-subtitle>{{
                  data.item.nameFi
                }}</v-list-item-subtitle>
              </v-list-item-content>
            </template>
          </v-autocomplete>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-text-field
            label="Distance"
            outlined
            color="highlight"
            v-model="distanceMetres"
            hint="Distance covered as meters. Max value allowed is 10000km."
            type="number"
            min="10"
            suffix="meters"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-text-field
            readonly
            disabled
            label="Duration"
            outlined
            color="highlight"
            v-model="getFormattedDuration"
            suffix="Duration"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col align="end">
          <v-btn
            :disabled="submitDisabled"
            color="submit"
            @click="submitJourney"
            >Submit journey</v-btn
          >
        </v-col>
      </v-row>
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
    defaultDate: "2021-05-01",
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

    insertResultMessage: {
      show: false,
      color: "error",
      success: false,
      text: "",
    },
    submitDisabled: false,
    refreshPickers: false,
  }),

  computed: {
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

    getFormattedDuration(): string {
      let seconds = this.getSecondsBetweenDates(
        this.departureDateTime!,
        this.returnDateTime!
      );
      return formatSeconds(seconds);
    },
  },

  methods: {
    submitJourney() {
      this.submitDisabled = true;
      if (this.formJourney()) {
        console.log("Submitting", this.currentJourney);
        this.postJourneyToAPI(this.currentJourney);
      } else {
        this.submitDisabled = false;
      }
    },

    formJourney() {
      this.currentJourney.departureTime = this.formDateTimeString("departure");
      this.currentJourney.returnTime = this.formDateTimeString("return");
      this.currentJourney.durationSeconds = this.formDuration();
      this.currentJourney.distanceCovered = Number(this.distanceMetres);

      // Station data (ID/Name) is inserted into
      // currentJourney inside these functions
      this.formStationData("departure");
      this.formStationData("return");

      return this.validateJourney(this.currentJourney);
    },

    validateJourney(journey: InsertJourneyType): boolean {
      // Check dates to be correct
      let departTime = this.formDateTimeFromString(journey.departureTime);
      let returnTime = this.formDateTimeFromString(journey.returnTime);

      console.log(journey);

      if (returnTime < departTime) {
        this.showStatusMessage(
          false,
          "Return time can't be before departure time"
        );
        return false;
      }

      if (this.getSecondsBetweenDates(returnTime, departTime) < 10) {
        this.showStatusMessage(false, "Journey can't be less than 10 seconds");
        return false;
      }

      if (typeof this.currentJourney.departureStationId == "undefined") {
        this.showStatusMessage(false, "Departure station not defined");
        return false;
      }

      if (typeof this.currentJourney.returnStationId == "undefined") {
        this.showStatusMessage(false, "Return station not defined");
        return false;
      }

      if (this.currentJourney.distanceCovered < 10) {
        this.showStatusMessage(false, "Distance can't be less than 10 metres");
        return false;
      }

      if (this.currentJourney.distanceCovered > 10 ** 7) {
        this.showStatusMessage(false, "Distance can't be more than 10000km.");
        return false;
      }

      return true;
    },

    showStatusMessage(success: boolean, message: string) {
      if (success) {
        this.insertResultMessage.color = "success";
      } else {
        this.insertResultMessage.color = "error";
      }
      this.insertResultMessage.text = message;
      this.insertResultMessage.show = true;
    },

    postJourneyToAPI(journey: InsertJourneyType) {
      insertJourney(journey).then((response) => {
        // Handle response here
        this.updateStore();
      });
    },

    // Update store after journey has been inserted
    updateStore() {
      this.store.updateStoreJourneyData().then(() => {
        this.showStatusMessage(true, "Journey inserted!");
        this.clearForm();
        this.submitDisabled = false;
      });
    },

    clearForm() {
      this.currentJourney = {} as InsertJourneyType;

      this.datesToDefault();

      this.departureStation = null as StationType | null;
      this.returnStation = null as StationType | null;

      this.distanceMetres = 0;

      // Pickers take this as a prop and change
      // back to default dates after change
      this.refreshPickers = !this.refreshPickers;
    },

    datesToDefault() {
      this.departureDate = this.defaultDate;
      this.returnDate = this.defaultDate;

      this.departureTime = this.defaultTime;
      this.returnTime = this.defaultTime;

      this.departureDateTime = this.formDateTimeFromString(
        this.formDateTimeString("departure")
      );
      this.returnDateTime = this.formDateTimeFromString(
        this.formDateTimeString("return")
      );
    },

    // Combines date and time to create a ISO 8601 string
    formDateTimeString(type: "departure" | "return") {
      if (type == "departure") {
        return (this.currentJourney.departureTime = `${this.departureDate}T${this.departureTime}`);
      } else {
        return (this.currentJourney.returnTime = `${this.returnDate}T${this.returnTime}`);
      }
    },

    // Forms Date from ISO 8601 string for easy comparisons
    formDateTimeFromString(datetimeString: string) {
      return Date.parse(datetimeString);
    },

    // Gets station data from dropdown pickers
    formStationData(type: "departure" | "return") {
      if (type == "departure") {
        if (this.departureStation != null) {
          this.currentJourney.departureStationId = this.departureStation
            .id as number;
          this.currentJourney.departureStationName = this.departureStation
            .nameFi as string;
        }
      }
      if (type == "return") {
        if (this.returnStation != null) {
          this.currentJourney.returnStationId = this.returnStation.id as number;
          this.currentJourney.returnStationName = this.returnStation
            .nameFi as string;
        }
      }
    },

    // Forms duration of journey during submit
    formDuration() {
      let departDate = this.formDateTimeFromString(
        this.currentJourney.departureTime
      );
      let returnDate = this.formDateTimeFromString(
        this.currentJourney.returnTime
      );
      let seconds = this.getSecondsBetweenDates(departDate, returnDate);
      return seconds;
    },

    getSecondsBetweenDates(returnDate: Date, departureDate: Date): number {
      return Math.abs(returnDate.getTime() - departureDate.getTime()) / 1000;
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

    // Update datetimes after date/time pickers update
    updateDateTimes() {
      console.log("updating");
      let departureDateTime = this.formDateTimeFromString(
        this.formDateTimeString("departure")
      );
      let returnDateTime = this.formDateTimeFromString(
        this.formDateTimeString("return")
      );

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
