package com.zzj.zhizuji.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.util.GlideCircleTransform;

/**
 * Created by shawn on 17/2/27.
 */

public class PhotoReviewPagerAdapter extends PagerAdapter {
    private int mSize;
    private String[] datas;

    public PhotoReviewPagerAdapter() {
        mSize = 5;
    }

    public PhotoReviewPagerAdapter(String[] datas) {
        this.datas = datas;
    }

    @Override public int getCount() {
        return datas.length;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override public Object instantiateItem(ViewGroup view, int position) {
        View itemView = View.inflate(view.getContext(), R.layout.item_photoreview, null);
        view.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView iv = (ImageView) itemView.findViewById(R.id.iv_photo);
        Glide.with(view.getContext()).load(datas[position])
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo)
                .into(iv);
        return itemView;
    }

}
