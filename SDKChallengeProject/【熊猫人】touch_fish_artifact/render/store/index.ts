import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    token: "",
    uid: "",
    channelName: ""
  },
  mutations: {
    tokenInfo: (state, data) => {
      state.token = data.token;
      state.uid = data.uid;
      state.channelName = data.channelName;
    }
  },
  actions: {
  },
  modules: {
  }
});
