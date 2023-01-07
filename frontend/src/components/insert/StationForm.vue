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
      <v-icon v-if="insertResultBar.success" dark>
        mdi-checkbox-marked-circle
      </v-icon>
    </v-snackbar>

    <v-container class="py-1">
      <v-row>
        <v-col>
          <v-card-title class="py-3 px-0">Insert station</v-card-title>
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
            hint="ID, can't be already taken. Allowed values 1-1999."
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
            suffix="bikes"
            hint="Capacity of the station. Allowed values 1-500."
          ></v-text-field>
        </v-col>
      </v-row>

      <location-picker @newData="newLocationData"></location-picker>

      <v-row>
        <v-col>
          <v-btn color="submit" @click="submitStation">Submit station</v-btn>
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

  data: () => ({
    store: useStore(),
    stationIDs: [] as number[],

    currentStation: {
      id: 516,
      nameFi: "",
      nameSe: "",
      nameEn: "",
      addressFi: "",
      addressSe: "",
      cityFi: "",
      citySe: "",
      operator: "",
      capacity: 0,
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
    newLocationData(location: { lat: number; lng: number }) {
      this.currentStation.latitude = location.lat.toString();
      this.currentStation.longitude = location.lng.toString();
    },
    postStationToAPI(station: StationType) {
      console.log("Posting station", station);
      insertStation(station).then((response) => {
        console.log(response);
        this.updateStoreData();
      });
    },

    updateStoreData() {
      this.store.updateStoreStationData().then(() => {
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

      // Reject if ID already taken or outside [1, 2000], and that
      // it is a whole number.
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
  },
});
</script>
