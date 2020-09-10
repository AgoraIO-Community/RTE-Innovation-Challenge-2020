import { staticEnv } from "./store";

const URL = `https://api.agora.io/v1/apps/${staticEnv.AGORA_APP_ID}/cloud_recording`;

const headers = {
  Authorization: `Basic ${staticEnv.AGORA_API_TOKEN}`,
  "Content-Type": "application/json",
};

export async function acquire(cname: string): Promise<string> {
  const res = await fetch(`${URL}/acquire`, {
    method: "POST",
    body: JSON.stringify({
      cname,
      uid: "1",
      clientRequest: {
        resourceExpiredHour: 48,
      },
    }),
    headers,
  });
  const { resourceId } = await res.json();
  return resourceId;
}

export async function start(
  cname: string,
  resourceId: string,
  { uids }: { uids: Array<string | number> }
): Promise<string> {
  const stringUids = uids.map((u) => String(u));
  const res = await fetch(`${URL}/resourceid/${resourceId}/mode/mix/start`, {
    method: "POST",
    body: JSON.stringify({
      cname,
      uid: "1",
      clientRequest: {
        recordingConfig: {
          streamTypes: 2,
          channelType: 0,
          transcodingConfig: {
            height: 360,
            width: 360,
            bitrate: 400,
            fps: 30,
            mixedVideoLayout: 1,
            backgroundColor: "#ffffff",
          },
          subscribeVideoUids: stringUids,
          subscribeAudioUids: stringUids,
          subscribeUidGroup: 1,
        },
        recordingFileConfig: {
          avFileType: ["hls"],
        },
        storageConfig: {
          accessKey: staticEnv.QINIU_AK,
          region: staticEnv.STORAGE_REGION,
          bucket: staticEnv.QINIU_BUCKET,
          secretKey: staticEnv.QINIU_SK,
          vendor: staticEnv.STORAGE_VENDOR,
          fileNamePrefix: ["storyteller", "video"],
        },
      },
    }),
    headers,
  });
  const { sid } = await res.json();
  return sid;
}

export async function query(resourceId: string, sid: string) {
  const res = await fetch(
    `${URL}/resourceid/${resourceId}/sid/${sid}/mode/mix/query`,
    {
      headers,
    }
  );
  return await res.json();
}

export async function stop(
  resourceId: string,
  sid: string,
  cname: string
): Promise<string> {
  const res = await fetch(
    `${URL}/resourceid/${resourceId}/sid/${sid}/mode/mix/stop`,
    {
      method: "POST",
      headers,
      body: JSON.stringify({
        cname,
        uid: "1",
        clientRequest: {},
      }),
    }
  );

  return (await res.json())?.serverResponse?.fileList;
}
