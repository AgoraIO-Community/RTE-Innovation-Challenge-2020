import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import Home from "../views/Home.vue";
import Fish from "../views/Fish.vue";
import store from "@/store";

Vue.use(VueRouter);

const routes: RouteConfig[] = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/fish",
    name: "Fish",
    component: Fish
  }
];

const router = new VueRouter({
  mode: process.env.IS_ELECTRON ? "hash" : "history",
  base: process.env.BASE_URL,
  routes
});

router.beforeEach((to, from, next) => {
  if (to.name !== "Home" && (typeof store.state.token !== "string" || store.state.token === "")) {
    next({
      path: "/",
      replace: true
    });
  } else {
    next();
  }
});

export default router;
