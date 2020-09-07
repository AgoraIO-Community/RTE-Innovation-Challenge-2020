import * as React from 'react';
import { withAuth } from 'common/Connect/Connections';
import { IAuthProps } from 'common/Authentication/IAuthProps';
import GameEnter from 'components/GameSketch/GameEnter';
import styles from './GameSketchPage.module.scss';

interface IGameSketchPageProps extends IAuthProps<{ roomId: string }> {
}

export interface IGameSketchPageState {


}

class GameSketchPage extends React.Component<IGameSketchPageProps, IGameSketchPageState> {
    constructor(props: IGameSketchPageProps) {
        super(props);
        this.state = {

        }
    }

    leaveRoom = () => {
        this.props.history.push("/enter");
    }
    public render() {
        return (
            <div className={styles.main}>
                <GameEnter currentUser={this.props.currentUser} gameId={this.props.match.params.roomId} closeGame={() => { this.leaveRoom()}}></GameEnter>
            </div>
        );
    }
}

export default withAuth(GameSketchPage);
