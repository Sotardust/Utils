package com.dai.utils.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.dai.utils.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class DialogActivity extends AppCompatActivity {

    @BindView(R.id.btn)
    Button btn;

    private MyDialogFragment myDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_dialog);
        ButterKnife.bind(this);
        myDialogFragment = new MyDialogFragment();
        myDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE,R.style.dialogFragment);

    }

    @OnClick(R.id.btn)
    public void onViewClicked() {

        myDialogFragment.show(getSupportFragmentManager(), "myDialog");

    }
}
