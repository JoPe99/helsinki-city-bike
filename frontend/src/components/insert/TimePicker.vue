<template>
  <v-menu
    ref="menu"
    v-model="menu"
    :close-on-content-click="false"
    :nudge-right="60"
    :return-value.sync="time"
    transition="scale-transition"
    offset-y
    max-width="290px"
    min-width="290px"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-text-field
        v-model="time"
        :label="label"
        readonly
        outlined
        v-bind="attrs"
        v-on="on"
        color="highlight"
      ></v-text-field>
    </template>
    <v-time-picker
      v-if="menu"
      v-model="time"
      full-width
      use-seconds
      format="24hr"
      @click:second="save(time)"
    ></v-time-picker>
  </v-menu>
</template>

<script lang="ts">
import Vue from "vue";
import { defineComponent } from "vue";

export default defineComponent({
  name: "TimePicker",
  props: ["id", "defaultTime", "label", "refresh"],

  mounted() {
    this.time = this.defaultTime;
  },

  data: () => ({
    menu: false,
    time: "" as string,
  }),

  watch: {
    refresh: function () {
      this.time = this.defaultTime;
    },
  },

  methods: {
    save(time: string) {
      let menuElement = this.$refs.menu;
      (menuElement as Vue & { save: (v: string) => boolean }).save(time);

      this.$emit("newData", this.$props.id, time);
      this.time = time;
    },
  },
});
</script>
