import type { eventWithTime } from "rrweb/typings/types";
import { nanoid } from "nanoid";

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

export type RtcVideo = {
  duration: number;
  timeOffset: number;
  file: string;
};

export type TooltipEffect = {
  type: "tooltip";
  payload: Tooltip;
};
export type SkipEffect = {
  type: "skip";
  payload: Skip;
};
export type RtcVideoEffect = {
  type: "rtc-video";
  payload: RtcVideo;
};

export type Effect = TooltipEffect | SkipEffect | RtcVideoEffect;

export type Scene = {
  id: string;
  name: string;
  type: "web";
  url: string;
  events: eventWithTime[];
  effects: Effect[];
  totalTime: number;
};

export type Chapter = {
  id: string;
  name: string;
  sequence: Array<Scene>;
};

export type Story = {
  id: string;
  name: string;
  chapters: Array<Chapter>;
};

export const genNewScene: () => Scene = () => ({
  id: genId(),
  name: "new scene",
  type: "web",
  url: null,
  events: [],
  effects: [],
  totalTime: 0,
});

export function genId() {
  return nanoid(8);
}

export type User = {
  username: string;
};

export function sleep(ms: number) {
  return new Promise((resolve) =>
    setTimeout(() => {
      resolve();
    }, ms)
  );
}

function djb2(str: string) {
  var hash = 5381;
  for (var i = 0; i < str.length; i++) {
    hash = (hash << 5) + hash + str.charCodeAt(i); /* hash * 33 + c */
  }
  return hash;
}

export function hashStringToColor(str: string) {
  var hash = djb2(str);
  var r = (hash & 0xff0000) >> 16;
  var g = (hash & 0x00ff00) >> 8;
  var b = hash & 0x0000ff;
  return (
    "#" +
    ("0" + r.toString(16)).substr(-2) +
    ("0" + g.toString(16)).substr(-2) +
    ("0" + b.toString(16)).substr(-2)
  );
}

export function invertColor(hex: string) {
  if (hex.indexOf("#") === 0) {
    hex = hex.slice(1);
  }
  // convert 3-digit hex to 6-digits.
  if (hex.length === 3) {
    hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
  }
  if (hex.length !== 6) {
    throw new Error("Invalid HEX color.");
  }
  var r = parseInt(hex.slice(0, 2), 16),
    g = parseInt(hex.slice(2, 4), 16),
    b = parseInt(hex.slice(4, 6), 16);
  return r * 0.299 + g * 0.587 + b * 0.114 > 186 ? "#000000" : "#FFFFFF";
}
