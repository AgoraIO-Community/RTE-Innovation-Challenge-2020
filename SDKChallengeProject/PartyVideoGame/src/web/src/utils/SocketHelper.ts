import io from "socket.io-client";

export class Message {
    type: string;
    data: any;
    constructor(type: string, data: any) {
        this.type = type;
        this.data = data;
    }
}
export class SocketHelper {
    socketClient: SocketIOClient.Socket;
    constructor(roomId: string) {
        this.socketClient = io({ path: '/api' });
        this.socketClient.emit('join', { 'id': roomId });
    }

    emit<T>(event: string, data: T) {
        this.socketClient.emit(event, data);
    }

    onEventChanged<T>(event: string, onChange: (data: T) => void) {
        this.socketClient.on(event, onChange);
    }

    dispose() {
        if (this.socketClient) {
            this.socketClient.close();
        }
    }
}