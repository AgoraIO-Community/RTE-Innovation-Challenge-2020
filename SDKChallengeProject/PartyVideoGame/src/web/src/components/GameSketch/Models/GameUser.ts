import { GameUserState } from "./GameUserState";
import { GameUserRole } from "./GameUserRole";

export class GameUser {
    name: string;
    uid: string;
    userState: GameUserState;
    role: GameUserRole;
    score: number;
    wordChosen: string;
    constructor(uid: string, name: string, userState: GameUserState, role: GameUserRole) {
        this.uid = uid;
        this.name = name;
        this.userState = userState;
        this.role = role;
        this.score = 0;
        this.wordChosen = ''
    }
}