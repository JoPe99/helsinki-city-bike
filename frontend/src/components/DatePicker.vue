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
        v-model="formattedDate"
        readonly
        :label="label"
        v-bind="attrs"
        outlined
        v-on="on"
        hide-details
        color="highlight"
      ></v-text-field>
    </template>
    <v-date-picker
      v-model="date"
      no-title
      :min="minDate"
      :max="maxDate"
      scrollable
    >
      <v-spacer></v-spacer>
      <v-btn text color="primary" @click="menu = false"> Cancel </v-btn>
      <v-btn text color="primary" @click="saveDate(date)"> OK </v-btn>
    </v-date-picker>
  </v-menu>
</template>

<script lang="ts">
import Vue from "vue";
import { defineComponent } from "vue";

export default defineComponent({
  name: "DatePicker",

  props: ["id", "label", "defaultDate", "refresh"],

  mounted() {
    this.date = this.$props.defaultDate;
  },

  data: () => ({
    date: new Date().toISOString().substring(0, 10),
    minDate: "2021-01-01",
    maxDate: "2021-12-31",
    menu: false,
  }),

  watch: {
    refresh: function () {
      this.date = this.defaultDate;
    },
  },

  computed: {
    // Formatting date to dd-mm-yyyy to make it more readable
    formattedDate(): string {
      return Date.parse(this.date).toString("d.M.yyyy");
    },
  },

  methods: {
    saveDate(date: string) {
      let menuElement = this.$refs.menu;
      if (menuElement) {
        (menuElement as Vue & { save: (date: string) => boolean }).save(date);
        this.$emit("newData", this.$props.id, date);
      }
    },
  },
});
</script>
