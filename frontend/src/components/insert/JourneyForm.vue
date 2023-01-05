<template>
  <v-card color="primary" class="elevation-4 fill-height"> Journey </v-card>
</template>

<script lang="ts">
import { InsertJourneyType } from "@/helpers/backend-data-types";
import { defineComponent } from "vue";

export default defineComponent({
  name: "InsertView",

  data: () => ({
    currentJourney: {} as InsertJourneyType,
  }),
});
</script>
