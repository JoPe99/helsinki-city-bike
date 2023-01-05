<template>
  <v-container fluid class="fill-height pa-0">
    <v-row no-gutters class="fill-height">
      <v-col class="pa-6 fill-height">
        <journey-form></journey-form>
      </v-col>
      <v-col class="pa-6 fill-height">
        <station-form></station-form>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import JourneyForm from "@/components/insert/JourneyForm.vue";
import StationForm from "@/components/insert/StationForm.vue";

export default defineComponent({
  name: "InsertView",

  components: { JourneyForm, StationForm },
});
</script>
