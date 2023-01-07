<template>
  <v-card color="primary" class="pa-4 elevation-4 fill-height">
    <v-snackbar
      v-model="insertResultBar.show"
      absolute
      top
      right
      :color="insertResultBar.color"
      timeout="2000"
    >
      <span v-if="insertResultBar.success">{{ insertResultBar.text }}</span>
      <span v-else>{{ insertResultBar.text }}</span>
      <v-icon dark> mdi-checkbox-marked-circle </v-icon>
    </v-snackbar>

    <v-container class="py-1">
      <v-card-title class="py-3 px-0">Insert station</v-card-title>
      <v-text-field
        label="ID"
        single-line
        outlined
        color="black"
        type="number"
        v-model="currentStation.id"
        hint="ID, can't be already taken. Allowed values 1-1999."
        max="2000"
      ></v-text-field>

      <v-text-field
        label="Name (FI)"
        single-line
        outlined
        color="black"
        v-model="currentStation.nameFi"
        hint="Name in Finnish"
        counter
        maxlength="50"
      ></v-text-field>

      <v-text-field
        label="Name (SE)"
        single-line
        outlined
        color="black"
        v-model="currentStation.nameSe"
        hint="Name in Swedish"
        counter
        maxlength="50"
      ></v-text-field>

      <v-text-field
        label="Name (EN)"
        single-line
        outlined
        color="black"
        v-model="currentStation.nameEn"
        hint="Name in English"
        counter
        maxlength="50"
      ></v-text-field>

      <v-text-field
        label="Address (FI)"
        single-line
        outlined
        color="black"
        v-model="currentStation.addressFi"
        hint="Address in Finnish"
        counter
        maxlength="50"
      ></v-text-field>
      <v-text-field
        label="Address (SE)"
        single-line
        outlined
        color="black"
        v-model="currentStation.addressSe"
        hint="Address in Swedish"
        counter
        maxlength="50"
      ></v-text-field>

      <v-text-field
        label="City (Fi)"
        single-line
        outlined
        color="black"
        v-model="currentStation.cityFi"
        hint="City in Finnish"
        counter
        maxlength="50"
      ></v-text-field>

      <v-text-field
        label="City (SE)"
        single-line
        outlined
        color="black"
        v-model="currentStation.citySe"
        hint="City in Swedish"
        counter
        maxlength="50"
      ></v-text-field>

      <v-text-field
        label="Operator"
        single-line
        outlined
        color="black"
        v-model="currentStation.operator"
        hint="Operator of the station"
        counter
        maxlength="50"
      ></v-text-field>

      <v-text-field
        label="Capacity"
        single-line
        outlined
        color="black"
        v-model="currentStation.capacity"
        type="number"
        suffix="bikes"
        hint="Capacity of the station. Allowed values 1-500."
      ></v-text-field>

      <!-- TODO: Map location picker for station here  -->
      <!-- https://medium.com/swlh/create-an-interactive-location-selector-with-vue-js-and-leaflet-5808c55b4636 -->

      <v-btn @click="submitStation">Submit station</v-btn>
    </v-container>
  </v-card>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useStore } from "@/store";
import { StationType } from "@/helpers/backend-data-types";
import { insertStation } from "@/helpers/api-functions";

export default defineComponent({
  name: "StationForm",

  components: {},

  created() {
    this.stationIDs = this.store.stationIDs;
  },

  data: () => ({
    store: useStore(),
    stationIDs: [] as number[],

    currentStation: {
      id: 516,
      nameFi: "Joonankatu",
      nameSe: "Joonagatan",
      nameEn: "Joona Street",
      addressFi: "Joonankatu 55",
      addressSe: "Joonagatan 55",
      cityFi: "Kouvola",
      citySe: "Kouvolarna",
      operator: "PeniSoft Oy",
      capacity: 50,
      longitude: "25.045481",
      latitude: "60.151844",
    } as StationType,

    insertResultBar: {
      show: false,
      color: "error",
      success: false,
      text: "",
    },
  }),

  methods: {
    postStationToAPI(station: StationType) {
      console.log("Posting station", station);
      insertStation(station).then((response) => {
        console.log(response);
        this.updateStoreData();
      });
    },

    updateStoreData() {
      this.store.updateStoreStationData().then((response) => {
        this.showResultBar(true, []);
      });
    },

    submitStation() {
      console.log("Submitting station");
      let validationResult = this.validateStation();

      // If frontend validation successful, post station to API.
      if (validationResult.success) {
        console.log("Insert successful");
        this.postStationToAPI(this.currentStation);
      } else {
        console.log("Insert failed");
        this.showResultBar(false, validationResult.invalidKeys);
      }
    },

    showResultBar(success: boolean, failedValues: string[]) {
      if (success) {
        this.insertResultBar.color = "success";
        this.insertResultBar.text = "Insert success!";
        this.insertResultBar.success = true;
        this.insertResultBar.show = true;
      } else {
        this.insertResultBar.color = "error";
        this.insertResultBar.success = false;

        let errorText = "";
        if (failedValues.length > 1) {
          errorText = "Values: ";
          for (let value of failedValues) {
            errorText += ` "${value}" `;
          }
          errorText += "failed validation.";
        } else if (failedValues.length == 1) {
          errorText = `Value ${failedValues[0]} failed validation.`;
        } else {
          errorText = "Insert failed for some other reason.";
        }
        this.insertResultBar.text = errorText;

        this.insertResultBar.show = true;
      }
    },

    validateStation(): { success: boolean; invalidKeys: string[] } {
      let currentStation = this.currentStation;

      let validationStatus = { success: true, invalidKeys: [] as string[] };

      // Reject if ID already taken or outside [1, 2000].
      if (
        this.stationIDs.includes(Number(currentStation.id)) ||
        currentStation.id > 2000 ||
        currentStation.id < 1
      ) {
        validationStatus.success = false;
        validationStatus.invalidKeys.push("Station ID");
      }

      // Capacity has to be in range of [1, 400].
      if (currentStation.capacity > 500 || currentStation.capacity < 1) {
        validationStatus.success = false;
        validationStatus.invalidKeys.push("Capacity");
      }

      // Validate all strings
      (Object.keys(currentStation) as (keyof StationType)[]).forEach((key) => {
        if (typeof currentStation[key] === "string") {
          if ((currentStation[key] as string).length == 0) {
            validationStatus.success = false;
            validationStatus.invalidKeys.push(key);
          }
          if ((currentStation[key] as string).length > 50) {
            validationStatus.success = false;
            validationStatus.invalidKeys.push(key);
          }
        }
      });

      return validationStatus;
    },
  },
});
</script>
