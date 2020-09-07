import * as React from 'react';
import styles from './GameSketch.module.scss';
import { GameData } from './GameData';
import { WordHelper } from 'utils/WordHelper';
import { GameUser } from './Models/GameUser';
import { GameUserState } from './Models/GameUserState';

interface IChoosingWordProps {
    gameId: string;
    uid: string;
}

interface IChoosingWordStates {
    wordsForChoosing: Array<string>;
    gameUsers: Array<GameUser>;

}

export default class ChoosingWord extends React.Component<IChoosingWordProps, IChoosingWordStates> {


    gameData: GameData;
    constructor(props: Readonly<IChoosingWordProps>) {
        super(props);
        this.state = {
            wordsForChoosing: WordHelper.newNounArray(5),
            gameUsers: []
        }
        this.gameData = new GameData(this.props.gameId)
        this.gameData.onGameRoomUsersChanged(gameUsers => this.setState({ gameUsers }))
    }

    componentDidMount() {
        this.gameData.initial();
    }



    componentWillUnmount() {
        this.gameData.dispose();
    }

    chooseWord = (word: string) => {
        var currentGameUser = this.state.gameUsers.find(p => p.uid === this.props.uid);
        if (currentGameUser) {
            currentGameUser.wordChosen = word;
            currentGameUser.userState = GameUserState.playing;
            this.gameData.updateGameUser(currentGameUser);
        }
    }


    public render() {

        return (<div className={styles.choose}>
            <ul>
                {this.state.wordsForChoosing.map(item => {
                    return <li>{item} <br></br> <button onClick={() => this.chooseWord(item)}>choose</button><hr/> </li>;
                })}
            </ul>
        </div>)
    }
}
