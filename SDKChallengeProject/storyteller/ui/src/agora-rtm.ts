import AgoraRTM from "agora-rtm-sdk";
import pmap from "p-map";
import { staticEnv, users, remoteStory } from "./store";
import type { User } from "./store";
import { sleep } from "./utils";
import type { Story, Effect, Scene } from "./utils";

export type AgoraRtmTransporterOptions = {
  uid: string;
  channelId: string;
};

enum TransporterEvents {
  SYNC_STORY,
  USER_JOINED,
  SYNC_USERS_UI_MAP,
}

type RtmClient = ReturnType<typeof AgoraRTM.createInstance>;
type TransporterEventHandler = (params: {
  event: TransporterEvents;
  payload: any;
}) => void;

type StorySyncPayload =
  | {
      op: "set";
      value: Story;
    }
  | {
      op: "set-story-name";
      value: {
        id: string;
        name: string;
      };
    }
  | {
      op: "set-chapter-name";
      value: {
        id: string;
        name: string;
      };
    }
  | {
      op: "set-scene-name";
      value: {
        id: string;
        chapterId: string;
        name: string;
      };
    }
  | {
      op: "add-scene";
      value: {
        chapterId: string;
        previousId: string | null;
        scene: Scene;
      };
    }
  | {
      op: "update-scene";
      value: {
        chapterId: string;
        scene: Scene;
      };
    }
  | {
      op: "add-effect";
      value: {
        chapterId: string;
        sceneId: string;
        previousIdx: number | null;
        effect: Effect;
      };
    }
  | {
      op: "update-effect";
      value: {
        chapterId: string;
        sceneId: string;
        idx: number;
        effect: Effect;
      };
    }
  | {
      op: "remove-effect";
      value: {
        chapterId: string;
        sceneId: string;
        idx: number;
      };
    };

export class AgoraRtmTransporter {
  handlers: Record<TransporterEvents, Array<TransporterEventHandler>> = {
    [TransporterEvents.SYNC_STORY]: [],
    [TransporterEvents.USER_JOINED]: [],
    [TransporterEvents.SYNC_USERS_UI_MAP]: [],
  };

  client: RtmClient;
  uid: string;
  channelId: string;
  channel: ReturnType<RtmClient["createChannel"]>;

  mid = 0;

  constructor(options: AgoraRtmTransporterOptions) {
    const { uid, channelId } = options;
    this.client = AgoraRTM.createInstance(staticEnv.AGORA_APP_ID, {
      logFilter: AgoraRTM.LOG_FILTER_ERROR,
    });
    this.uid = uid;
    this.channelId = channelId;
  }

