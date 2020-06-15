import Vue from "vue";
import VueRouter from "vue-router";
import Home from "@/views/Home.vue";
import Settings from "@/views/Settings.vue";
import Sites from "@/views/Sites.vue";
import Restaurants from "@/views/Restaurants.vue";
import Parkings from "@/views/Parkings.vue";
import Reports from "@/views/Reports.vue";
import Denied from "@/views/Denied.vue";
import store from "../store/index";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/settings",
    name: "Settings",
    component: Settings,
    beforeEnter: (to, from, next) => {
      store.dispatch("loadSettings");
      store.dispatch("loadUserRoles");
      next();
    }
  },
  {
    path: "/sites",
    name: "Sites",
    component: Sites
  },
  {
    path: "/restaurants",
    name: "Restaurants",
    component: Restaurants
  },
  {
    path: "/parkings",
    name: "Parkings",
    component: Parkings
  },
  {
    path: "/reports",
    name: "Reports",
    component: Reports
  },
  {
    path: "/denied",
    name: "Denied",
    component: Denied
  }
  // {
  //   path: "/about",
  //   name: "About",
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ "../views/About.vue")
  // }
];

const router = new VueRouter({
  routes
});

export default router;
