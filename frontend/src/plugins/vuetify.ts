import Vue from "vue";
import Vuetify from "vuetify/lib/framework";
import colors from "vuetify/lib/util/colors";

Vue.use(Vuetify);

export default new Vuetify({
  theme: {
    dark: true,
    themes: {
      light: {
        primary: colors.blue.darken1,
        secondary: colors.blue,
        accent: colors.pink.darken1,
        error: colors.red.accent3,
        background: colors.indigo.lighten5,
        info: colors.teal.darken1,
        highlight: colors.shades.white,
      },
      dark: {
        primary: colors.blue.darken4,
        secondary: colors.blue.darken3,
        background: colors.indigo.base,
        info: colors.teal.lighten1,
        highlight: colors.shades.white,
      },
    },
  },
});
