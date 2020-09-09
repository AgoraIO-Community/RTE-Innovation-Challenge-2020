<template>
  <div id="app">
    <Home
      msg="Agora Basic Video Call"
      @join="view = 'room'"
      v-if="view === 'home'"
    />
    <Room
      msg="Agora Basic Video Call"
      @leave="view = 'home'"
      v-else-if="view === 'room'"
    />
    <h4 v-else>初始化...</h4>
  </div>
</template>

<script>
import Home from "./components/Home.vue";
import Room from "./components/Room.vue";
import { initClient } from "./share";

export default {
  name: "App",
  components: {
    Home,
    Room,
  },
  data() {
    return {
      view: "",
    };
  },
  methods: {
    toRoom() {
      this.view = "room";
    },
  },
  created() {
    initClient().then((client) => {
      if (client) {
        this.view = "home";
      }
    });
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
