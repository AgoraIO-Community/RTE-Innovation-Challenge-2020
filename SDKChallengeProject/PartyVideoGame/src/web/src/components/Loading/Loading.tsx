import * as React from 'react';
import {ReactComponent as Waiting} from 'assets/loading.svg';
import styles from './Loading.module.scss';
export default class Loading extends React.Component {
    public render() {
        return (
            <div className={styles.main}>
                <Waiting/>
            </div>
        );
    }
}
