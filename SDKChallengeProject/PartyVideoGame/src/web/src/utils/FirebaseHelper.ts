import * as firebase from 'firebase/app';
import 'firebase/auth';
import 'firebase/firestore';
import 'firebase/analytics';
import { User } from "common/Models/User";
import Log from "./Log";

export default class FirebaseHelper {

    database: firebase.firestore.Firestore;
    unregisterAuthObserver: firebase.Unsubscribe;
    onDbChangingObserver: () => void;

    constructor() {
        this.database = firebase.firestore();
        this.unregisterAuthObserver = () => { };
        this.onDbChangingObserver = () => { };
    }
    public static initial() {
        var firebaseConfig = {
            apiKey: process.env.REACT_APP_FIREBASE_KEY,
            authDomain: `${process.env.REACT_APP_FIREBASE_PROJECT_ID}.firebaseapp.com`,
            databaseURL: `https://${process.env.REACT_APP_FIREBASE_PROJECT_ID}.firebaseio.com`,
            projectId: `${process.env.REACT_APP_FIREBASE_PROJECT_ID}`,
            storageBucket: `${process.env.REACT_APP_FIREBASE_PROJECT_ID}.appspot.com`,
            messagingSenderId: `${process.env.REACT_APP_FIREBASE_SENDER_ID}`,
            appId: `${process.env.REACT_APP_FIREBASE_APP_ID}`,
            measurementId: `${process.env.REACT_APP_FIREBASE_MEASUREMENT_ID}`
        };
        firebase.initializeApp(firebaseConfig);
        firebase.analytics();
    }

    public signOut() {
        return firebase.auth().signOut();
    }

    public onAuthStateChanged(action: (userInput?: User) => void) {
        this.unregisterAuthObserver = firebase.auth().onAuthStateChanged(user => {
            if (user) {
                var userObj = new User();
                userObj.id = user.uid;
                userObj.name = user.displayName ?? '';
                userObj.provider = user.providerId;
                userObj.photoUrl = user.photoURL;
                action(userObj);
            } else {
                action(undefined);
            }

        });
    }

    public dispose() {
        this.unregisterAuthObserver();
        this.onDbChangingObserver();
    }

    public async dbAddOrUpdateAsync(collection: string, docId: string, value: object): Promise<void> {
        try {
            let doc = this.database.collection(collection).doc(docId);
            let valuePlain = JSON.parse(JSON.stringify(value));
            await doc.set(valuePlain);
        } catch (error) {
            debugger;
            Log.Error(error)
        }
    }

    public async dbGetByDocIdAsync<T>(collection: string, docId: string) {
        let doc = await this.database.collection(collection).doc(docId).get();
        if (doc.exists) {
            return doc.data() as T;
        } else {
            return null;
        }

    }


    public dbChanging<T>(collection: string, docId: string, onChanged: (value: T) => void) {
        this.onDbChangingObserver= this.database.collection(collection).doc(docId).onSnapshot(observer => {
            var changes = observer.data() as T;
            onChanged(changes);
        })
    }

}
