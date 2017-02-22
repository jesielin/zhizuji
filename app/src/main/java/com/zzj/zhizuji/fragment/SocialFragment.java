package com.zzj.zhizuji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.adapter.SocialAdapter;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.network.entity.SocialItem;
import com.zzj.zhizuji.network.entity.SocialTotal;
import com.zzj.zhizuji.util.DebugLog;

/**
 * Created by shawn on 2017-02-22.
 */

public class SocialFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private View mContentView;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.edit_body)
    View mCommentEditBody;
    @BindView(R.id.et)
    EditText etComment;
    @BindView(R.id.send)
    Button btnSend;

    private SocialAdapter mSocialAdapter;
    private Network mNetwork;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = View.inflate(getActivity(), R.layout.fragment_social, null);
        ButterKnife.bind(this, mContentView);
        mNetwork = Network.getInstance();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),mLayoutManager
                .getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    Glide.with(getActivity()).resumeRequests();
                }else{
                    Glide.with(getActivity()).pauseRequests();
                }

            }
        });
        return mContentView;
    }



    @Override
    public void onRefresh() {
        DebugLog.e("do on refresh");

        mNetwork.getSocialItems("EE2", 1, 20)
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
                        if (socialTotal.list != null) {
                            for (SocialItem item : socialTotal.list){
                                item.message = "哈哈哈http://www.baidu.com哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈";
                            }

                            mSocialAdapter.setDatas(socialTotal.list);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void showCommentEditBody(){

    }


}
