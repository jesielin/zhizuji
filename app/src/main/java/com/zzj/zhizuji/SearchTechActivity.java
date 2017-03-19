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

import com.google.gson.Gson;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.network.entity.Tech;
import com.zzj.zhizuji.util.DebugLog;
import com.zzj.zhizuji.util.KeyboardControlMnanager;
import com.zzj.zhizuji.util.UIHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        refreshLayout.setOnRefreshListener(this);
//        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
//        refreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                refreshLayout.setRefreshing(true);
//                onRefresh();
//                DebugLog.e("start refre");
//            }
//        });
        etSearch.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    UIHelper.hideInputMethod(etSearch);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    search();
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugLog.e("error:"+e.getMessage());

                    }

                    @Override
                    public void onNext(List<Tech> teches) {
                        DebugLog.e("json:"+new Gson().toJson(teches));

                    }
                });

    }

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.list)
    RecyclerView recyclerView;


    @Override
    public void onRefresh(){

    }

    public class SearchVH extends RecyclerView.ViewHolder{

        public SearchVH(View itemView) {
            super(itemView);
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<SearchVH>{

        @Override
        public SearchVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(SearchVH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
