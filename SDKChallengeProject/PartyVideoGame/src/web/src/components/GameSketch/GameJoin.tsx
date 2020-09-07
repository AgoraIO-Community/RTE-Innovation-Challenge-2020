import * as React from 'react';
import { GameData } from './GameData';
import { WordHelper } from 'utils/WordHelper';
import { GameUser } from './Models/GameUser';
import { GameUserRole } from './Models/GameUserRole';
import { GameUserState } from './Models/GameUserState';
import styles from './GameSketch.module.scss';
import { User } from 'common/Models/User';

export interface IGameJoinProps {
    roomId: string;
    currentUser: User;

}

export interface IGameJoinState {
    gameUserName: string;

}

export default class GameJoin extends React.Component<IGameJoinProps, IGameJoinState> {
    gameData: GameData;
    constructor(props: IGameJoinProps) {
        super(props);
        this.gameData = new GameData(this.props.roomId);
        this.state = {
            gameUserName: ''
        }
    }


    componentDidMount() {
        this.gameData.initial();
        this.setState({
            gameUserName: 'user-'+WordHelper.newNoun()
        })

    }

    onNameChanged = (name: string) => {
        this.setState({
            gameUserName: name
        })
    }

    joinGame = () => {
        var _gameUser = new GameUser(this.props.currentUser.id, this.state.gameUserName, GameUserState.waiting, GameUserRole.player);
        this.gameData.joinRoom(_gameUser);

        window.location.pathname = window.location.pathname.replace('join', '');//TODO: switch to a callback
    }

    public render() {
        return (
            <div className={styles.join}>
                <div>Please input your name:</div>
                <input type="text" value={this.state.gameUserName} onChange={e => this.onNameChanged(e.target.value)} />
                <button onClick={this.joinGame}>Join</button>
            </div>
        );
    }
}
