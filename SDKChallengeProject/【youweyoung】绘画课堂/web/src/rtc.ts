import AgoraRTC from "agora-rtc-sdk";
import * as config from "./config";
import { getToken } from "./api";

interface IShareData {
  client?: AgoraRTC.Client;
  uid?: number;
}
enum Role {
  teacher,
  student,
  visitor,
}
export const rtc: IShareData = {};

export function init(): Promise<AgoraRTC.Client | null> {
  const client = AgoraRTC.createClient({ mode: "rtc", codec: "h264" });
  return new Promise((resolve) => {
    client.init(
      config.appid,
      () => {
        rtc.client = client;
        console.log(client);
        resolve(client);
      },
      (err) => {
        console.log(err);
        resolve(null);
      }
    );
  });
}

export function joinRoom(
  roomId: string,
  uid: number,
  role: Role
): Promise<boolean> {
  return new Promise(async (resolve) => {
    const { client } = rtc;
    if (!client) {
      return resolve(false);
    }
    const token = await getToken(roomId,uid);
    if (!token) {
      resolve(false);
    }
    client.join(
      token,
      roomId,
      uid,
      function(uid) {
        rtc.uid = uid as number;
        resolve(true);
      },
      function(err) {
        console.error("join failed");
        console.log(err);
        resolve(false);
      }
    );
  });
}
export function leaveRoom(roomId: string, role: Role) {}

export function wrapCanvasStream(stream: MediaStream) {
  return new Promise((resolve) => {
    const t = AgoraRTC.createStream({
      audio: true,
      video: true,
      videoSource: stream.getVideoTracks()[0] as AgoraRTC.MediaStreamTrack,
    });
    t.init(
      function() {
        resolve(t);
      },
      function() {
        resolve(null);
      }
    );
  });
}
