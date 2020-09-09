import config from "../../config.json";

export const APPID: string = config.APP_ID;

export const REPLACE_RENDER_INTERVAL: number = 20; // 单位:秒

export const REPLACE_RENDER_FLOAT_INTERVAL: number = 10; // < REPLACE_RENDER_INTERVAL 单位:秒

export const CHANNEL_TIME: number = 24 * 60 * 60; // 单位:秒

export const VIDEO_INDEX: number = 2; // < MAX_RENDER_COUNT - 1

export const MAX_RENDER_COUNT: number = 9; // <= 9
