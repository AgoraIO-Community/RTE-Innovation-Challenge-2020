// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import VueRounter from 'vue-router';
import Login from './components/login/login';
import Room from './components/room/room';

Vue.use(VueRounter);

const routes = [
    {path: '/', redirect: '/login'},
    {path: '/login', component: Login},
    {path: '/room', component: Room}
];
const router = new VueRounter({
    routes
});

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    template: '<App/>',
    components: {App}
});
