import Vue from "vue";
import Vuex from "vuex";
import { init } from "../rtc";
import { getRooms, joinRoom } from "@/api";
Vue.use(Vuex);

enum Role {
  teacher,
  student,
  visitor,
}
type rtcStatus = 0 | 1 | 2;
export default new Vuex.Store<{
  uid: null | number;
  role: Role;
  status: rtcStatus;
  rooms: any[];
  targetId: string | null;
}>({
  state: {
    uid: 88888887,
    role: Role.visitor,
    status: 0,
    rooms: [],
    targetId: null,
  },
  mutations: {
    SET_ROLE(state, role: Role) {
      state.role = role;
    },
    SET_ONLINE(state, e: rtcStatus) {
      state.status = e;
    },
    SET_UID(state, uid: number) {
      if (typeof uid !== "number") {
        uid = 2333;
      }
      state.uid = uid;
    },
    SET_ROOMS(state, rooms) {
      state.rooms = rooms;
    },
    SET_TARGET(state, id: string) {
      state.targetId = id;
    },
  },
  getters: {
    room(state) {
      return state.rooms.find((el) => el.id === state.targetId);
    },
  },
  actions: {
    async init(ctx) {
      ctx.commit("SET_ROOMS", await getRooms());
      ctx.commit("SET_ONLINE", !!(await init()) ? 1 : 2);
    },
    joinRoom(ctx, roomId) {
      ctx.commit("SET_TARGET", roomId);
    },
  },
  modules: {},
});
