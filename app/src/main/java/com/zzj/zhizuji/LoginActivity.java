package com.zzj.zhizuji;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by shawn on 17/3/5.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.single,R.id.operator})
    public void single(View view){
        switch (view.getId()){
            case R.id. single:
                Toast.makeText(this, "signle", Toast.LENGTH_SHORT).show();
                break;
            case R.id.operator:
                Toast.makeText(this, "opera", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnClick(R.id.register)
    public void register(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
