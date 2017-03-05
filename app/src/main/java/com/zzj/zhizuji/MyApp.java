package com.zzj.zhizuji;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.zzj.zhizuji.util.SharedPreferenceUtils;

/**
 * Created by shawn on 17/2/22.
 */

public class MyApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        SharedPreferenceUtils.init(this);
    }

    public static Context getContext() {
        return context;
    }
}
