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
      <v-card-subtitle class="subtitle-2 px-3 pt-1 pb-0">
        {{ title }}
      </v-card-subtitle>
      <v-text-field
        class="pb-3 pt-0 px-3"
        v-model="formattedDate"
        readonly
        v-bind="attrs"
        v-on="on"
        hide-details
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
import { defineComponent } from "vue";

export default defineComponent({
  name: "DatePicker",

  props: ["id", "title", "defaultDate"],

  data: () => ({
    date: new Date().toISOString().substring(0, 10),
    menu: false,
  }),

  mounted() {
    this.date = this.$props.defaultDate;
  },

  watch: {},
  computed: {
    // Formatting date to dd-mm-yyyy to make it more readable
    formattedDate(): string {
      let ret = Date.parse(this.date).toString("d.M.yyyy");
      return ret;
    },
  },

  methods: {
    saveDate(date: string) {
      if (this.$refs.menu) {
        (this.$refs.menu as any).save(date);
        this.$emit("newData", this.$props.id, date);
      }
    },
  },
});
</script>
