import { StatesAction } from "common/Redux/Actions/StatesAction";
import { SampleActionType } from "./SampleActionType";
import { User } from "common/Models/User";

export class SystemActions {


    static SaveUserSuccess(currentUser: User): StatesAction<SampleActionType> {
        return {
            type: SampleActionType.SaveUserSuccess,
            payload: currentUser
        }
    }

    static GoLogin(): StatesAction<SampleActionType> {
        return {
            type: SampleActionType.RemoveCurrentUserSuccess,
            payload: null
        }
    }

  

}