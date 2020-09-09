const AgoraRTC = window.AgoraRTC;
export const rtc = {
  client: null,
  option: {
    appid: '',//    APP ID
    token: '', //   RoomToken
  },
  uid: null
};
export function initClient(uid) {
  rtc.uid = uid || Math.round(Math.random() * 10000000)
  const client = AgoraRTC.createClient({ mode: "rtc", codec: "h264" });
  return new Promise((res) => {
    client.init(
      rtc.option.appid,
      function () {
        rtc.client = client
        console.log('init client')
        console.log(client)
        res(client);
      },
      (err) => {
        console.log(err);
        res(false);
      }
    );
  });
  // Initialize the client
}
export function joinChannel(channel) {
  const client = rtc.client
  if (!client) {
    return false
  }
  return new Promise(resolve => {
    client.join(rtc.option.token, channel, null, function (uid) {
      console.log("join channel: " + channel + " success, uid: " + uid);
      rtc.uid = uid;
      resolve(uid)
    }, function (err) {
      console.error("client join failed", err);
      resolve(null)
    });
  })
}
export function createStream() {
  const stream = AgoraRTC.createStream({
    streamID: rtc.uid,
    audio: true,
    video: false,
    screen: false,
  });
  console.log(stream);
  return new Promise((res) => {
    stream.init(
      function () {
        res(stream);
      },
      function () {
        res(null);
      }
    );
  });
}
export function genRoom() {
  return {
    uid: Math.round(Math.random() * 10000000),
    channel: "test",
  };
}
setInterval(() => {
  const client = rtc.client
  if (!client) {
    return
  }
  client.getLocalAudioStats((localAudioStats) => {
    // for(var uid in localAudioStats){
    //   console.log(`Audio CodecType from ${uid}: ${localAudioStats[uid].CodecType}`);
    //   console.log(`Audio MuteState from ${uid}: ${localAudioStats[uid].MuteState}`);
    //   console.log(`Audio RecordingLevel from ${uid}: ${localAudioStats[uid].RecordingLevel}`);
    //   console.log(`Audio SamplingRate from ${uid}: ${localAudioStats[uid].SamplingRate}`);
    //   console.log(`Audio SendBitrate from ${uid}: ${localAudioStats[uid].SendBitrate}`);
    //   console.log(`Audio SendLevel from ${uid}: ${localAudioStats[uid].SendLevel}`);
    // }
    console.log(localAudioStats)
  });
}, 10000)