package com.dai.recycler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.WindowManager;

import com.dai.recycler.aidl.AidlActivity;
import com.dai.recycler.broadcast.BroadCastActivity;
import com.dai.recycler.file.FileActivity;
import com.dai.recycler.rollview.RollActivity;
import com.dai.recycler.service.ServiceActivity;
import com.dai.recycler.sqlite.SQLiteActivity;
import com.dai.recycler.util.BaseRecyclerAdapter;
import com.dai.recycler.util.Constant;
import com.dai.recycler.util.MainAdapter;
import com.dai.recycler.view.ViewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dai on 2017/9/15.
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private static final int REQUEST_CODE = 1;
    private final static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE);
        }
        ArrayList<String> data = new ArrayList<>();
        data.add("RecyclerView");
        data.add("sqlite");
        data.add("file I/O 流");
        data.add("rollView");
        data.add("service");
        data.add("aidl");
        data.add("broadcast");
        data.add("view");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        MainAdapter adapter = new MainAdapter();
        adapter.setData(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLister(new BaseRecyclerAdapter.OnItemClickLister<String>() {
            @Override
            public void onItemClick(int position, String data) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), RecyclerActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getApplicationContext(), SQLiteActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), FileActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getApplicationContext(), RollActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getApplicationContext(), ServiceActivity.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(getApplicationContext(), AidlActivity.class);
                        startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6 = new Intent(getApplicationContext(), BroadCastActivity.class);
                        startActivity(intent6);
                        break;
                    case 7:
                        Intent intent7 = new Intent(getApplicationContext(), ViewActivity.class);
                        startActivity(intent7);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCode = " + requestCode);
        System.out.println("resultCode = " + resultCode);
    }

    private void init() {
        WindowManager windowManager = this.getWindowManager();
        Point point = new Point();
        Display display = windowManager.getDefaultDisplay();
        display.getSize(point);
        Constant.WIDTH = point.x;
        Constant.HEIGHT = point.y;
    }
}
