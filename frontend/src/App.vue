<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <v-toolbar-title class="mr-5">Helsinki City Bike</v-toolbar-title>
      <v-btn
        color="secondary"
        class="mx-3"
        :disabled="$route.path == '/' ? true : false"
        @click="$router.push('/')"
      >
        Home
      </v-btn>
      <v-btn
        color="secondary"
        class="mx-3"
        :disabled="$route.path == '/journeys' ? true : false"
        @click="$router.push('/journeys')"
      >
        Journeys
      </v-btn>
      <v-btn
        color="secondary"
        class="mx-3"
        :disabled="$route.path == '/stations' ? true : false"
        @click="$router.push('/stations')"
      >
        Stations
      </v-btn>
      <v-btn
        color="secondary"
        class="mx-3"
        :disabled="$route.path == '/insert' ? true : false"
        @click="$router.push('/insert')"
      >
        Insert data
      </v-btn>
      <v-spacer />
      <v-btn icon @click="toggleTheme()">
        <v-icon :color="themeButtonColor()">mdi-theme-light-dark</v-icon>
      </v-btn>
    </v-app-bar>

    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useStore } from "@/store/index";

export default defineComponent({
  name: "App",

  data: () => ({}),

  created() {
    const store = useStore();
    store.setupStore();
  },

  methods: {
    toggleTheme() {
      this.$vuetify.theme.dark = !this.$vuetify.theme.dark;
    },
    themeButtonColor() {
      return this.$vuetify.theme.dark
        ? "yellow darken-3"
        : "blue-grey darken-4";
    },
  },
});
</script>

<style>
/* Take 100% of the available space all times
 * and don't allow scrolling pages 
*/
body {
  overflow: hidden;
  height: 100vh;
}

html {
  overscroll-behavior: none;
}
</style>
