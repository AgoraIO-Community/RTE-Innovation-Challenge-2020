import { User } from "common/Models/User";
import { Reducer } from "redux";
import { StatesAction } from "common/Redux/Actions/StatesAction";
import { SampleStates } from "./SampleStates";
import { SampleActionType } from "./SampleActionType";

export const sampleReducer: Reducer<SampleStates, StatesAction<SampleActionType>> = (state = new SampleStates(), action) => {
    switch (action.type) {
        case SampleActionType.SaveUserSuccess:
            return { ...state, currentUser: action.payload as User }
        case SampleActionType.RemoveCurrentUserSuccess:
            return { ...state, currentUser: null }
    }
    return state;
};
