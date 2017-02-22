package com.zzj.zhizuji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.network.entity.SocialItem;
import com.zzj.zhizuji.util.GlideCircleTransform;
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

    public void setDatas(List<SocialItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public SocialAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TYPE_HEADER == viewType)
            return new SocialHeaderViewHolder(View.inflate(mContext, R.layout.header_social, null));
        else
            return new SocialViewHolder(View.inflate(mContext, R.layout.item_social, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (TYPE_HEADER == getItemViewType(position)) {

        } else {
//            DebugLog.e("posi:"+position);
            final SocialItem item = datas.get(position - HEADER_SIZE);
            final SocialViewHolder vh = (SocialViewHolder) holder;
            vh.tvTitle.setText(item.momentOwner);

            Glide.with(mContext).load("http://www.qqtouxiang.com/d/file/qinglv/20170212/9fd014a7c29552d364d58e5df64f0ed5.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo).transform(new GlideCircleTransform(mContext))
                    .into(vh.ivAvatar);

            vh.tvContent.setText(UrlUtils.formatUrlString(item.message),item.isExpand());
            vh.tvContent.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
                @Override
                public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                    item.setExpand(isExpanded);
                }
            });

//            vh.tvContent.setOnExpandStateChangeListener();
//            if (position % 2 == 0)
//                vh.tvContent.setText(UrlUtils.formatUrlString("的的的的的的http://www.baidu.com的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的的"));
//            if(!TextUtils.isEmpty(item.message)) {
//                vh.tvContent.setVisibility(View.VISIBLE);
//                vh.tvContent.setExpand(item.isExpand());
//                vh.tvContent.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
//                    @Override
//                    public void statusChange(boolean isExpand) {
//                        item.setExpand(isExpand);
//                    }
//                });
//
//
//                vh.tvContent.setText(UrlUtils.formatUrlString(item.message));
//                if (position % 2 == 0)
//                    vh.tvContent.setText(UrlUtils.formatUrlString("哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈http://www.baidu.com哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈"));
//
//            }else {
//                vh.tvContent.setVisibility(View.GONE);
//            }

            vh.btnPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "点了", Toast.LENGTH_SHORT).show();
                }
            });

            if (item.hasComments()) {//处理评论列表
                vh.listComment.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int commentPosition) {
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

    @Override
    public int getItemCount() {
        if (datas != null && datas.size() >= 0)
            return datas.size() + HEADER_SIZE;
        else
            return HEADER_SIZE;
    }

    private int TYPE_HEADER = 0x0001;
    private int TYPE_SOCIAL = 0x0002;

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_SOCIAL;
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

        public SocialViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SocialHeaderViewHolder extends RecyclerView.ViewHolder {

        public SocialHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

}
