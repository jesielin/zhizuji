package zzj.com.zhizuji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import zzj.com.zhizuji.R;
import zzj.com.zhizuji.adapter.SocialAdapter;
import zzj.com.zhizuji.base.BaseFragment;
import zzj.com.zhizuji.network.Network;
import zzj.com.zhizuji.network.entity.SocialItem;
import zzj.com.zhizuji.network.entity.SocialTotal;
import zzj.com.zhizuji.util.DebugLog;

/**
 * Created by shawn on 2017-02-22.
 */

public class SocialFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private View mContentView;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private SocialAdapter mSocialAdapter;
    private Network mNetwork;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = View.inflate(getActivity(), R.layout.fragment_social, null);
        ButterKnife.bind(this, mContentView);
        mNetwork = Network.getInstance();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSocialAdapter = new SocialAdapter( getActivity());
        mRecyclerView.setAdapter(mSocialAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
                DebugLog.e("start refre");
            }
        });
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mSwipeRefreshLayout.setRefreshing(true);

    }

    @Override
    public void onRefresh() {
        DebugLog.e("do on refresh");

        mNetwork.getSocialItems("EE2", 1, 10)
                .subscribe(new Subscriber<SocialTotal>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SocialTotal socialTotal) {
                        DebugLog.e("json:"+new Gson().toJson(socialTotal));
                        if (socialTotal.list != null)
                            mSocialAdapter.setDatas(socialTotal.list);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }


}
