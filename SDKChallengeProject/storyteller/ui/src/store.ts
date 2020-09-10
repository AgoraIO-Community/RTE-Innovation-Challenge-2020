import { writable, readable } from "svelte/store";
import type AgoraRTC from "agora-rtc-sdk";

const defaultRtc: {
  client: AgoraRTC.Client | null;
  localStream: AgoraRTC.Stream | null;
  remoteStreams: Array<AgoraRTC.Stream>;
  channel: string;
  uid: null | string | number;
} = {
  client: null,
  localStream: null,
  remoteStreams: [],
  channel: "",
  uid: null,
};

export const rtc = writable(defaultRtc);

export const staticEnv: {
  AGORA_APP_ID: string;
  AGORA_API_TOKEN: string;
  STORAGE_VENDOR: number;
  STORAGE_REGION: number;
  QINIU_AK: string;
  QINIU_SK: string;
  QINIU_BUCKET: string;
  STORAGE_BUCKET_URL: string;
} = {
  AGORA_APP_ID: globalThis.process.env.AGORA_APP_ID,
  AGORA_API_TOKEN: globalThis.process.env.AGORA_API_TOKEN,
  STORAGE_VENDOR: parseInt(globalThis.process.env.STORAGE_VENDOR, 10),
  STORAGE_REGION: parseInt(globalThis.process.env.STORAGE_REGION, 10),
  QINIU_AK: globalThis.process.env.QINIU_AK,
  QINIU_SK: globalThis.process.env.QINIU_SK,
  QINIU_BUCKET: globalThis.process.env.QINIU_BUCKET,
  STORAGE_BUCKET_URL: globalThis.process.env.STORAGE_BUCKET_URL,
};
export const env = readable(staticEnv, () => {});
