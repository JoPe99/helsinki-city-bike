<template>
  <v-simple-table dense>
    <template v-slot:default>
      <thead>
        <tr>
          <th class="text-left">Name</th>
          <th class="text-left">Journeys</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in topStations" :key="item.name">
          <td>{{ item.name }}</td>
          <td>{{ item.count }}</td>
        </tr>
      </tbody>
    </template>
  </v-simple-table>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "TopStationsTable",
  props: ["topStations"],

  data: () => ({}),

  computed: {},

  methods: {},
});
</script>
