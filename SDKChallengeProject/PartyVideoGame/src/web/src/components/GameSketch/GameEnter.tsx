import * as React from 'react';
import { GameUser } from './Models/GameUser';
import { GameData } from './GameData';
import { GameRoom } from './Models/GameRoom';
import { RoomState } from './Models/RoomState';
import { User } from 'common/Models/User';
import Loading from 'components/Loading/Loading';
import GamePlaying from './GamePlaying';
import CanvasWatcher from './CanvasWatcher';
import styles from './GameSketch.module.scss';
import { IoMdCloseCircle } from 'react-icons/io';
import { FaGamepad } from 'react-icons/fa';

interface IGameEnterProps {
  currentUser: User;
  gameId: string;
  closeGame: () => void;
}

interface IGameEnterStates {
  gameRoom: GameRoom;
  gameUsers: Array<GameUser>;
}

export default class GameEnter extends React.Component<IGameEnterProps, IGameEnterStates> {
  gameData: GameData;
  constructor(props: Readonly<IGameEnterProps>) {
    super(props);
    this.gameData = new GameData(this.props.gameId);
    this.gameData.onGameRoomChanged(this.onGameRoomChanged);
    this.gameData.onGameRoomUsersChanged(this.onGameRoomUsersChanged);
    this.state = {
      gameUsers: [],
      gameRoom: this.gameData.gameRoom
    }
  }

  componentDidMount() {
    this.gameData.initial();

  }
  componentWillUnmount() {
    this.gameData.dispose();
  }

  onGameRoomChanged = (gameRoom: GameRoom) => {
    this.setState({ gameRoom });
    if (!gameRoom.gameOwnerUid) {
      this.gameData.updateRoomOwner(this.props.currentUser.id);
    }
  }
  onGameRoomUsersChanged = (gameUsers: Array<GameUser>) => {
    this.setState({ gameUsers });
  }

  startGame = () => {
    this.gameData.startGame();
  }

  closeGame = () => {
    this.gameData.closeGame();
    this.props.closeGame();
  }

  isGameOwner = () => {
    return this.state.gameRoom.gameOwnerUid === this.props.currentUser.id;
  }

  isWatcher = () => {
    return !this.state.gameUsers.find(p => p.uid === this.props.currentUser.id);
  }

  isShowStart = () => {
    return this.isGameOwner() && this.state.gameUsers.length > 1;
  }


  public render() {

    switch (this.state.gameRoom.roomState) {
      case RoomState.playing:
        return this.isWatcher() ? <CanvasWatcher gameId={this.props.gameId} uid={this.props.currentUser.id} /> : <GamePlaying gameId={this.props.gameId} uid={this.props.currentUser.id}></GamePlaying>
      case RoomState.waiting:
        return this.waringForJoin();
      default:
        return <Loading></Loading>
    }
  }

  waringForJoin = () => {

    return (
      <div className={styles.enter}>
        <h1>Game Sketch</h1>
        <div className={styles.content}>
          <div className={styles.left}>

            <div className={styles.card}>
              <button className={styles.close} onClick={() => this.closeGame()} ><IoMdCloseCircle></IoMdCloseCircle></button>
              <FaGamepad></FaGamepad>

              <input type="number" value={this.state.gameRoom.round} onChange={e => { this.gameData.updateRoomRound(Number.parseInt(e.target.value)) }} />
              <p>rounds</p>
              <input type="number" value={this.state.gameRoom.roundTime} onChange={e => { this.gameData.updateRoomRoundTime(Number.parseInt(e.target.value)) }} />
              <p>seconds per round</p>
              <ul>
                {this.state.gameUsers.map(user =>
                  <li key={user.uid}>{user.name} is ready</li>)
                }
              </ul>
              {this.isShowStart() && <button className={styles.control} onClick={() => this.startGame()}>Start</button>}
            </div>

          </div>
          <div className={styles.right}>
            <div>Please join the game through the link using your phone:</div>
            <div>https://localhost/game/{this.state.gameRoom.gameId}/join</div>
            <div>
              <a href="https://web.whatsapp.com/" target={"blank"} >Share to WhatsApp</a>
            </div>
          </div>
        </div>
      </div>
    );

  };
}
