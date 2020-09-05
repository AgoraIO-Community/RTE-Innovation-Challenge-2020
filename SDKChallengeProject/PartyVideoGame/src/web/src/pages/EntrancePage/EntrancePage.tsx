/* eslint-disable jsx-a11y/anchor-is-valid */
import * as React from 'react';
import styles from './EntrancePage.module.scss';
import { IAuthProps } from "common/Authentication/IAuthProps";
import { withAuth } from 'common/Connect/Connections';
import { WordHelper } from 'utils/WordHelper';
import FirebaseHelper from 'utils/FirebaseHelper';
import { FaDribbbleSquare, FaGamepad, FaMitten } from 'react-icons/fa';
import { IoMdCloseCircle } from 'react-icons/io';

export interface IEntrancePageProps extends IAuthProps<{ path: string }> {
}

interface IEntrancePageStates {
    roomId: string;
}

class EntrancePage extends React.Component<IEntrancePageProps, IEntrancePageStates> {
    firebaseHelper: FirebaseHelper;
    constructor(props: Readonly<IEntrancePageProps>) {
        super(props);
        this.firebaseHelper = new FirebaseHelper();
        this.state = {
            roomId: WordHelper.newNoun(),
        }
    }
    roomChanged = async (room: string) => {
        this.setState({ roomId: room });
    }

    public render() {
        const { path } = this.props.match.params;
        return (
            <div className={styles.main}>
                {/* <h1>Hi,{name ? `${name},` : ''} Welcome! Please enter your room to start.</h1> */}
                <img src={""} alt="" />
                {path === 'create-party' &&
                    <div className={styles.card}>
                        <a className={styles.close} href="/welcome" ><IoMdCloseCircle></IoMdCloseCircle></a>
                        <FaDribbbleSquare></FaDribbbleSquare>
                        <div className={styles.roomName}>
                            <div>
                                <span>Room:</span>
                            </div>
                            <input type="text" value={this.state.roomId} onChange={e => { this.roomChanged(e.target.value) }} />
                        </div>
                        <a className={styles.button} href={`/room/${this.state.roomId}`}>Create Party</a>
                    </div>

                }
                {path === 'join-party' &&
                    <div className={styles.card}>
                        <a className={styles.close} href="/welcome" ><IoMdCloseCircle></IoMdCloseCircle></a>
                        <FaMitten></FaMitten>
                        <div className={styles.roomName}>
                            <div>
                                <span>Room:</span>
                            </div>
                            <input type="text" value={this.state.roomId} onChange={e => { this.roomChanged(e.target.value) }} />
                        </div>
                        <a className={styles.button} href={`/room/${this.state.roomId}`}>Join Party</a>
                    </div>
                }
                {path === 'create-game' &&
                    <div className={styles.card}>
                        <a className={styles.close} href="/welcome" ><IoMdCloseCircle></IoMdCloseCircle></a>
                        <FaGamepad></FaGamepad>
                        <div className={styles.roomName}>
                            <div>
                                <span>Room:</span>
                            </div>
                            <input type="text" value={this.state.roomId} onChange={e => { this.roomChanged(e.target.value) }} />
                        </div>
                        <a className={styles.button} href={`/game/${this.state.roomId}`}>Create Game</a>
                    </div>
                }
            </div>
        );
    }
}

export default withAuth(EntrancePage);