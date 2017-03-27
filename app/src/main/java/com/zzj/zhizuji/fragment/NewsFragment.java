package com.zzj.zhizuji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import com.zzj.zhizuji.R;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.util.DebugLog;

/**
 * Created by shawn on 2017-02-22.
 */

public class NewsFragment extends BaseFragment {



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected String getTitle() {
        return "咨询";
    }

    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        viewPager.setAdapter(new NewsPagerAdapter(getActivity().getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return mContentView;
    }

    private class NewsPagerAdapter extends FragmentPagerAdapter{


        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Fragment f1 = new VideoNewsFragment();
                    return f1;
                case 1:
                    Fragment f2 = new TextNewsFragment();
                    return f2;
            }
            return null;
        }



        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "视频";
                case 1:
                    return "咨询";

            }
            return "";
        }
    }
}
