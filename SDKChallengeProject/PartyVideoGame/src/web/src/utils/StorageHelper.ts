export class StorageHelper {

    save<T>(key: string, data: T) {
        localStorage.setItem(key, JSON.stringify(data));
    }

    get<T>(key: string) {
        var result = localStorage.getItem(key);
        if (result) {
            return JSON.parse(result) as T;
        } else {
            return null;
        }
    }
}