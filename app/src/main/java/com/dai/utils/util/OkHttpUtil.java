package com.dai.utils.util;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dai on 2017/9/14.
 */

public class OkHttpUtil<T> {
    public void create(OnSubscribe<T> onSubscribe, Observer<T> observer) {
        Single.create(onSubscribe).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
