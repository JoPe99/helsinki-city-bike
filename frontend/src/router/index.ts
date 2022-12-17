import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import HomeView from "../views/HomeView.vue";
import JourneyListView from "../views/JourneyListView.vue";
import StationListView from "../views/StationListView.vue";

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
    component: JourneyListView,
  },
  {
    path: "/stations",
    name: "stations",
    component: StationListView,
  },
];

const router = new VueRouter({
  routes,
});

export default router;