  async login(user: User, story: Story) {
    let retry = 5;
    let loginResult;
    let loginError;
    while (retry > 0 || loginResult) {
      retry--;
      try {
        loginResult = await this.client.login({
          uid: this.uid,
        });
        break;
      } catch (error) {
        loginError = error;
        await sleep(1000);
      }
    }
    if (loginError) {
      throw loginError;
    }
    this.channel = this.client.createChannel(this.channelId);
    await this.channel.join();

    const fragmentPool: Record<string, string[]> = {};
    this.channel.on("ChannelMessage", (message) => {
      if (message.messageType !== "TEXT" || !message.text) {
        return;
      }
      let data!: { event: TransporterEvents; payload?: unknown };
      try {
        data = JSON.parse(message.text);
        this.handlers[data.event].map((h) =>
          h({
            event: data.event,
            payload: data.payload,
          })
        );
      } catch (_) {
        const matchedArr = message.text.match(/(\d+)\/(\d+)\/(\d+)_(.+)?/);
        if (!matchedArr) {
          return;
        }
        const [, current, total, id, raw] = matchedArr;
        if (!fragmentPool[id]) {
          fragmentPool[id] = [];
        }
        fragmentPool[id][parseInt(current, 10)] = raw;
        let complete = true;
        let concatRaw = "";
        // check whether every idx in the array was filled
        for (let i = 1; i <= parseInt(total, 10); i++) {
          if (typeof fragmentPool[id][i] !== "string") {
            complete = false;
          } else {
            concatRaw += fragmentPool[id][i];
          }
        }
        if (complete) {
          data = JSON.parse(concatRaw);
          this.handlers[data.event].map((h) =>
            h({
              event: data.event,
              payload: data.payload,
            })
          );
        }
      }
    });

    this.on(TransporterEvents.USER_JOINED, ({ payload }) => {
      users.update((prev) => {
        const alreadyIn = prev.some((u) => u.uid === payload.uid);
        if (alreadyIn) {
          return prev;
        } else {
          this.boradcastSelf(user);
          if (story) {
            this.syncStory({
              op: "set",
              value: story,
            });
          }
          return prev.concat({
            ...payload,
            readonly: true,
          });
        }
      });
    });

    this.on(
      TransporterEvents.SYNC_STORY,
      ({ payload }: { payload: StorySyncPayload }) => {
        switch (payload.op) {
          case "set":
            remoteStory.set(payload.value);
            break;
          case "set-story-name":
            remoteStory.update((prev) => ({
              ...prev,
              name: payload.value.name,
            }));
            break;
          case "set-chapter-name":
            remoteStory.update((prev) => {
              prev.chapters.forEach((c) => {
                if (c.id === payload.value.id) {
                  c.name = payload.value.name;
                }
              });
              return prev;
            });
            break;
          case "set-scene-name":
            remoteStory.update((prev) => {
              prev.chapters.forEach((c) => {
                if (c.id === payload.value.chapterId) {
                  c.sequence.forEach((s) => {
                    if (s.id === payload.value.id) {
                      s.name = payload.value.name;
                    }
                  });
                }
              });
              return prev;
            });
            break;
          case "add-scene":
            remoteStory.update((prev) => {
              prev.chapters.forEach((c) => {
                if (c.id === payload.value.chapterId) {
                  const previousIdx = c.sequence.findIndex(
                    (s) => s.id === payload.value.previousId
                  );
                  if (previousIdx > -1) {
                    c.sequence.splice(previousIdx + 1, 0, payload.value.scene);
                  } else {
                    c.sequence.push(payload.value.scene);
                  }
                }
              });
              return prev;
            });
            break;
          case "update-scene":
            remoteStory.update((prev) => {
              prev.chapters.forEach((c) => {
                if (c.id === payload.value.chapterId) {
                  c.sequence = c.sequence.map((s) => {
                    if (s.id === payload.value.scene.id) {
                      return payload.value.scene;
                    } else {
                      return s;
                    }
                  });
                }
              });
              return prev;
            });
            break;
          case "add-effect":
            console.log(payload);
            remoteStory.update((prev) => {
              prev.chapters.forEach((c) => {
                if (c.id === payload.value.chapterId) {
                  c.sequence.forEach((s) => {
                    if (s.id === payload.value.sceneId) {
                      const { previousIdx } = payload.value;
                      if (previousIdx > -1) {
                        s.effects.splice(
                          previousIdx + 1,
                          0,
                          payload.value.effect
                        );
                      } else {
                        s.effects.push(payload.value.effect);
                      }
                    }
                  });
                }
              });
              return prev;
            });
            break;
          case "update-effect":
            console.log("ue", payload);
            remoteStory.update((prev) => {
              prev.chapters.forEach((c) => {
                if (c.id === payload.value.chapterId) {
                  c.sequence.forEach((s) => {
                    if (s.id === payload.value.sceneId) {
                      const { idx } = payload.value;
                      s.effects[idx] = payload.value.effect;
                      console.log("after", s.effects[idx]);
                    }
                  });
                }
              });
              return prev;
            });
            break;
          case "remove-effect":
            remoteStory.update((prev) => {
              prev.chapters.forEach((c) => {
                if (c.id === payload.value.chapterId) {
                  c.sequence.forEach((s) => {
                    if (s.id === payload.value.sceneId) {
                      const { idx } = payload.value;
                      s.effects.splice(idx, 1);
                    }
                  });
                }
              });
              return prev;
            });
            break;
          default:
            break;
        }
      }
    );

    this.on(
      TransporterEvents.SYNC_USERS_UI_MAP,
      ({ payload }: { payload: User[] }) => {
        const map = payload.reduce<Record<string, User["uiMap"]>>(
          (prev, cur) => {
            prev[cur.uid] = cur.uiMap;
            return prev;
          },
          {}
        );
        users.update((prev) =>
          prev.map((u) => {
            if (u.readonly && u.uid in map) {
              u.uiMap = map[u.uid];
            }
            return u;
          })
        );
      }
    );

    this.channel.on("MemberLeft", (uid) => {
      users.update((prev) => prev.filter((u) => u.uid !== uid));
    });

    this.boradcastSelf(user);
  }

  async syncStory(record: StorySyncPayload) {
    this.mid++;
    const texts =
      JSON.stringify({
        event: TransporterEvents.SYNC_STORY,
        payload: record,
      }).match(/(.|[\r\n]){1,30000}/g) || [];
    await pmap(
      texts,
      async (text, idx) => {
        await this.channel.sendMessage({
          text: `${idx + 1}/${texts.length}/${this.mid}_${text}`,
        });
        await sleep(1000);
      },
      {
        concurrency: 50,
      }
    );
  }

  boradcastSelf(user: User) {
    return this.channel.sendMessage({
      text: JSON.stringify({
        event: TransporterEvents.USER_JOINED,
        payload: user,
      }),
    });
  }

  syncUiMaps(_users: User[]) {
    return this.channel.sendMessage({
      text: JSON.stringify({
        event: TransporterEvents.SYNC_USERS_UI_MAP,
        payload: _users,
      }),
    });
  }

  on(event: TransporterEvents, handler: TransporterEventHandler) {
    this.handlers[event].push(handler);
  }
}
