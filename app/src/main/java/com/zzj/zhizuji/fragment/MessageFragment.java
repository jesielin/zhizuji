package com.zzj.zhizuji.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zzj.zhizuji.LoginActivity;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.SearchTechActivity;
import com.zzj.zhizuji.TalkActivity;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.network.entity.MessageResult;
import com.zzj.zhizuji.util.DebugLog;
import com.zzj.zhizuji.util.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shawn on 2017-02-22.
 */

public class MessageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected String getTitle() {
        return "消息";
    }

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.list)
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
                DebugLog.e("start refresh");
            }
        });
        recyclerView.setAdapter(myAdapter);

        loginEm();


        return mContentView;
    }



    @Override
    public void onRefresh() {
        Network.getInstance().getMyFriendship(SharedPreferenceUtils.getValue("UUID"))
                .subscribe(new Subscriber<List<MessageResult>>() {
                    @Override
                    public void onCompleted() {
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugLog.e("error:"+e.getMessage());
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<MessageResult> rs) {
                        if (rs != null) {
                            results.clear();
                            results.addAll(rs);
                            myAdapter.notifyDataSetChanged();
                        }
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

    private void loginEm(){
        EMClient.getInstance().login(SharedPreferenceUtils.getValue("UUID"), "123456", new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                DebugLog.d("登录聊天服务器成功！");
                SharedPreferenceUtils.setEmLogin(true);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                DebugLog.d("登录聊天服务器失败！");
                SharedPreferenceUtils.setEmLogin(false);
            }
        });
    }

    public class MessageVH extends RecyclerView.ViewHolder{

        @BindView(R.id.name)
        TextView tvName;
        public MessageVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private List<MessageResult> results = new ArrayList<>();

    private MyAdapter myAdapter = new MyAdapter();

    public class MyAdapter extends RecyclerView.Adapter<MessageVH>{

        @Override
        public MessageVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MessageVH(View.inflate(parent.getContext(),R.layout.item_message,null));
        }

        @Override
        public void onBindViewHolder(MessageVH holder, final int position) {
            holder.tvName.setText(results.get(position).nickName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!SharedPreferenceUtils.isEmLogin()){
                        Toast.makeText(getActivity(), "账户错误！", Toast.LENGTH_SHORT).show();
                        loginEm();
                        return;
                    }
                    Intent intent  = new Intent(getActivity(), TalkActivity.class);
                    intent.putExtra("UUID",results.get(position).uuid);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return results.size();
        }
    }
}
