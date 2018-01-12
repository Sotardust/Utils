package com.dai.recycler.sqlite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dai.recycler.R;
import com.dai.recycler.util.Observer;
import com.dai.recycler.util.OkHttpUtil;
import com.dai.recycler.util.OnSubscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.SingleEmitter;

/**
 * Created by dai on 2017/9/14.
 */

public class SQLiteActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.find_one)
    TextView findOne;
    @BindView(R.id.find_all)
    TextView findAll;
    @BindView(R.id.insert)
    TextView insert;
    @BindView(R.id.update)
    TextView update;
    @BindView(R.id.delete)
    TextView delete;

    SQLiteAdapter adapter;
    private DatabaseContext context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        ButterKnife.bind(this);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        adapter = new SQLiteAdapter();
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        adapter.setData(userInfos);
        context = new DatabaseContext(getApplicationContext());

        OkHttpUtil<ArrayList<UserInfo>> okHttpUtil = new OkHttpUtil<ArrayList<UserInfo>>();
        okHttpUtil.create(new OnSubscribe<ArrayList<UserInfo>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<UserInfo>> e) throws Exception {
                super.subscribe(e);
                SQLiteUtil sqLiteUtil = new SQLiteUtil(context);
                e.onSuccess(sqLiteUtil.getAllData());
            }
        }, new Observer<ArrayList<UserInfo>>() {
            @Override
            public void onSuccess(ArrayList<UserInfo> userInfos) {
                super.onSuccess(userInfos);
                adapter.setData(userInfos);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private int count = 0;
    private int total = 0;

    @OnClick({R.id.insert, R.id.delete, R.id.update, R.id.find_all, R.id.find_one})
    void submit(View view) {

        if (view == findAll) {
            OkHttpUtil<ArrayList<UserInfo>> okHttpUtil = new OkHttpUtil<ArrayList<UserInfo>>();
            okHttpUtil.create(new OnSubscribe<ArrayList<UserInfo>>() {
                @Override
                public void subscribe(SingleEmitter<ArrayList<UserInfo>> e) throws Exception {
                    super.subscribe(e);
                    SQLiteUtil sqLiteUtil = new SQLiteUtil(context);
                    e.onSuccess(sqLiteUtil.getAllData());
                }
            }, new Observer<ArrayList<UserInfo>>() {
                @Override
                public void onSuccess(ArrayList<UserInfo> userInfos) {
                    super.onSuccess(userInfos);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (UserInfo userInfo : userInfos) {
                        stringBuilder.append(userInfo.toString());

                    }
                    total = userInfos.get(userInfos.size() - 1).getId();
                    Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
        }
        if (view == insert) {
            OkHttpUtil<ArrayList<UserInfo>> okHttpUtil = new OkHttpUtil<ArrayList<UserInfo>>();
            okHttpUtil.create(new OnSubscribe<ArrayList<UserInfo>>() {
                @Override
                public void subscribe(SingleEmitter<ArrayList<UserInfo>> e) throws Exception {
                    super.subscribe(e);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(count++);
                    userInfo.setName("小张" + count);
                    userInfo.setAge(18);
                    userInfo.setSex("女" + count);
                    SQLiteUtil sqLiteUtil = new SQLiteUtil(context);
                    if (sqLiteUtil.insertData(userInfo)) {
                        e.onSuccess(sqLiteUtil.getAllData());
                    }
                }
            }, new Observer<ArrayList<UserInfo>>() {
                @Override
                public void onSuccess(ArrayList<UserInfo> userInfos) {
                    super.onSuccess(userInfos);
                    adapter.getData().clear();
                    adapter.setData(userInfos);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
        }
        if (view == update) {
            OkHttpUtil<UserInfo> okHttpUtil = new OkHttpUtil<UserInfo>();
            okHttpUtil.create(new OnSubscribe<UserInfo>() {
                @Override
                public void subscribe(SingleEmitter<UserInfo> e) throws Exception {
                    super.subscribe(e);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(count++);
                    userInfo.setName("小兰1");
                    userInfo.setAge(14);
                    userInfo.setSex("女12" + count);
                    SQLiteUtil sqLiteUtil = new SQLiteUtil(context);
                    if (sqLiteUtil.updateUserInfo(userInfo, "小兰")) {
                        e.onSuccess(userInfo);
                    }
                }
            }, new Observer<UserInfo>() {
                @Override
                public void onSuccess(UserInfo userInfo) {
                    super.onSuccess(userInfo);
                    ArrayList<UserInfo> userInfos = adapter.getData();
                    for (UserInfo userInfo1 : userInfos)
                        if (userInfo1.getName().equals("小兰")) {
                            int position = userInfos.indexOf(userInfo1);
                            System.out.println("position = " + position);
                            adapter.getData().remove(position);
                            System.out.println("adapter.size = " + adapter.getData().size());
                            adapter.getData().add(position, userInfo);
                            ArrayList<UserInfo> userInfos1 = adapter.getData();
                            adapter.setData(userInfos1);
                        }
//                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
        }
        if (view == delete) {
            OkHttpUtil<Integer> okHttpUtil = new OkHttpUtil<Integer>();
            okHttpUtil.create(new OnSubscribe<Integer>() {
                @Override
                public void subscribe(SingleEmitter<Integer> e) throws Exception {
                    super.subscribe(e);
                    SQLiteUtil sqLiteUtil = new SQLiteUtil(context);
                    System.out.println("total = " + total);
                    if (sqLiteUtil.deleteData(total)) {
                        e.onSuccess(total);
                    }
                }
            }, new Observer<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    super.onSuccess(integer);
                    adapter.getData().remove(adapter.getItemCount() - 1);
                    adapter.notifyItemRemoved(adapter.getItemCount());
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
        }
        if (view == findOne) {
            OkHttpUtil<Integer> okHttpUtil = new OkHttpUtil<Integer>();
            okHttpUtil.create(new OnSubscribe<Integer>() {
                @Override
                public void subscribe(SingleEmitter<Integer> e) throws Exception {
                    super.subscribe(e);
                    SQLiteUtil sqLiteUtil = new SQLiteUtil(context);
                    System.out.println("total = " + total);
                    int num = sqLiteUtil.getNameCount("小兰1");
                    e.onSuccess(num);
                }

            }, new Observer<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    super.onSuccess(integer);
                    Toast.makeText(getApplicationContext(), integer + "", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
        }
    }
}