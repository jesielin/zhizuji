package com.zzj.zhizuji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.base.BaseFragment;

/**
 * Created by shawn on 2017-02-22.
 */

public class HomeFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected String getTitle() {
        return "首页";
    }
}
