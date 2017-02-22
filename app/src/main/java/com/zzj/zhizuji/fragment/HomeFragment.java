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
    private View mContentView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = View.inflate(getActivity(), R.layout.fragment_home,null);
        ButterKnife.bind(this,mContentView);
        return mContentView;
    }
}
