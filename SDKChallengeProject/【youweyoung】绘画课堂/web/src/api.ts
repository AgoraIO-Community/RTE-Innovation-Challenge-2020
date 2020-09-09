function ajax(method: string, url: string): Promise<[boolean, any?]> {
  const xhr = new XMLHttpRequest();
  xhr.open(method, url);
  return new Promise((resolve) => {
    xhr.onerror = xhr.ontimeout = xhr.onabort = () => resolve([true]);
    xhr.onload = () => {
      let t = xhr.responseText;
      try {
        t = JSON.parse(t);
      } catch (error) {}
      resolve([false, t]);
    };
    xhr.send();
  });
}

export async function getToken(roomId: string, uid: number): Promise<string> {
  const [err, res] = await ajax(
    "GET",
    `http://localhost:8000/token?channelName=${roomId}&uid=${uid}`
  );
  console.log(`----------------------`);
  console.log(`token ${res.key}`);
  console.log(`----------------------`);
  return err ? null : res.key;
  return "006c5002f33fc8341c994ca66e208e99855IACwHl7x9IhkfiCecwdTOwHamoA4Nycx6viRSCxYFdn4jgx+f9gAAAAAEABfsrtbokJWXwEAAQCiQlZf+X66YZuy4YBrg5/J9mMdWsploSFFTRgx+f9gAAAAAEABfsrtbnEBWXwEAAQCcQFZf";
}
interface IRoom {
  id: string;
  uid: number;
  members: number[];
  name: string;
}

const rooms: IRoom[] = [
  {
    uid: 88888888,
    members: [],
    id: "test",
    name: "测试房间",
  },
];

export async function createRoom(uid: number, name = "测试房间") {
  rooms.push({
    uid,
    members: [],
    id: "test",
    name,
  });
}

export async function joinRoom(roomId: string, uid: number) {
  const room = rooms.find((el) => el.id === roomId);
  if (!room) {
    return false;
  }
  const members = room.members;
  const i = members.findIndex((el) => el === uid);
  if (i > -1) {
    members.splice(i);
  }
}

export async function getRooms() {
  return rooms.slice(0);
}
