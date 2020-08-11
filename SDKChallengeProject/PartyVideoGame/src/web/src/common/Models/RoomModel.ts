import { User } from "./User";
import { RoomGameSketch } from "./RoomGameSketch";

export class RoomModel {
    roomId!: string;
    ownerUid!: string;
    roomUsers: Array<User> = [];
    roomGameSketch?: RoomGameSketch;
}