package com.zzj.zhizuji;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zzj.zhizuji.adapter.PhotoReviewPagerAdapter;
import com.zzj.zhizuji.util.DebugLog;

/**
 * Created by shawn on 2017-02-26.
 */

public class PhotoReviewActivity extends AppCompatActivity {
    String[] list;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_photo_review);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        Intent intent = getIntent();
        if (intent != null) {
            list = intent.getStringArrayExtra("list");
            position = intent.getIntExtra("position", 0);
        }

        DebugLog.e("list:" + list);
        DebugLog.e("position:" + position);
        if (list != null)
            viewPager.setAdapter(new PhotoReviewPagerAdapter(list));

        viewPager.setCurrentItem(position);
    }


}
