import Vue from 'vue';
import App from './App.vue';
import i18n from './i18n';
import { router } from './router';
import store from './store';
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import VeeValidate from 'vee-validate';
import Vuex from 'vuex';
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import VueSimpleAlert from "vue-simple-alert";
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import VueNumberInput from '@chenfengyuan/vue-number-input';
import {
  faHome,
  faUser,
  faUserPlus,
  faSignInAlt,
  faSignOutAlt,
  faHouseUser,
  faGifts,
  faFlag,
  faPlusSquare,
  faKey,
  faCartPlus,
  faCartArrowDown,
  faShoppingCart
} from '@fortawesome/free-solid-svg-icons';

library.add(faHome, faUser, faUserPlus, faSignInAlt, faSignOutAlt, faHouseUser,faGifts,faFlag,faPlusSquare,faKey,faCartPlus,faCartArrowDown,faShoppingCart);

Vue.config.productionTip = false;

Vue.use(VeeValidate);
Vue.use(VueNumberInput);
Vue.component('font-awesome-icon', FontAwesomeIcon);
Vue.use(VueSimpleAlert);
Vue.use(Vuex);
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)

new Vue({
  i18n,
  router,
  store,
  render: h => h(App)
}).$mount('#app');
