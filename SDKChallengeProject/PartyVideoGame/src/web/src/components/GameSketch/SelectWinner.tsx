import * as React from 'react';
import styles from './GameSketch.module.scss';
import { GameData } from './GameData';
import { GameUser } from './Models/GameUser';
import { GameUserState } from './Models/GameUserState';

interface ISelectWinnerProps {
    gameId: string;
    uid: string;
}

interface ISelectWinnerStates {
    gameUsers: Array<GameUser>;
    gameUsersForSelect: Array<GameUser>;

}

export default class SelectWinner extends React.Component<ISelectWinnerProps, ISelectWinnerStates> {


    gameData: GameData;
    constructor(props: Readonly<ISelectWinnerProps>) {
        super(props);
        this.state = {
            gameUsers: [],
            gameUsersForSelect: []
        }
        this.gameData = new GameData(this.props.gameId)
        this.gameData.onGameRoomUsersChanged(gameUsers => {
            this.setState({
                gameUsers,
                gameUsersForSelect: gameUsers.filter(p => p.uid !== this.props.uid)
            });
        })
    }

    componentDidMount() {
        this.gameData.initial();
    }

    componentWillUnmount() {
        this.gameData.dispose();
    }

    chooseWinner = (winnerGamerUser: GameUser) => {

        winnerGamerUser.score++;
        this.gameData.updateGameUser(winnerGamerUser);

        var currentGameUser = this.state.gameUsers.find(p => p.uid === this.props.uid);
        if (currentGameUser) {
            currentGameUser.userState = GameUserState.choosing;
            currentGameUser.score++;
            this.gameData.updateGameUser(currentGameUser);
        }
    }


    public render() {

        return (<div className={styles.choose}>
            <ul>
                {this.state.gameUsersForSelect.map(item => {
                    return <li>{item.name} <br></br> <button onClick={() => this.chooseWinner(item)}>choose</button><hr/> </li>;
                })}
            </ul>
        </div>)
    }
}
