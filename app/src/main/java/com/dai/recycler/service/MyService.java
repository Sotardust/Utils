package com.dai.recycler.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.dai.recycler.R;

/**
 * Created by dai on 2017/10/20.
 */

public class MyService extends Service {

//    private MyBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
//        Notification notification = new Notification(R.mipmap.ic_launcher,
//                "有通知到来", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, ServiceActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("有通知到来")
                .setContentTitle("有通知标题")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("MyService.onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("MyService.onDestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("MyService.onBind");
        return mBinder;
    }

//    class MyBinder extends Binder {
//
//        public void startDownload() {
//            System.out.println("MyBinder.startDownload");
//            // 执行具体的下载任务
//        }
//
//    }

    IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int plus(int a, int b) throws RemoteException {
            return 0;
        }

        @Override
        public String toUpperCase(String str) throws RemoteException {
            return null;
        }
    } ;
}
