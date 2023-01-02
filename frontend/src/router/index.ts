import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import HomeView from "../views/HomeView.vue";
import JourneyView from "../views/JourneyView.vue";
import StationView from "../views/StationView.vue";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/journeys",
    name: "journeys",
    component: JourneyView,
  },
  {
    path: "/stations",
    name: "stations",
    component: StationView,
  },
];

const router = new VueRouter({
  routes,
});

export default router;
