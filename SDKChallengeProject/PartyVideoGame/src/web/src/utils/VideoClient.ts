import AgoraRTC from "agora-rtc-sdk";
import Log from "./Log";
import { isSafari } from "./BrowserCheck";

export interface VideoStream {
    id: string;
    stream: AgoraRTC.Stream;
    isLocal: boolean;
    order: number;
}
export default class VideoClient {

    appId: string;
    client: AgoraRTC.Client;
    streamList: Array<VideoStream>;
    private localStream: AgoraRTC.Stream | null;
    onStreamListChanged: (streamList: Array<VideoStream>) => void;
    constructor(appId: string) {
        AgoraRTC.Logger.setLogLevel(2);
        this.appId = appId;
        this.client = AgoraRTC.createClient({ mode: "rtc", codec: "h264" });
        this.streamList = [];
        this.localStream = null;
        this.onStreamListChanged = {} as (streamList: Array<VideoStream>) => void;
    }


    public async create(channel: string, uid: string) {
        await this.initial();
        await this.subscribeEvents();
        await this.joinChannel(channel, uid);
        let stream = await this.createStream(uid);
        this.localStream = stream;
        this.addStreamToList({ id: stream.getId().toString(), stream: stream, order: 0, isLocal: true });
        await this.publishStream(stream);
    }

    public dispose() {
        this.localStream && this.client.unpublish(this.localStream);
        this.localStream && this.localStream.close();
        this.streamList.forEach(item => {
            item.stream.isPlaying() && item.stream.close();
        })
        this.client.leave(() => {
            Log.Info('client left');
        }, error => {
            Log.Error(error);
        })
    }

    private initial(): Promise<AgoraRTC.Client> {
        return new Promise<AgoraRTC.Client>(resolve => {
            this.client.init(this.appId, () => {
                Log.Info("AgoraRTC client initialized");
                resolve(this.client);
            }, (error: any) => {
                Log.Error(error);
            })
        })
    }
    private subscribeEvents() {
        this.client.on('stream-added', evt => {
            let stream = evt.stream;
            if (stream.getId() !== this.localStream?.getId()) {
                let options = isSafari() ? undefined : {};
                this.client.subscribe(stream, options, error => {
                    Log.Error(error);
                })
            }
        });

        this.client.on('stream-subscribed', evt => {
            let stream = evt.stream;
            let maxOrder = Math.max(...this.streamList.map(p => p.order));
            this.addStreamToList({ id: stream.getId().toString(), stream, order: maxOrder + 1, isLocal: false });
        });

        this.client.on("stream-removed", evt => {
            let stream = evt.stream
            this.removeStreamFromList(stream.getId());
        })

        this.client.on('peer-leave', evt => {
            this.removeStreamFromList(evt.uid);
        })


    }

    private joinChannel(channel: string, uid: string): Promise<string> {
        return new Promise<string>(resolve => {
            this.client.join(this.appId, channel, uid, uid => resolve(uid.toString()), error => {
                Log.Error(error);
            })

        })
    }

    private createStream(uid: string): Promise<AgoraRTC.Stream> {
        return new Promise<AgoraRTC.Stream>(resolve => {
            let defaultConfig = {
                streamID: uid,
                audio: true,
                video: true,
                screen: false
            }
            let stream = AgoraRTC.createStream(defaultConfig);
            stream.setVideoProfile('720p_3');
            stream.init(() => {
                resolve(stream);
            }, error => {
                Log.Error(error);
            })
        })
    }

    private publishStream(stream: AgoraRTC.Stream): Promise<void> {
        return new Promise<void>(resolve => {
            this.client.publish(stream, err => {
                Log.Error(err);
            })
            resolve();
        });

    }

    private addStreamToList(stream: VideoStream) {
        this.streamList.push(stream);
        this.onStreamListChanged(this.streamList);
    }

    private removeStreamFromList(streamId: string) {
        this.streamList = this.streamList.filter(p => p.stream.getId().toString() !== streamId);
        this.onStreamListChanged(this.streamList);
    }
}