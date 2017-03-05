package com.zzj.zhizuji.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.zzj.zhizuji.PhotoReviewActivity;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.fragment.SocialFragment;
import com.zzj.zhizuji.network.entity.CommentItem;
import com.zzj.zhizuji.network.entity.SocialItem;
import com.zzj.zhizuji.util.DebugLog;
import com.zzj.zhizuji.util.GlideCircleTransform;
import com.zzj.zhizuji.util.UIHelper;
import com.zzj.zhizuji.util.UrlUtils;
import com.zzj.zhizuji.widget.CommentListView;
import com.zzj.zhizuji.widget.ExpandableTextView;

/**
 * Created by shawn on 17/2/22.
 */

public class SocialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SocialItem> datas;
    private int HEADER_SIZE = 1;
    private Context mContext;
    private PopupWindow comPop;
    private View commentView;

    public void setDatas(List<SocialItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void addAll(List<SocialItem> datas){
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    private CommentClickListener commentClickListener;
    private SendCommentClickListener sendCommentClickListener;

    public SocialAdapter(Context context, CommentClickListener listener,SendCommentClickListener sendCommentClickListener) {
        this.mContext = context;
        this.commentClickListener = listener;
        this.sendCommentClickListener = sendCommentClickListener;
        initPop();

    }

    private void initPop(){
        commentView = View.inflate(mContext,R.layout.popup_comment,null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TYPE_HEADER == viewType)
            return new SocialHeaderViewHolder(View.inflate(mContext, R.layout.header_social, null));
        else if (TYPE_MORE == viewType)
            return new SocialLoadMoreViewHolder(View.inflate(mContext, R.layout.item_load_more, null));
        else
            return new SocialViewHolder(View.inflate(mContext, R.layout.item_social, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (TYPE_HEADER == getItemViewType(position)) {

        } else if (TYPE_MORE == getItemViewType(position)) {
            if (onLoadMoreListener != null)
                onLoadMoreListener.onLoadMore();
        } else {
//            DebugLog.e("posi:"+position);
            final SocialItem item = datas.get(position - HEADER_SIZE);
            final SocialViewHolder vh = (SocialViewHolder) holder;
            vh.tvTitle.setText(item.momentOwner);

            Glide.with(mContext).load("http://www.qqtouxiang.com/d/file/qinglv/20170212/9fd014a7c29552d364d58e5df64f0ed5.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo).transform(new GlideCircleTransform(mContext))
                    .into(vh.ivAvatar);

            vh.tvContent.setText(UrlUtils.formatUrlString(item.message), item.isExpand());
            vh.tvContent.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
                @Override
                public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                    item.setExpand(isExpanded);
                }
            });


            vh.imv.setImagesData(item.photos);
            final int[] locas = new int[2];

            vh.btnPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comPop = new PopupWindow(commentView,RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT,true);
                    comPop.setBackgroundDrawable(new BitmapDrawable());
                    comPop.getContentView().findViewById(R.id.item_like).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "zan", Toast.LENGTH_SHORT).show();
                            comPop.dismiss();
                        }
                    });
                    comPop.getContentView().findViewById(R.id.item_comment).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "comment", Toast.LENGTH_SHORT).show();
                            if (sendCommentClickListener != null){
                                vh.itemView.getLocationInWindow(locas);
                                DebugLog.e("in adapter location:"+locas[1]);
                                sendCommentClickListener.onSendCommentClick(item,locas[1]+vh.itemView.getMeasuredHeight());
                            }
                            comPop.dismiss();
                        }
                    });

                    comPop.showAsDropDown(v, -UIHelper.dipToPx(140),-(UIHelper.dipToPx(35)+v.getMeasuredHeight())/2);
//                    pp.showAsDropDown(v,0,0);
                }
            });
//            vh.btnPop.setOnClickListener(onClickListener);

            if (item.hasComments()) {//处理评论列表
                vh.listComment.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int commentPosition) {
                        CommentItem commentItem = item.comments.get(commentPosition);
                        if (item.momentOwner.equals(commentItem.commenterUUID)) {
                            showDeleteDialog();

                        } else if (commentClickListener != null)
                            commentClickListener.onCommentClick(item,commentItem, commentPosition, vh.listComment);
                    }
                });
                vh.listComment.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(int commentPosition) {
                        //长按进行复制或者删除
                    }
                });
                vh.listComment.setDatas(item.comments);
                vh.listComment.setVisibility(View.VISIBLE);

            } else {
                vh.listComment.setVisibility(View.GONE);
            }
        }
    }


    private void showDeleteDialog() {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(R.layout.dialog_delete);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();

//        builder.setView()
    }

    @Override
    public int getItemCount() {
        if (datas != null && datas.size() >= 0) {
            if (mCanLoadMore)
                return datas.size() + HEADER_SIZE + 1;
            else
                return datas.size() + HEADER_SIZE;
        } else
            return HEADER_SIZE;
    }

    private int TYPE_HEADER = 0x0001;
    private int TYPE_SOCIAL = 0x0002;
    private int TYPE_MORE = 0x0003;

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == getItemCount() - 1 && mCanLoadMore) {
            return TYPE_MORE;
        } else {
            return TYPE_SOCIAL;
        }

    }

    class SocialViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView tvTitle;

        @BindView(R.id.avatar)
        ImageView ivAvatar;
        @BindView(R.id.pop)
        ImageButton btnPop;
        @BindView(R.id.comment_list)
        CommentListView listComment;

        @BindView(R.id.expand_text_view)
        ExpandableTextView tvContent;

        @BindView(R.id.imv)
        NineGridImageView<String> imv;

        public SocialViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imv.setAdapter(mAdapter);

        }

        private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
                Glide.with(mContext).load(s)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo)
                        .into(imageView);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }

            @Override
            protected void onItemImageClick(Context context, int index, List<String> list) {
                super.onItemImageClick(context, index, list);
                Intent intent = new Intent(mContext,PhotoReviewActivity.class);
                intent.putExtra("position",index);
                intent.putExtra("list", list.toArray(new String[0]));
                mContext.startActivity(intent);

                Toast.makeText(mContext, "posi:"+index+",url:"+list.get(index), Toast.LENGTH_SHORT).show();
            }
        };
    }



    class SocialHeaderViewHolder extends RecyclerView.ViewHolder {

        public SocialHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class SocialLoadMoreViewHolder extends RecyclerView.ViewHolder {

        public SocialLoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }


    public interface CommentClickListener {
        void onCommentClick(SocialItem item,CommentItem commentItem, int commentPosition, CommentListView listView);

    }

    public interface SendCommentClickListener{
        void onSendCommentClick(SocialItem item,int locaY);
    }

    private boolean mCanLoadMore;

    public void setCanLoadMore(boolean canLoadMore) {
        this.mCanLoadMore = canLoadMore;
        notifyDataSetChanged();
    }

    private OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
}
