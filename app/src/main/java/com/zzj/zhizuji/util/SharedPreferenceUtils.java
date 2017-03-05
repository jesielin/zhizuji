package com.zzj.zhizuji.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by shawn on 17/3/5.
 */

public class SharedPreferenceUtils {

    private static SharedPreferences instance;
    private SharedPreferenceUtils(){

    }

    public static void init(Application application){
        instance = application.getSharedPreferences("DATA", Context.MODE_PRIVATE);
    }

    public static boolean isLogin(){
        if (TextUtils.isEmpty(instance.getString("LOGIN_NAME","")))
            return false;
        else
            return true;

    }
}
