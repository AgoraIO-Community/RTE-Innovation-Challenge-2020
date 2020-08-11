import * as React from 'react';

export interface ICanvasWaitingProps {
    gameId: string;
    uid: string;
}

export interface ICanvasWaitingState {
}

export default class CanvasWaiting extends React.Component<ICanvasWaitingProps, ICanvasWaitingState> {
  constructor(props: ICanvasWaitingProps) {
    super(props);

    this.state = {
    }
  }

  public render() {
    return (
      <div>
        please watch the big screen, and wait until your turn.
      </div>
    );
  }
}
