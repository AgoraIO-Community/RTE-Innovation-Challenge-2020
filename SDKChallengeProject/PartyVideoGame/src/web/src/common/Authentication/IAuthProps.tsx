import { User } from "common/Models/User";
import { RouteComponentProps } from "react-router-dom";
export interface IAuthProps<TRouterParas> extends RouteComponentProps<TRouterParas> {
    currentUser: User;
    logout: () => void;
}
