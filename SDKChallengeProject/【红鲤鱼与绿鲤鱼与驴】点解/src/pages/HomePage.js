import React from 'react';
import {
  Switch,
  Redirect,
  Route,
} from 'react-router-dom';
import ws from '../ws';
import NavBar from '../components/NavBar';
import SendFilePanel from '../components/SendFilePanel';
import RecvFilePanel from '../components/RecvFilePanel';
import Icon from '../components/common/Icon';
import SloganCard from '../components/SloganCard';

import styles from './HomePage.cm.styl';

class HomePage extends React.Component {
  constructor() {
    super();
    this.state = {
      send: {
        curStep: 1, // 当前在第几步
        files: [], // 已选择的文件
        peerState: '', // WebRTC连接状态
        waitingPrepareSend: false, // 是否正在等待选择完成的消息
      },
      recv: {
        recvCode: '', // 用户输入的收件码
        peerState: '', // WebRTC连接状态
        started: false, // 是否点击了开始下载
        files: [], // 接收到的文件
        targetId: '', // 对方的peerId
      },
    };

    this.setSendState = this.setSendState.bind(this);
    this.setRecvState = this.setRecvState.bind(this);
    this.onS2cPrepareSend = this.onS2cPrepareSend.bind(this);
    this.onS2cPrepareRecv = this.onS2cPrepareRecv.bind(this);
  }

  onS2cPrepareSend(payload) {
    this.setSendState({
      recvCode: payload.recvCode,
    });
    if (this.state.send.curStep === 1) {
      this.setSendState({
        curStep: 2,
        waitingPrepareSend: false,
      });
    }
  }

  onS2cPrepareRecv(payload) {
    this.setRecvState({
      targetId: payload.clientId,
      files: payload.files,
    });
  }

  componentDidMount() {
    ws.registerMessageHandler('s2c_prepare_send', this.onS2cPrepareSend);
    ws.registerMessageHandler('s2c_prepare_recv', this.onS2cPrepareRecv);
  }

  setSendState(newState) {
    this.setState(prevState => {
      const nextState = {
        ...prevState,
        send: {
          ...prevState.send,
          ...newState,
        },
      };
      return nextState;
    });
  }

  setRecvState(newState) {
    this.setState(prevState => {
      const nextState = {
        ...prevState,
        recv: {
          ...prevState.recv,
          ...newState,
        },
      };
      return nextState;
    });
  }

  render() {
    return (
      <div className={styles.container}>
        <NavBar />
        <Route exact path="/">
          <Redirect to="/send" />
        </Route>
        <div className={styles.content}>
          <Switch>
            <Route path="/send">
              <SendFilePanel {...this.state.send} setState={this.setSendState} />
            </Route>
            <Route path="/recv/:recvCode?">
              <RecvFilePanel {...this.state.recv} setState={this.setRecvState} />
            </Route>
          </Switch>
        </div>
      </div>
    );
  }
}

HomePage.defaultProps = {};

HomePage.propTypes = {
};

export default HomePage;
