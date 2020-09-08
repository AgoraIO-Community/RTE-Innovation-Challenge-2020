import type { eventWithTime } from "rrweb/typings/types";

function padZero(num: number, len = 2): string {
  let str = String(num);
  const threshold = Math.pow(10, len - 1);
  if (num < threshold) {
    while (String(threshold).length > str.length) {
      str = "0" + num;
    }
  }
  return str;
}

const SECOND = 1000;
const MINUTE = 60 * SECOND;
const HOUR = 60 * MINUTE;
export function formatTime(ms: number): string {
  if (ms <= 0) {
    return "00:00";
  }
  const hour = Math.floor(ms / HOUR);
  ms = ms % HOUR;
  const minute = Math.floor(ms / MINUTE);
  ms = ms % MINUTE;
  const second = Math.floor(ms / SECOND);
  if (hour) {
    return `${padZero(hour)}:${padZero(minute)}:${padZero(second)}`;
  }
  return `${padZero(minute)}:${padZero(second)}`;
}

export type Tooltip = {
  content: string;
  timeOffset: number;
  id: number;
};

export type Skip = {
  timeOffset: number;
  duration: number;
};

export type Scene = {
  id: string;
  name: string;
  type: "web";
  url: string;
  events: eventWithTime[];
  tooltips: Tooltip[];
  skips?: Skip[];
  totalTime: number;
};

export type Chapter = {
  id: string;
  name: string;
  sequence: Array<Scene>;
};

export const newScene: Scene = {
  id: "",
  name: "new scene",
  type: "web",
  url: null,
  events: [],
  tooltips: [],
  skips: [],
  totalTime: 0,
};
