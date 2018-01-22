package com.dai.utils.util;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by dai on 2017/9/14.
 */

public abstract class Observer<T> implements SingleObserver<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onSuccess(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }
}
