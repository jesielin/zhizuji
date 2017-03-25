package com.zzj.zhizuji;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zzj.zhizuji.util.SharedPreferenceUtils;
import com.zzj.zhizuji.util.UIHelper;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by shawn on 17/3/19.
 */

public class SettingActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        UIHelper.setTitle("设置",this);
    }

    @OnClick(R.id.quit)
    public void quit(View view){
        SharedPreferenceUtils.quitLogin();
        finish();
    }
}
