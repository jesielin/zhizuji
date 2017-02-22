package com.zzj.zhizuji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.base.BaseFragment;

/**
 * Created by shawn on 2017-02-22.
 */

public class MessageFragment extends BaseFragment {

    private View mContentView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = View.inflate(getActivity(), R.layout.fragment_message,null);
        ButterKnife.bind(this,mContentView);
        return mContentView;
    }

}
