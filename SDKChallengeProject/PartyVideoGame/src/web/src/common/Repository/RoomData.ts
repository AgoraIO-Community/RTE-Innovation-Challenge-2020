import ParseServerHelper from "utils/ParseServerHelper";
import { User } from "common/Models/User";
import { RoomModel } from "common/Models/RoomModel";
import Log from "utils/Log";
import { RoomGameSketch } from "common/Models/RoomGameSketch";

export class RoomData {

    private serviceHelper: ParseServerHelper;
    public roomUsers: Array<User>;
    private roomId: string;
    constructor(roomId: string) {
        this.serviceHelper = new ParseServerHelper();
        this.roomUsers = [];
        this.roomId = roomId;
    }

    async createOrJoinRoom(user: User) {
        var room = await this.serviceHelper.dbGetByDocIdAsync<RoomModel>('room', this.roomId);
        if (room) {
            if (!room.roomUsers.find(p => p.id === user.id)) {
                room.roomUsers.push(user);
            }
            await this.serviceHelper.dbAddOrUpdateAsync('room', this.roomId, room);
        } else {
            room = new RoomModel();
            room.ownerUid = user.id;
            room.roomId = this.roomId;
            room.roomUsers.push(user);
            await this.serviceHelper.dbAddOrUpdateAsync('room', this.roomId, room);
        }
    }

    async  leaveRoom(uid: string) {
        var room = await this.serviceHelper.dbGetByDocIdAsync<RoomModel>('room', this.roomId);
        if (room) {
            room.roomUsers = room.roomUsers.filter(p => p.id !== uid);
            await this.serviceHelper.dbAddOrUpdateAsync('room', this.roomId, room);
        }
    }

    async updateRoomGame(roomGameSketch?: RoomGameSketch) {
        var room = await this.serviceHelper.dbGetByDocIdAsync<RoomModel>('room', this.roomId);
        if (room) {
            room.roomGameSketch = roomGameSketch;
            await this.serviceHelper.dbAddOrUpdateAsync('room', this.roomId, room);
        }
    }

    onRoomChange(onChanged: (room: RoomModel) => void) {
        this.serviceHelper.dbChanging<RoomModel>('room', this.roomId, room => {
            Log.Info(room);
            onChanged(room);
        })
    }

    dispose() {
        this.serviceHelper.dispose();
    }
}