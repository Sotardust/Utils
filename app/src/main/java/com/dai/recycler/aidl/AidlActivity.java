package com.dai.recycler.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.dai.recycler.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dai on 2017/10/23.
 */

public class AidlActivity extends AppCompatActivity {

    @BindView(R.id.aidl)
    Button aidl;
    private BookAidlInterface bookAidlInterface;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookAidlInterface = BookAidlInterface.Stub.asInterface(service);
            mBound = true;
            if (bookAidlInterface != null) {
                try {
                    mBooks = bookAidlInterface.getBooks();
                    Log.e(getLocalClassName(), mBooks.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;
    //包含Book对象的list
    private List<Book> mBooks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        ButterKnife.bind(this);
    }



    /**
     * 尝试与服务端建立连接
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.dai.aidltest.aidl");
        intent.setPackage("com.dai.aidltest");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.aidl)
    public void onViewClicked() {
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bookAidlInterface == null) return;

        Book book = new Book();
        book.setName("APP研发录In");
        book.setPrice(30);
        try {
            bookAidlInterface.addBook(book);
            Log.e(getLocalClassName(), book.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            Toast.makeText(AidlActivity.this, bookAidlInterface.getName(), Toast.LENGTH_SHORT).show();
            System.out.println("客户端发起测试");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(serviceConnection);
            mBound = false;
        }
    }
}
