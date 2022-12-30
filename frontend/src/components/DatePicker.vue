<template>
  <v-menu
    ref="menu"
    v-model="menu"
    :close-on-content-click="false"
    :return-value.sync="date"
    transition="scale-transition"
    offset-y
    min-width="auto"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-text-field
        v-model="date"
        prepend-icon="mdi-calendar"
        readonly
        v-bind="attrs"
        v-on="on"
      ></v-text-field>
    </template>
    <v-date-picker v-model="date" no-title scrollable>
      <v-spacer></v-spacer>
      <v-btn text color="primary" @click="menu = false"> Cancel </v-btn>
      <v-btn text color="primary" @click="saveDate(date)"> OK </v-btn>
    </v-date-picker>
  </v-menu>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  name: "DatePicker",

  data: () => ({
    date: new Date(Date.now()).toISOString().substring(0, 10),
    menu: false,
  }),

  methods: {
    saveDate(date: any) {
      if (this.$refs.menu) {
        (this.$refs.menu as any).save(date);
      }
    },
  },
});
</script>
