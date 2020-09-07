import * as React from 'react';
import GameJoin from 'components/GameSketch/GameJoin';
import { withAuth } from 'common/Connect/Connections';
import { IAuthProps } from 'common/Authentication/IAuthProps';

interface IGameSketchJoinPageProps extends IAuthProps<{ roomId: string }> {
}

export interface IGameSketchJoinPageState {
    

}

class GameSketchJoinPage extends React.Component<IGameSketchJoinPageProps, IGameSketchJoinPageState> {
    constructor(props: IGameSketchJoinPageProps) {
        super(props);
        this.state = {
            
        }
    }
    
    public render() {
        return (
            <GameJoin currentUser={this.props.currentUser}  roomId={this.props.match.params.roomId}></GameJoin>
        );
    }
}

export default withAuth(GameSketchJoinPage);
