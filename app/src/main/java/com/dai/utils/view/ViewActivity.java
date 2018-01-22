package com.dai.utils.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dai.utils.R;
import com.dai.utils.util.BaseRecyclerAdapter;
import com.dai.utils.util.MainAdapter;
import com.dai.utils.view.fallingview.FallingActivity;
import com.dai.utils.view.musicbutton.MusicActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dai on 2018/1/8.
 */

public class ViewActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

//    @BindView(R.id.wave_progress)
//    WaveProgressView waveProgressView;
//    @BindView(R.id.text_progress)
//    TextView textProgress;
    //    @BindView(R.id.circle_view)
//    CircleBarView circleBarView;
//    @BindView(R.id.progressView)
//    ProgressView progressView;
    //    @BindView(R.id.text_progress)
//    TextView textProgress;
//    private LinearGradientUtil linearGradientUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        ButterKnife.bind(this);


        ArrayList<String> data = new ArrayList<>();
        data.add("雪花飘落");
        data.add("音乐旋转");
//        data.add("file I/O 流");
//        data.add("rollView");
//        data.add("service");
//        data.add("aidl");
//        data.add("broadcast");
//        data.add("view");
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
                        Intent intent = new Intent(getApplicationContext(), FallingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getApplicationContext(), MusicActivity.class);
                        startActivity(intent1);
                        break;
//                    case 2:
//                        Intent intent2 = new Intent(getApplicationContext(), FileActivity.class);
//                        startActivity(intent2);
//                        break;
//                    case 3:
//                        Intent intent3 = new Intent(getApplicationContext(), RollActivity.class);
//                        startActivity(intent3);
//                        break;
//                    case 4:
//                        Intent intent4 = new Intent(getApplicationContext(), ServiceActivity.class);
//                        startActivity(intent4);
//                        break;
//                    case 5:
//                        Intent intent5 = new Intent(getApplicationContext(), AidlActivity.class);
//                        startActivity(intent5);
//                        break;
//                    case 6:
//                        Intent intent6 = new Intent(getApplicationContext(), BroadCastActivity.class);
//                        startActivity(intent6);
//                        break;
//                    case 7:
//                        Intent intent7 = new Intent(getApplicationContext(), ViewActivity.class);
//                        startActivity(intent7);
//                        break;
                }
            }
        });
//        waveProgressView.setTextView(textProgress);
//        waveProgressView.setOnAnimationListener(new WaveProgressView.OnAnimationListener() {
//            @Override
//            public String howToChangeText(float interpolatedTime, float updateNum, float maxNum) {
//                DecimalFormat decimalFormat = new DecimalFormat("0.00");
//                String s = decimalFormat.format(interpolatedTime * updateNum / maxNum * 100) + "%";
//                return s;
//            }
//
//            @Override
//            public float howToChangeWaveHeight(float percent, float waveHeight) {
//                return (1 - percent) * waveHeight;
//            }
//        });
//        waveProgressView.setProgressNum(95, 3000);
//        waveProgressView.setDrawSecondWave(true);

//        linearGradientUtil = new LinearGradientUtil(Color.WHITE, Color.GRAY);
//        System.out.println("ViewActivity.onCreate");
//        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
//        float num = 0.0f;
//        for (int i = 0; i < 12; i++) {
//            if (i == 11)
//                System.out.println(" linearGradientUtil.getColor(0.0f) " + i + "***   " + linearGradientUtil.getColor(1.0f));
//            else
//                System.out.println(" linearGradientUtil.getColor(0.0f) " + i + "***   " + linearGradientUtil.getColor(num));
//
//            num = num + 0.0833f;
//
//        }

//        circleBarView.setTextView(textProgress);
//        circleBarView.setOnAnimationListener(new CircleBarView.OnAnimationListener() {
//            @Override
//            public String howToChangeText(float interpolatedTime, float progressNum, float maxNum) {
//                DecimalFormat decimalFormat = new DecimalFormat("0.00");
//                String s = decimalFormat.format(interpolatedTime * progressNum / maxNum * 100) + "%";
//                return s;
//            }
//
//            @Override
//            public void howTiChangeProgressColor(Paint paint, float interpolatedTime, float progressNum, float maxNum) {
//
////                System.out.println("interpolatedTime = " + interpolatedTime +" -------- "+ linearGradientUtil.getColor(interpolatedTime));
////                paint.setColor(linearGradientUtil.getColor(interpolatedTime));
//            }
//        });
//        circleBarView.setProgressNum(100, 100);
//        circleBarView.postInvalidate();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },0,1000).create();

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                circleBarView.postInvalidate();
//            }
//        },1,500);
    }
}
