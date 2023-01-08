<template>
  <v-container fluid class="pa-0">
    <v-row no-gutters>
      <v-col class="pa-6 pr-3 fill-width">
        <journey-form></journey-form>
      </v-col>
      <v-col class="pa-6 pl-3">
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
