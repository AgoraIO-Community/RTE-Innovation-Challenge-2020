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
