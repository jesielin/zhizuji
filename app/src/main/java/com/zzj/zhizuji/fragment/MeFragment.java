package com.zzj.zhizuji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.util.GlideCircleTransform;
import com.zzj.zhizuji.util.SharedPreferenceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shawn on 2017-02-22.
 */

public class MeFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected String getTitle() {
        return "我";
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @BindView(R.id.imv)
    ImageView imv;
    @BindView(R.id.name)
    TextView tvName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        String avator = SharedPreferenceUtils.getValue("AVATOR");
        Glide.with(getActivity()).load(avator)
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.avator_placeholder).transform(new GlideCircleTransform(getActivity()))
                .into(imv);

        tvName.setText(SharedPreferenceUtils.getValue("NICKNAME"));
        return mContentView;
    }

}
