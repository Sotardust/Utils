package com.dai.recycler.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    class MyBinder extends BookAidlInterface.Stub {

        @Override
        public List<Book> getBooks() throws RemoteException {
            return null;
        }

        @Override
        public String getName() throws RemoteException {
            return "客户端测试";
        }

        @Override
        public void addBook(Book book) throws RemoteException {

        }
    }
}


