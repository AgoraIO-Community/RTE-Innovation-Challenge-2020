import React from 'react';
import PropTypes from 'prop-types';
import classnames from 'classnames';
import {
  withRouter,
  matchPath,
} from 'react-router';

import {
  Link,
} from 'react-router-dom';

import styles from './NavBar.cm.styl';

function NavBar(props) {
  const pathname = props.location.pathname;

  const matchRecv = matchPath(pathname, '/recv/:recvCode?');

  return (
    <div>

    </div>
  );
}

NavBar.defaultProps = {};

NavBar.propTypes = {
  location: PropTypes.object,
};

export default withRouter(NavBar);
