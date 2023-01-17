import Vue from "vue";
import Vuetify from "vuetify/lib/framework";
import colors from "vuetify/lib/util/colors";

Vue.use(Vuetify);

export default new Vuetify({
  theme: {
    dark: true,
    themes: {
      light: {
        primary: colors.purple.lighten3,
        secondary: colors.purple.lighten2,
        submit: colors.green.accent1,
        highlight: colors.shades.black,
      },
      dark: {
        primary: colors.blue.darken4,
        secondary: colors.blue.darken3,
        submit: colors.teal.lighten1,
        highlight: colors.shades.white,
      },
    },
  },
});
