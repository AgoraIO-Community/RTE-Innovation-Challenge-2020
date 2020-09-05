import * as React from 'react';
import * as firebase from 'firebase/app';
import { StyledFirebaseAuth } from 'react-firebaseui';
import * as firebaseui from 'firebaseui';
import styles from './LoginPage.module.scss';
import Loading from 'components/Loading/Loading';
import { RouteComponentProps, withRouter } from 'react-router-dom';

interface ILoginPageProps extends RouteComponentProps<{ redirect: string }> {
}

class LoginPage extends React.Component<ILoginPageProps, { isPending: boolean }> {

    uiconfig: firebaseui.auth.Config;
    constructor(props: Readonly<ILoginPageProps>) {
        super(props);
        this.uiconfig = {
            signInFlow: 'redirect',
            signInSuccessUrl: this.props.match.params.redirect.split('_').join('/') || '/enter',
            signInOptions: [
                firebase.auth.GoogleAuthProvider.PROVIDER_ID,
                {
                    provider:firebase.auth.EmailAuthProvider.PROVIDER_ID,
                    requireDisplayName:false
                },
                firebaseui.auth.AnonymousAuthProvider.PROVIDER_ID,
                {
                    provider: firebase.auth.PhoneAuthProvider.PROVIDER_ID,
                    defaultCountry: 'NL',
                }
            ],
        };

        this.state = { isPending: false }
    }

    uiCallback = (ui: firebaseui.auth.AuthUI) => {
        this.setState({ isPending: ui.isPendingRedirect() })
    }

    public render() {

        return (
            <div className={styles.main}>
                {this.state.isPending && <Loading></Loading>}
                <StyledFirebaseAuth uiCallback={this.uiCallback} uiConfig={this.uiconfig} firebaseAuth={firebase.auth()} />
            </div>
        );
    }
}

export default withRouter(LoginPage);