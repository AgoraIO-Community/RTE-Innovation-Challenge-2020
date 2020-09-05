import { User } from "common/Models/User";
import Log from "./Log";
import Parse from "parse";
export default class ParseServerHelper {

    subscription: Parse.LiveQuerySubscription | null;
    unregisterAuthObserver: () => void;
    onDbChangingObserver: () => void;
    onAuthStateChanged: (userInput?: User) => void;
    constructor() {
        this.unregisterAuthObserver = () => { };
        this.onDbChangingObserver = () => { };
        this.onAuthStateChanged = user => { };
        this.subscription = null;
    }
    public static initial() {
        Parse.initialize(process.env.REACT_APP_PARSE_SERVER_APPLICATION_ID ?? '', process.env.REACT_APP_PARSE_SERVER_MASTER_KEY);
        Parse.serverURL = process.env.REACT_APP_PARSE_SERVER_URL ?? '';
    }

    public async subscribe(collection: string) {
        let query = new Parse.Query(collection);
        this.subscription = await query.subscribe();
    }

    public signOut() {
        Parse.User.logOut().then(() => {
            var user = Parse.User.current();  // this will now be null
            if (user) {
                var userObj = new User();
                userObj.id = user.id;
                userObj.name = user.getUsername() ?? '';
                this.onAuthStateChanged(userObj);
            } else {
                this.onAuthStateChanged(undefined);
            }
        });
    }

    public async signIn(userName: string, passWord: string): Promise<boolean> {
        const user = await Parse.User.logIn(userName, passWord);
        const userExist = user.existed();
        if (userExist) {
            var userObj = new User();
            userObj.id = user.id;
            userObj.name = user.getUsername() ?? '';
            this.onAuthStateChanged(userObj);
        } else {
            this.onAuthStateChanged(undefined);
        }
        return userExist;
    }

    public async signUp(userName: string, passWord: string, email: string) {
        var user = new Parse.User();
        user.set("username", userName);
        user.set("password", passWord);
        user.set("email", email);
        try {
            await user.signUp();
        } catch (error) {
            Log.Error("Error: " + error.code + " " + error.message)
        }
    }

    public dispose() {
        this.unregisterAuthObserver();
        this.onDbChangingObserver();
        this.subscription?.unsubscribe();
    }

    public async dbAddOrUpdateAsync(collection: string, docId: string, value: object): Promise<string> {
        try {
            var document = Parse.Object.extend(collection);
            var doc = await document.save(value);
            return doc.id;
        } catch (error) {
            Log.Error(error)
        }
        return '';
    }

    public async dbGetByDocIdAsync<T>(collection: string, docId: string) {
        try {
            var document = Parse.Object.extend(collection);
            var doc = await document.get(docId);
            return doc as T;
        } catch (error) {
            Log.Error(error);
        }
        return null;
    }


    public dbChanging<T>(collection: string, docId: string, onChanged: (value: T) => void) {
        this.subscription?.on("update", (data) => {
            var doc = data.get(docId);
            onChanged(doc as T);
        });
    }

}
