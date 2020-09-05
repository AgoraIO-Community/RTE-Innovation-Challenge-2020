import { GameUser } from "./Models/GameUser";
import ParseServerHelper from "utils/ParseServerHelper";
import { GameRoom } from "./Models/GameRoom";
import { SocketHelper } from "utils/SocketHelper";
import Consts from "./Consts";
import { GameRound } from "./Models/GameRound";
import { CanvasMessage } from "./Models/CanvasMessage";
import { Line } from "./Models/Line";

export class GameData {

    private socketHelper: SocketHelper;
    private serviceHelper: ParseServerHelper;
    public gameRoom: GameRoom;
    public gameRound: GameRound;
    public gameUsers: Array<GameUser>;
    constructor(gameId: string) {
        this.socketHelper = new SocketHelper(gameId);
        this.serviceHelper = new ParseServerHelper();
        this.gameRoom = new GameRoom(gameId);
        this.gameRound = new GameRound(gameId);
        this.gameUsers = [];
    }


    initial() {
        this.socketHelper.emit(Consts.initialGame, { gameRoom: this.gameRoom, gameRound: this.gameRound, gameUsers: this.gameUsers });
    }

    startGame() {
        this.socketHelper.emit(Consts.startGame, {});
    }

    closeGame(){
        this.socketHelper.emit(Consts.closeGame,{});
    }

    stopGame(){
        this.socketHelper.emit(Consts.stopGame,{});
    }


    onGameRoundChanged(onChange: (gameRound: GameRound) => void) {
        this.socketHelper.onEventChanged<GameRound>(Consts.gameRound, async data => {
            this.gameRound = data;
            onChange(data);
        });
    }

    onGameRoomChanged(onChange: (gameRoom: GameRoom) => void) {
        this.socketHelper.onEventChanged<GameRoom>(Consts.gameRoom, data => {
            this.gameRoom = data;
            onChange(data);
        });
    }

    onGameRoomUsersChanged(onChange: (gameUsers: Array<GameUser>) => void) {
        this.socketHelper.onEventChanged<Array<GameUser>>(Consts.gameUsers, data => {
            this.gameUsers = data;
            onChange(data);
        });
    }

    joinRoom(gameUser: GameUser) {
        this.socketHelper.emit<GameUser>(Consts.gameUserUpdate, gameUser);
    }

    updateRoomOwner(uid: string) {
        this.gameRoom.gameOwnerUid = uid;
        this.socketHelper.emit<GameRoom>(Consts.gameRoom, this.gameRoom);
    }


    updateRoomRound(round: number) {
        this.gameRoom.round = round;
        this.socketHelper.emit<GameRoom>(Consts.gameRoom, this.gameRoom);
    }

    updateRoomRoundTime(roundTime: number) {
        this.gameRoom.roundTime = roundTime;
        this.socketHelper.emit<GameRoom>(Consts.gameRoom, this.gameRoom);
    }

    updateGameUser(gameUser: GameUser) {
        this.socketHelper.emit<GameUser>(Consts.gameUserUpdate, gameUser);
    }

    updateCanvas(canvas: CanvasMessage) {
        this.socketHelper.emit("canvas", canvas);
    }

    onCanvasUpdate(onChange: (canvas: CanvasMessage) => void) {
        this.socketHelper.onEventChanged<CanvasMessage>('canvas', data => onChange(data));
    }

    cleanCanvas() {
        this.socketHelper.emit("canvasClean", {});
    }

    onCanvasClean(onChange: () => void) {
        this.socketHelper.onEventChanged('canvasClean', onChange);
    }

    drawLine(line: Line) {
        this.socketHelper.emit("line", line);
    }

    onLineDraw(onChange: (data: Line) => void) {
        this.socketHelper.onEventChanged<Line>('line', line => onChange(line));
    }

    onGameLines(onChange: (data: Array<Line>) => void) {
        this.socketHelper.onEventChanged<Array<Line>>('gameLines', lines => onChange(lines));
    }

    dispose() {
        this.serviceHelper.dispose();
        this.socketHelper.dispose();
    }


}