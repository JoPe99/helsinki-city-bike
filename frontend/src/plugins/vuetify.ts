import Vue from "vue";
import Vuetify from "vuetify/lib/framework";
import colors from "vuetify/lib/util/colors";

Vue.use(Vuetify);

export default new Vuetify({
  theme: {
    dark: false,
    themes: {
      light: {
        primary: colors.green.lighten3,
        secondary: colors.green.darken1,
        accent: colors.pink.darken1,
        error: colors.red.accent3,
        background: colors.indigo.lighten5,
        info: colors.teal.darken1,
        submit: colors.green.accent1,
        highlight: colors.shades.black,
      },
      dark: {
        primary: colors.blue.darken4,
        secondary: colors.blue.darken3,
        background: colors.indigo.base,
        submit: colors.teal.lighten1,
        highlight: colors.shades.white,
      },
    },
  },
});
