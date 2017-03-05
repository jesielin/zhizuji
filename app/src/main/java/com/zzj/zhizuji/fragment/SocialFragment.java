package com.zzj.zhizuji.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import rx.Subscriber;

import com.zzj.zhizuji.LoginActivity;
import com.zzj.zhizuji.PostSocialActivity;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.adapter.SocialAdapter;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.network.entity.CommentItem;
import com.zzj.zhizuji.network.entity.SocialItem;
import com.zzj.zhizuji.network.entity.SocialTotal;
import com.zzj.zhizuji.util.DebugLog;
import com.zzj.zhizuji.util.KeyboardControlMnanager;
import com.zzj.zhizuji.util.SharedPreferenceUtils;
import com.zzj.zhizuji.util.UIHelper;
import com.zzj.zhizuji.widget.CommentListView;

/**
 * Created by shawn on 2017-02-22.
 */

public class SocialFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, SocialAdapter.CommentClickListener, SocialAdapter.OnLoadMoreListener, SocialAdapter.SendCommentClickListener {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private SocialAdapter mSocialAdapter;
    private Network mNetwork;
    private LinearLayoutManager mLayoutManager;

    private PopupWindow popupWindow;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

            mNetwork = Network.getInstance();
            initView();
            return mContentView;

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_social;
    }

    @Override
    protected String getTitle() {
        return "知脊圈";
    }



    private void initPopupEdit(){
        popupWindow = new PopupWindow(getActivity());
        popupWindow.setContentView(View.inflate(getActivity(), R.layout.pop_edit_comment, null));
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        popupWindow.setOutsideTouchable(false);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        //设置输入框不被输入法遮挡
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @OnClick(R.id.post)
    public void post(){
        Intent intent = new Intent(getActivity(), PostSocialActivity.class);
        startActivityForResult(intent,1);
    }

    private void initView(){
        initPopupEdit();



        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSocialAdapter = new SocialAdapter(getActivity(), this,this);
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), mLayoutManager
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getActivity()).resumeRequests();
                } else {
                    Glide.with(getActivity()).pauseRequests();
                }

            }
        });
        mRecyclerView.setFocusable(true);
        mRecyclerView.setFocusableInTouchMode(true);

        KeyboardControlMnanager.observerKeyboardVisibleChange(getActivity(), new KeyboardControlMnanager.OnKeyboardStateChangeListener() {
            @Override
            public void onKeyboardChange(int displayHeight,int statusbarHeight, boolean isVisible) {
                if (isVisible) {
                    mRecyclerView.smoothScrollBy(0,  popLocationY-(displayHeight - popupWindow.getContentView().getHeight())-statusbarHeight);
                    DebugLog.e("displayHeight:"+displayHeight);
                    DebugLog.e("status bar height:"+statusbarHeight);
                    DebugLog.e("pop height:"+popupWindow.getContentView().getHeight());
                    DebugLog.e("item location:"+popLocationY);
                    DebugLog.e("scroll dis:"+(popLocationY-(displayHeight - popupWindow.getContentView().getHeight() )));
                }

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String message = data.getStringExtra("MESSAGE");
        ArrayList<MediaBean> photos = data.getParcelableArrayListExtra("PHOTOS");
        DebugLog.e("message:"+message);
        for (MediaBean bean : photos){
            DebugLog.e("result:"+bean.toString());

        }
    }

    //TODO:
    private int page = 1;
    private int PAGE_SIZE = 10;
    @Override
    public void onRefresh() {
        DebugLog.e("do on refresh");

        request(REQUEST_TYPE_REFRESH);


    }

    private static final int REQUEST_TYPE_REFRESH = 0x0011;
    private static final int REQUEST_TYPE_LOAD_MORE = 0x0012;

    //TODO:
    private String[] IMG_URL_LIST = {
            "http://img3.imgtn.bdimg.com/it/u=1794894692,1423685501&fm=214&gp=0.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-480302.jpg",
            "http://ac-QYgvX1CC.clouddn.com/36f0523ee1888a57.jpg", "http://ac-QYgvX1CC.clouddn.com/07915a0154ac4a64.jpg",
            "http://ac-QYgvX1CC.clouddn.com/9ec4bc44bfaf07ed.jpg", "http://ac-QYgvX1CC.clouddn.com/fa85037f97e8191f.jpg",
            "http://ac-QYgvX1CC.clouddn.com/de13315600ba1cff.jpg", "http://ac-QYgvX1CC.clouddn.com/15c5c50e941ba6b0.jpg",
            "http://ac-QYgvX1CC.clouddn.com/10762c593798466a.jpg", "http://ac-QYgvX1CC.clouddn.com/eaf1c9d55c5f9afd.jpg"


    };

    private void request(final int type){

        if (type == REQUEST_TYPE_REFRESH)
            page = 1;


        mNetwork.getSocialItems("FF1", page, PAGE_SIZE)
                .subscribe(new Subscriber<SocialTotal>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "网络错误!", Toast.LENGTH_SHORT).show();
                        if (type == REQUEST_TYPE_REFRESH)
                            mSwipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onNext(SocialTotal socialTotal) {
                        DebugLog.e("json:" + new Gson().toJson(socialTotal));
                        DebugLog.e("total:"+socialTotal.total+",page:"+page);
                        setupLoadMore(socialTotal);


                        if (socialTotal.list != null) {
                            for (int i = 0 ; i < socialTotal.list.size() ; i ++){
                                socialTotal.list.get(i).photos = Arrays.asList(IMG_URL_LIST).subList(0, i % 9);
                            }
                            switch (type){
                                case REQUEST_TYPE_REFRESH:
                                    mSocialAdapter.setDatas(socialTotal.list);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    break;
                                case REQUEST_TYPE_LOAD_MORE:mSocialAdapter.addAll(socialTotal.list);

                                    break;
                            }
                        }
                    }
                });
    }

    @Override
    public void onLoadMore() {
        request(REQUEST_TYPE_LOAD_MORE);
    }

    private void setupLoadMore(SocialTotal socialTotal){
        if (socialTotal.total == page){
            mSocialAdapter.setCanLoadMore(false);
            mSocialAdapter.setOnLoadMoreListener(null);
        }else {
            mSocialAdapter.setCanLoadMore(true);
            mSocialAdapter.setOnLoadMoreListener(this);
        }
        page++;

    }


    private int popLocationY;
    private int scrollDistance;
    private Button btnSend;
    private EditText etComment;

    @Override
    public void onCommentClick(SocialItem item,CommentItem commentItem,int commentPosition, CommentListView listView) {
        View commentView = listView.getChildAt(commentPosition);
        int[] commentLocations = new int[2];
        commentView.getLocationInWindow(commentLocations);
        popLocationY = commentLocations[1];
        showKeyboard(item,commentPosition,listView);


    }


    public void showKeyboard(final SocialItem item, final int commentPosition, final CommentListView listView){
        if (btnSend == null){
            btnSend = (Button) popupWindow.getContentView().findViewById(R.id.send);
        }
        if (etComment == null)
            etComment = (EditText) popupWindow.getContentView().findViewById(R.id.et);
        etComment.setText("");
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etComment.getText()))
                    Toast.makeText(getActivity(), "评论不能为空", Toast.LENGTH_SHORT).show();
                else
                    sendComment(item,commentPosition,etComment.getText().toString(),listView);
                Toast.makeText(getActivity(), "发送了："+etComment.getText(), Toast.LENGTH_SHORT).show();

                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(mContentView, Gravity.BOTTOM, 0, 0);
        UIHelper.showInputMethod(popupWindow.getContentView().findViewById(R.id.et), 150);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.setFocusable(false);
            }
        });
    }

    @Override
    public void onSendCommentClick(SocialItem item,int locaY,CommentListView listView) {
//        View child = mRecyclerView.getChildAt(childPosition);
//
//        int[] commentLocations = new int[2];
//        DebugLog.e("child:"+child);
//        if (child != null)
//            child.getLocationInWindow(commentLocations);
        popLocationY = locaY;
        showKeyboard(item,-1,listView);

    }

    public void sendComment(final SocialItem item, int commentPosition, String message, final CommentListView listView){
        if (commentPosition == -1){
            //评论朋友圈


        }else {
            //评论其他人
            String friendID = item.comments.get(commentPosition).commenterUUID;
            mNetwork.sendComment(item.momentsID,item.momentOwner,"FF3",friendID,message)
            .subscribe(new Subscriber<Object>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    DebugLog.e("error message:"+e.getMessage());
                }

                @Override
                public void onNext(Object o) {
                    DebugLog.e("send comment result:"+new Gson().toJson(o));

                }
            });
        }
    }

}
