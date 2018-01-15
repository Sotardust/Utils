package com.dai.recycler.view.musicbutton;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dai.recycler.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dai on 2018/1/15.
 */

public class MusicActivity extends AppCompatActivity {
    @BindView(R.id.music_btn)
    MusicButton musicBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        musicBtn.setOnClickListener(new View.OnClickListener() {//单击播放或暂停
            @Override
            public void onClick(View v) {
                musicBtn.playMusic();
//                try {
//                    if (mPlayer != null) {
//                        if (mPlayer.isPlaying()) {
//                            mPlayer.pause();
//                        } else {
//                            mPlayer.start();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        musicBtn.setOnLongClickListener(new View.OnLongClickListener() {//长按停止
            @Override
            public boolean onLongClick(View v) {
//                try {
//                    if (mPlayer != null) {
//                        mPlayer.stop();
//                        mPlayer.prepare();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                musicBtn.stopMusic();
                return true;//消费此长按事件，不再向下传递
            }
        });
    }


}
