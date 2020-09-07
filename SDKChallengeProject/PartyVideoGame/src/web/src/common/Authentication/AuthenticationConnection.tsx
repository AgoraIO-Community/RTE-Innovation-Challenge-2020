import React from "react";
import { User } from "common/Models/User";
import { RouteComponentProps } from "react-router-dom";
import FirebaseHelper from "utils/FirebaseHelper";
import { IAuthProps } from "./IAuthProps";
import Loading from "components/Loading/Loading";
interface IAuthStates {
    currentUser?: User;
}
interface AuthenticationConnectionConfig {
    redirectPath?: string;
}
export function AuthenticationConnection<TRouterParas>(ChildComponent: React.ComponentType<IAuthProps<TRouterParas>>, config: AuthenticationConnectionConfig = {}) {

    interface IWithAuthProps extends RouteComponentProps<TRouterParas> { }

    class WithAuthentication extends React.Component<IWithAuthProps, IAuthStates> {
        firebaseHelper:FirebaseHelper;
        constructor(props: Readonly<IWithAuthProps>) {
            super(props);
            this.firebaseHelper=new FirebaseHelper();
            this.state = {
                currentUser: undefined
            };
        }
        componentDidMount() {
            this.firebaseHelper.onAuthStateChanged(user => {
                this.setState({ currentUser: user });
                if (!user) {
                    this.redirectLogin();
                }
            })
        }

        componentWillUnmount() {
            this.firebaseHelper.dispose();
        }

        componentDidUpdate() {
            if (!this.state.currentUser) {
                this.redirectLogin();
            }
        }

        logout = () => {
            this.firebaseHelper.signOut();
        }

        private redirectLogin = () => {
            var redirectPath = '/login';
            if (config.redirectPath) {
                redirectPath = `/login/${config.redirectPath.split('/').join('_')}`;
            } else if (this.props.match.url) {
                redirectPath = `/login/${this.props.match.url.split('/').join('_')}`;
            }
            this.props.history.push(redirectPath);
        }

        public render() {
            const { currentUser } = this.state;
            if (!currentUser) {
                return <Loading />;
            }
            return (
                <ChildComponent
                    currentUser={currentUser}
                    logout={this.logout}
                    history={this.props.history}
                    location={this.props.location}
                    match={this.props.match}></ChildComponent>
            );
        }
    }
    return WithAuthentication;
}

