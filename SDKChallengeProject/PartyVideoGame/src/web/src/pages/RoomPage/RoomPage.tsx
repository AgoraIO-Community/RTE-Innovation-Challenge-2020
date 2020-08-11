import * as React from 'react';
import { withAuth } from 'common/Connect/Connections';
import { IAuthProps } from 'common/Authentication/IAuthProps';
import Room from 'components/Room/Room';

interface RouteInfo {
    id: string;
}

class RoomPage extends React.Component<IAuthProps<RouteInfo>> {
    leaveRoom = () => {
        this.props.history.push("/enter");
    }

    public render() {
        return (
            <Room roomId={this.props.match.params.id}
                roomPassword={null}
                currentUser={this.props.currentUser}
                leaveRoom={this.leaveRoom} />
        );
    }
}

export default withAuth(RoomPage);

