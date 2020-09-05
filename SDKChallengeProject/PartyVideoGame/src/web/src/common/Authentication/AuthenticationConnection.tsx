import { User } from "common/Models/User";
import Loading from "components/Loading/Loading";
import React from "react";
import { RouteComponentProps } from "react-router-dom";
import ParseServerHelper from "utils/ParseServerHelper";
import { IAuthProps } from "./IAuthProps";
interface IAuthStates {
    currentUser?: User;
}
interface AuthenticationConnectionConfig {
    redirectPath?: string;
}
export function AuthenticationConnection<TRouterParas>(ChildComponent: React.ComponentType<IAuthProps<TRouterParas>>, config: AuthenticationConnectionConfig = {}) {

    interface IWithAuthProps extends RouteComponentProps<TRouterParas> { }

    class WithAuthentication extends React.Component<IWithAuthProps, IAuthStates> {
        serviceHelper: ParseServerHelper;
        constructor(props: Readonly<IWithAuthProps>) {
            super(props);
            this.serviceHelper = new ParseServerHelper();
            this.state = {
                currentUser: undefined
            };
        }
        componentDidMount() {
            this.serviceHelper.onAuthStateChanged = (user) => {
                this.setState({ currentUser: user });
                if (!user) {
                    this.redirectLogin();
                }
            }
        }

        componentWillUnmount() {
            this.serviceHelper.dispose();
        }

        componentDidUpdate() {
            if (!this.state.currentUser) {
                this.redirectLogin();
            }
        }

        logout = () => {
            this.serviceHelper.signOut();
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

