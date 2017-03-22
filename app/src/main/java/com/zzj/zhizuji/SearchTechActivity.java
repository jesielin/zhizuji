package com.zzj.zhizuji;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.network.entity.Tech;
import com.zzj.zhizuji.util.DebugLog;
import com.zzj.zhizuji.util.KeyboardControlMnanager;
import com.zzj.zhizuji.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by shawn on 17/3/19.
 */

public class SearchTechActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title)
    EditText etSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tech);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
//        refreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                refreshLayout.setRefreshing(true);
//                onRefresh();
//                DebugLog.e("start refre");
//            }
//        });
        recyclerView.setAdapter(myAdapter);

        etSearch.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    UIHelper.hideInputMethod(etSearch);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    refreshLayout.setRefreshing(true);
                    onRefresh();

                }
                return false;
            }
        });
    }

    private void search(){

        Network.getInstance().searchTech(1,10,etSearch.getText().toString())
                .subscribe(new Subscriber<List<Tech>>() {
                    @Override
                    public void onCompleted() {
                        DebugLog.e("complete");
                        refreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugLog.e("error:"+e.getMessage());

                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<Tech> ts) {
                        DebugLog.e("json:"+new Gson().toJson(teches));
                        teches.clear();
                        teches.addAll(ts);
                        myAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);

                    }
                });

    }

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.list)
    RecyclerView recyclerView;

    private List<Tech> teches = new ArrayList<>();

    @Override
    public void onRefresh(){
        search();
    }

    @OnClick(R.id.cancel)
    public void cancel(View view){
        finish();
    }

    public class SearchVH extends RecyclerView.ViewHolder{

        @BindView(R.id.name)
        TextView tvName;
        public SearchVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private MyAdapter myAdapter = new MyAdapter();

    public class MyAdapter extends RecyclerView.Adapter<SearchVH>{

        @Override
        public SearchVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SearchVH(View.inflate(parent.getContext(),R.layout.item_search,null));
        }

        @Override
        public void onBindViewHolder(SearchVH holder, int position) {
            holder.tvName.setText(teches.get(position).nickName);
        }

        @Override
        public int getItemCount() {
            return teches.size();
        }
    }
}
