import React from 'react';
import ReactDOM from 'react-dom';
import './index.scss';
import * as serviceWorker from './serviceWorker';
import store from './common/Redux/Store';
import { Provider } from 'react-redux';
import 'bootstrap/js/dist/dropdown';
import 'bootstrap/js/dist/collapse';
import 'bootstrap/js/dist/modal';
import EntrancePage from 'pages/EntrancePage/EntrancePage';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import RoomPage from 'pages/RoomPage/RoomPage';
import LoginPage from 'pages/LoginPage/LoginPage';
import ParseServerHelper from 'utils/ParseServerHelper';
import WelcomePage from 'pages/Welcome/WelcomePage';
import GameSketchPage from 'pages/GameSketchPage/GameSketchPage';
import GameSketchJoinPage from 'pages/GameSketchJoinPage/GameSketchJoinPage';

ParseServerHelper.initial();

ReactDOM.render(
  <Provider store={store()}>
    {/* <React.StrictMode> */}
    <Router>
      <Switch>
        <Route path="/login/:redirect">
          <LoginPage />
        </Route>
        <Route path="/login">
          <LoginPage />
        </Route>
        <Route path="/room/:id">
          <RoomPage />
        </Route>
        <Route path="/enter/:path">
          <EntrancePage />
        </Route>
        <Route path="/game/:roomId/join">
          <GameSketchJoinPage />
        </Route>
        <Route path="/game/:roomId">
          <GameSketchPage />
        </Route>
        <Route path="/">
          <WelcomePage />
        </Route>
      </Switch>
    </Router>
    {/* </React.StrictMode> */}
  </Provider>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
