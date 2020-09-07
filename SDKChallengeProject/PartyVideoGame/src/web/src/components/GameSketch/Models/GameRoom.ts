import { RoomState } from "./RoomState";

export class GameRoom {
    constructor(gameId: string) {
        this.gameId = gameId;
    }
    gameId: string;
    gameOwnerUid?:string;
    roomState: RoomState = RoomState.waiting;
    round: number = 3;
    roundTime: number = 120;
}
