import * as React from 'react';
import styles from './LoginPage.module.scss';
import Loading from 'components/Loading/Loading';
import { RouteComponentProps, withRouter } from 'react-router-dom';
import ParseServerHelper from 'utils/ParseServerHelper';

interface ILoginPageProps extends RouteComponentProps<{ redirect: string }> {
}

class LoginPage extends React.Component<ILoginPageProps, { isPending: boolean }> {

    private serviceHelper: ParseServerHelper;

    constructor(props: Readonly<ILoginPageProps>) {
        super(props);
        this.serviceHelper = new ParseServerHelper();
        // this.uiconfig = {
        //     signInFlow: 'redirect',
        //     signInSuccessUrl: this.props.match.params.redirect.split('_').join('/') || '/enter',
        //     signInOptions: [
        //         firebase.auth.GoogleAuthProvider.PROVIDER_ID,
        //         {
        //             provider:firebase.auth.EmailAuthProvider.PROVIDER_ID,
        //             requireDisplayName:false
        //         },
        //         firebaseui.auth.AnonymousAuthProvider.PROVIDER_ID,
        //         {
        //             provider: firebase.auth.PhoneAuthProvider.PROVIDER_ID,
        //             defaultCountry: 'NL',
        //         }
        //     ],
        // };

        this.state = { isPending: false }
        
    }


    public render() {

        return (
            <div className={styles.main}>
                {this.state.isPending && <Loading></Loading>}
                {<div>Login page</div>}
            </div>
        );
    }
}

export default withRouter(LoginPage);