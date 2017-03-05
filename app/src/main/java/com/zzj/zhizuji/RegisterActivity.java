package com.zzj.zhizuji;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.zzj.zhizuji.R;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.fragment.RegisterFirstFragment;
import com.zzj.zhizuji.util.UIHelper;
import com.zzj.zhizuji.util.ViewUtils;

/**
 * Created by shawn on 17/3/5.
 */

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        BaseFragment fragment = ViewUtils.createFragment(RegisterFirstFragment.class);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
}
