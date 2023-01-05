<template>
  <v-container fluid>
    <v-card color="primary" dark>
      <v-card-title>Here are things you can do with this app</v-card-title>
      <v-container fluid>
        <v-row wrap>
          <v-col>
            <v-card color="secondary">
              <v-card-title>Browse journeys</v-card-title>
            </v-card>
          </v-col>
          <v-col>
            <v-card color="secondary">
              <v-card-title>Browse stations</v-card-title>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useStore } from "../../store/index";

export default defineComponent({
  name: "ActionsList",
  data: () => ({
    store: useStore(),
  }),
  computed: {},
  methods: {},
});
</script>
