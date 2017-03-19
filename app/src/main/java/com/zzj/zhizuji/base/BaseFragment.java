package com.zzj.zhizuji.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzj.zhizuji.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shawn on 2017-02-22.
 */

public abstract class BaseFragment extends Fragment {


    protected View mContentView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView tvTitle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContentView = View.inflate(getActivity(),getLayoutId(),null);
        ButterKnife.bind(this,mContentView);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        tvTitle.setText(getTitle());
        return mContentView;
    }

    protected abstract int getLayoutId();
    protected abstract String getTitle();
}
