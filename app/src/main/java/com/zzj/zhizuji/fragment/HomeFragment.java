package com.zzj.zhizuji.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.zzj.zhizuji.R;
import com.zzj.zhizuji.SearchTechActivity;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.util.DebugLog;

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
        return "搜索技师";
    }


    @OnClick(R.id.title)
    public void toSearch(View view){
        startActivity(new Intent(getActivity(), SearchTechActivity.class));
    }



}
