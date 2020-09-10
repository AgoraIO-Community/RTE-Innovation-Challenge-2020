import { writable, readable, derived } from "svelte/store";
import type AgoraRTC from "agora-rtc-sdk";
import type { Story } from "./utils";
import type { AgoraRtmTransporter } from "./agora-rtm";

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

export type User = {
  username: string;
  readonly: boolean;
  uid: string | number;
  uiMap: {
    sceneId?: string;
  };
};
const defaultUsers: Array<User> = [];

export const users = writable(defaultUsers);

export const remoteStory = writable<Story | null>(null);

export const readonlyUsersMap = derived(users, ($users) => {
  const scenes: Record<string, User[]> = {};
  for (const u of $users) {
    if (u.readonly && u.uiMap.sceneId) {
      if (!scenes[u.uiMap.sceneId]) {
        scenes[u.uiMap.sceneId] = [];
      }
      scenes[u.uiMap.sceneId].push(u);
    }
  }
  return {
    scenes,
  };
});

export const rtm = writable<AgoraRtmTransporter | null>(null);
