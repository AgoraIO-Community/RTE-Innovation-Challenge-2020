const { record } = require("rrweb");

const events = [];
let stopFn;

function startRecord() {
  events.length = 0;
  stopFn = record({
    emit(event) {
      events.push(event);
    },
  });
}

function stopRecord() {
  stopFn();
}

function getEvents() {
  return events;
}

window.ST = {
  startRecord,
  stopRecord,
  getEvents,
};
