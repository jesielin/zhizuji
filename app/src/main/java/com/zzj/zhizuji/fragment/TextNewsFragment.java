package com.zzj.zhizuji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.network.entity.NewsResult;
import com.zzj.zhizuji.util.CommonUtils;
import com.zzj.zhizuji.util.GlideCircleTransform;
import com.zzj.zhizuji.util.GlideRoundTransform;
import com.zzj.zhizuji.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import rx.Subscriber;

/**
 * Created by shawn on 17/3/27.
 */

public class TextNewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    View mContentView;


    private List<NewsResult> datas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = View.inflate(getActivity(),R.layout.fragment_text_news,null);
        ButterKnife.bind(this,mContentView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(textNewsAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });


        return mContentView;
    }

    @Override
    public void onRefresh() {
        Network.getInstance().getNews("1")
                .subscribe(new Subscriber<List<NewsResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<NewsResult> newsResults) {
                        if (newsResults != null) {
                            datas.clear();
                            datas.addAll(newsResults);
                            textNewsAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

    class TextNewsVH extends RecyclerView.ViewHolder{

        @BindView(R.id.title)
        TextView tvTitle;
        @BindView(R.id.subtitle)
        TextView tvSubTitle;
        @BindView(R.id.imv)
        ImageView imv;
        @BindView(R.id.hot)
        TextView tvHot;
        @BindView(R.id.date)
        TextView tvDate;

        public TextNewsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


    }

    private TextNewsAdapter textNewsAdapter = new TextNewsAdapter();
    private class TextNewsAdapter extends RecyclerView.Adapter<TextNewsVH>{

        @Override
        public TextNewsVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TextNewsVH(View.inflate(parent.getContext(),R.layout.item_text_news,null));
        }

        @Override
        public void onBindViewHolder(TextNewsVH holder, int position) {

            NewsResult item = datas.get(position);
            Glide.with(getActivity()).load(item.titleImgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.common_placeholder3)
                    .into(holder.imv);
            holder.tvTitle.setText(item.title);
            holder.tvSubTitle.setText(item.contents);
            holder.tvHot.setText(item.hot);
            holder.tvDate.setText(CommonUtils.getDate(Double.valueOf(item.createDate)));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }
}
