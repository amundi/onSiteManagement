import Vue from "vue";
import { library } from "@fortawesome/fontawesome-svg-core";
import {
  faBars,
  faCalendarDay,
  faCaretDown,
  faCaretUp,
  faFileAlt,
  faBuilding,
  faPoll,
  faTimes,
  faPlus,
  faPlusSquare,
  faMinus,
  faMinusSquare,
  faUserCircle,
  faTimesCircle,
  faChalkboard,
  faSave,
  faSyncAlt,
  faPen,
  faUtensils,
  faCity,
  faLightbulb,
  faCogs,
  faCheck,
  faPlaneArrival,
  faPlaneDeparture,
  faDrumstickBite,
  faParking,
  faArrowRight,
  faMotorcycle,
  faCar,
  faCheckCircle,
  faWalking,
  faUserLock,
  faMapMarkerAlt,
  faBicycle,
  faHouseUser
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import ToggleButton from "vue-js-toggle-button";
import * as d3 from "d3";
import store from "./store";
import router from "./router";
import vSelect from "vue-select";

import App from "./App.vue";

import "@/assets/css/tailwind.css";
import "vue-select/dist/vue-select.css";

Vue.config.productionTip = false;

library.add(
  faBars,
  faCalendarDay,
  faCaretDown,
  faCaretUp,
  faChalkboard,
  faFileAlt,
  faCity,
  faBuilding,
  faPen,
  faPoll,
  faTimes,
  faPlus,
  faTimesCircle,
  faPlusSquare,
  faMinus,
  faMinusSquare,
  faUserCircle,
  faSave,
  faSyncAlt,
  faUtensils,
  faLightbulb,
  faCogs,
  faCheck,
  faCheckCircle,
  faPlaneArrival,
  faPlaneDeparture,
  faDrumstickBite,
  faParking,
  faArrowRight,
  faMotorcycle,
  faCar,
  faBicycle,
  faWalking,
  faUserLock,
  faMapMarkerAlt,
  faHouseUser
);
Vue.component("font-awesome-icon", FontAwesomeIcon);
Vue.use(ToggleButton);
Vue.component("v-select", vSelect);
Vue.use(d3);

new Vue({
  router,
  store,
  render: h => h(App),
  beforeCreate() {
    this.$store.commit("LOAD_LOCALSTORAGE");
  }
}).$mount("#app");
