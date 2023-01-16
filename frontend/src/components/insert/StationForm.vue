<template>
  <v-card color="primary" class="pa-4 elevation-4">
    <!-- Status message -->
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
      <v-icon v-if="insertResultBar.success" dark>
        mdi-checkbox-marked-circle
      </v-icon>
    </v-snackbar>

    <!-- Form items -->
    <v-container class="py-1">
      <v-row>
        <v-col>
          <v-card-title class="py-3 px-0">Insert station</v-card-title>
        </v-col>
        <v-col align="end">
          <v-btn color="info" @click="clearForm">Clear form</v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-text-field
            label="ID"
            outlined
            color="highlight"
            type="number"
            v-model="currentStation.id"
            hint="ID can't be already taken, defaults to first available ID. Allowed values 1-1999."
            min="1"
            max="2000"
          ></v-text-field>
        </v-col>
        <v-col>
          <v-text-field
            label="Name (FI)"
            outlined
            color="highlight"
            v-model="currentStation.nameFi"
            hint="Name in Finnish"
            counter
            maxlength="50"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-text-field
            label="Name (SE)"
            outlined
            color="highlight"
            v-model="currentStation.nameSe"
            hint="Name in Swedish"
            counter
            maxlength="50"
          ></v-text-field>
        </v-col>
        <v-col>
          <v-text-field
            label="Name (EN)"
            outlined
            color="highlight"
            v-model="currentStation.nameEn"
            hint="Name in English"
            counter
            maxlength="50"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-text-field
            label="Address (FI)"
            outlined
            color="highlight"
            v-model="currentStation.addressFi"
            hint="Address in Finnish"
            counter
            maxlength="50"
          ></v-text-field
        ></v-col>
        <v-col>
          <v-text-field
            label="Address (SE)"
            outlined
            color="highlight"
            v-model="currentStation.addressSe"
            hint="Address in Swedish"
            counter
            maxlength="50"
          ></v-text-field
        ></v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-text-field
            label="City (Fi)"
            outlined
            color="highlight"
            v-model="currentStation.cityFi"
            hint="City in Finnish"
            counter
            maxlength="50"
          ></v-text-field>
        </v-col>

        <v-col>
          <v-text-field
            label="City (SE)"
            outlined
            color="highlight"
            v-model="currentStation.citySe"
            hint="City in Swedish"
            counter
            maxlength="50"
          ></v-text-field
        ></v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-text-field
            label="Operator"
            outlined
            color="highlight"
            v-model="currentStation.operator"
            hint="Operator of the station"
            counter
            maxlength="50"
          ></v-text-field
        ></v-col>

        <v-col>
          <v-text-field
            label="Capacity"
            outlined
            color="highlight"
            v-model="currentStation.capacity"
            type="number"
            suffix="bike(s)"
            hint="Capacity of the station. Allowed values 1-500."
          ></v-text-field>
        </v-col>
      </v-row>

      <location-picker
        :refresh="refreshLocationPicker"
        @newData="newLocationData"
      ></location-picker>

      <v-row>
        <v-col align="end">
          <v-btn :disabled="disableSubmit" color="submit" @click="submitStation"
            >Submit station</v-btn
          >
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useStore } from "@/store";
import { StationType } from "@/helpers/backend-data-types";
import { insertStation } from "@/helpers/api-functions";
import LocationPicker from "./LocationPicker.vue";

export default defineComponent({
  name: "StationForm",

  components: { LocationPicker },

  created() {
    this.stationIDs = this.store.stationIDs;
  },

  mounted() {
    this.currentStation.id = this.getFirstFreeStationID();
  },

  data: () => ({
    store: useStore(),
    stationIDs: [] as number[],
    disableSubmit: false,
    refreshLocationPicker: false,
    currentStation: {
      id: 1,
      nameFi: "",
      nameSe: "",
      nameEn: "",
      addressFi: "",
      addressSe: "",
      cityFi: "",
      citySe: "",
      operator: "",
      capacity: 1,
      latitude: "",
      longitude: "",
    } as StationType,

    insertResultBar: {
      show: false,
      color: "error",
      success: false,
      text: "",
    },
  }),

  methods: {
    submitStation() {
      this.disableSubmit = true;
      let validationResult = this.validateStation();

      // If frontend validation successful, post station to API.
      if (validationResult.success) {
        this.postStationToAPI(this.currentStation);
      } else {
        this.showResultBar(false, validationResult.invalidKeys);
        this.disableSubmit = false;
      }
    },

    // Post station to API and update store if successful
    postStationToAPI(station: StationType) {
      insertStation(station).then(() => {
        // Handle response here too.
        this.updateStoreData();
      });
    },

    // Update store and clear up form for new station
    updateStoreData() {
      this.store.updateStoreStationData().then(() => {
        this.stationIDs = this.store.stationIDs;
        this.disableSubmit = false;
        this.clearForm();
        this.showResultBar(true, []);
        // Notifying InsertView to update stations on JourneyForm
        this.$emit("storeUpdated");
      });
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

      // Reject if ID already taken or outside [1, 2000], and
      // verify that it is a whole number.
      if (
        this.stationIDs.includes(Number(currentStation.id)) ||
        currentStation.id > 2000 ||
        currentStation.id < 1 ||
        currentStation.id % 1 != 0
      ) {
        validationStatus.success = false;
        validationStatus.invalidKeys.push("Station ID");
      }

      // Reject if capacity not in range [1, 500] or not whole number.
      if (
        currentStation.capacity > 500 ||
        currentStation.capacity < 1 ||
        currentStation.capacity % 1 != 0
      ) {
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

    newLocationData(location: { lat: number; lng: number }) {
      this.currentStation.latitude = location.lat.toString();
      this.currentStation.longitude = location.lng.toString();
    },

    clearForm() {
      let currentStation = this.currentStation;
      currentStation.id = this.getFirstFreeStationID();
      currentStation.nameFi = "";
      currentStation.nameSe = "";
      currentStation.nameEn = "";
      currentStation.addressFi = "";
      currentStation.addressSe = "";
      currentStation.cityFi = "";
      currentStation.citySe = "";
      currentStation.operator = "";
      currentStation.capacity = 1;
      currentStation.latitude = "";
      currentStation.longitude = "";

      // Could be done by refs, but would be unnecessarily complicated
      // for such a simple thing
      this.refreshLocationPicker = !this.refreshLocationPicker;
    },

    getFirstFreeStationID() {
      let counter = 1;
      while (counter < 2000) {
        if (!this.stationIDs.includes(counter)) {
          return counter;
        }
        counter++;
      }

      // If all IDs are taken, alert user.
      alert("All IDs are taken.");
      return 0;
    },
  },
});
</script>
