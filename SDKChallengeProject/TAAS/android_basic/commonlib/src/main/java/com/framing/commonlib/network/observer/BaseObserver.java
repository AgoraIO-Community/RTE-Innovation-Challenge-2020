package com.framing.commonlib.network.observer;

import android.util.Log;

import com.framing.commonlib.network.error.ExceptionError;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 */
public abstract class BaseObserver<T> implements Observer<T> {
    public BaseObserver() {
    }
    @Override
    public void onError(Throwable e) {
        Log.e("lvr", e.getMessage());
        // todo error somthing

        if(e instanceof ExceptionError.ResponeThrowable){
            onError((ExceptionError.ResponeThrowable)e);
        } else {
            onError(new ExceptionError.ResponeThrowable(e, ExceptionError.ERROR.UNKNOWN));
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
    }


    @Override
    public void onComplete() {
    }


    public abstract void onError(ExceptionError.ResponeThrowable e);

}
