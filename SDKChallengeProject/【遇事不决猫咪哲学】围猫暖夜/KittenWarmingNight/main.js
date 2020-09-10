import Vue from 'vue'
import uView from "uview-ui";
import store from './store';
Vue.use(uView);
import App from './App'

let vuexStore = require("@/store/$u.mixin.js")
Vue.mixin(vuexStore)
Vue.config.productionTip = false

App.mpType = 'app'

const app = new Vue({
	store,
    ...App
})
app.$mount()
