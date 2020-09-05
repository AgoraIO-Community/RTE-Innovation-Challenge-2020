import FirebaseHelper from "utils/FirebaseHelper";
import { User } from "common/Models/User";
import { RoomModel } from "common/Models/RoomModel";
import Log from "utils/Log";
import { RoomGameSketch } from "common/Models/RoomGameSketch";

export class RoomData {

    private firebaseHelper: FirebaseHelper;
    public roomUsers: Array<User>;
    private roomId: string;
    constructor(roomId: string) {
        this.firebaseHelper = new FirebaseHelper();
        this.roomUsers = [];
        this.roomId = roomId;
    }

    async createOrJoinRoom(user: User) {
        var room = await this.firebaseHelper.dbGetByDocIdAsync<RoomModel>('room', this.roomId);
        if (room) {
            if (!room.roomUsers.find(p => p.id === user.id)) {
                room.roomUsers.push(user);
            }
            await this.firebaseHelper.dbAddOrUpdateAsync('room', this.roomId, room);
        } else {
            room = new RoomModel();
            room.ownerUid = user.id;
            room.roomId = this.roomId;
            room.roomUsers.push(user);
            await this.firebaseHelper.dbAddOrUpdateAsync('room', this.roomId, room);
        }
    }

    async  leaveRoom(uid: string) {
        var room = await this.firebaseHelper.dbGetByDocIdAsync<RoomModel>('room', this.roomId);
        if (room) {
            room.roomUsers = room.roomUsers.filter(p => p.id !== uid);
            await this.firebaseHelper.dbAddOrUpdateAsync('room', this.roomId, room);
        }
    }

    async updateRoomGame(roomGameSketch?: RoomGameSketch) {
        var room = await this.firebaseHelper.dbGetByDocIdAsync<RoomModel>('room', this.roomId);
        if (room) {
            room.roomGameSketch = roomGameSketch;
            await this.firebaseHelper.dbAddOrUpdateAsync('room', this.roomId, room);
        }
    }

    onRoomChange(onChanged: (room: RoomModel) => void) {
        this.firebaseHelper.dbChanging<RoomModel>('room', this.roomId, room => {
            Log.Info(room);
            onChanged(room);
        })
    }

    dispose() {
        this.firebaseHelper.dispose();
    }
}