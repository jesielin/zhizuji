package com.zzj.zhizuji.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zzj.zhizuji.MyApp;
import com.zzj.zhizuji.R;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by 大灯泡 on 2016/10/26.
 * <p>
 * ui工具类
 */
public class UIHelper {

    // =============================================================tools
    // methods


    public static void setTitle(String title, final AppCompatActivity activity){
        ((TextView)activity.findViewById(R.id.title)).setText(title);
        View viewById = activity.findViewById(R.id.back);
        if (viewById != null)
            viewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
    }

    /**
     * dip转px
     */
    public static int dipToPx(float dip) {
        return (int) (dip * MyApp.getContext().getResources()
                                           .getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dip
     */
    public static int pxToDip(float pxValue) {
        final float scale = MyApp.getContext().getResources()
                                           .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(float spValue) {
        final float fontScale = MyApp.getContext().getResources()
                                               .getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕分辨率：宽
     */
    public static int getScreenWidthPix(@Nullable Context context) {
        if (context == null) context = MyApp.getContext();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    /**
     * 获取屏幕分辨率：高
     */
    public static int getScreenHeightPix(@Nullable Context context) {
        if (context == null) context = MyApp.getContext();
        int height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources()
                            .getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 隐藏软键盘
     */
    public static void hideInputMethod(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                                                              .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DebugLog.e(e.getMessage());
        }
    }

    public static void hideInputMethod(final View view, long delayMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideInputMethod(view);
            }
        }, delayMillis);
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(View view) {
        if (view == null) return;
        if (view instanceof EditText) view.requestFocus();
//        if (view != null){
//            view.setVisibility(View.VISIBLE);
//            view.findViewById(editId).requestFocus();
//        }
        InputMethodManager imm = (InputMethodManager) view.getContext()
                                                          .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            boolean success = imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            DebugLog.i("showSoftKeyboard"+" isSuccess   >>>   " + success);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 多少时间后显示软键盘
     */
    public static void showInputMethod(final View view, long delayMillis) {
        if (view != null)
        // 显示输入法
        {
            view.postDelayed(new Runnable() {

                @Override
                public void run() {
                    UIHelper.showInputMethod(view);
                }
            }, delayMillis);
        }
    }


    /**
     * Toast封装
     */
    public static void ToastMessage(String msg) {
        Toast.makeText(MyApp.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * =============================================================
     * 一些工具方法
     */
    public static void setViewsClickListener(@NonNull View.OnClickListener listener, View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
    }


    /**
     * =============================================================
     * 资源工具
     */

    public static int getResourceColor(int colorResId) {
        try {
            return MyApp.getContext()
                                  .getResources()
                                  .getColor(colorResId);
        } catch (Exception e) {
            return Color.TRANSPARENT;
        }
    }



    public static SweetAlertDialog showProgressDialog(Context context,String loadingText){
        SweetAlertDialog dialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE).setTitleText(loadingText);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static void showDialogSuccess(SweetAlertDialog dialog, String successText, SweetAlertDialog.OnSweetClickListener listener){
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        dialog.setTitleText(successText);
        dialog.setConfirmClickListener(listener);

    }

    public static void showDialogFailed(SweetAlertDialog dialog,String failText){
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        dialog.setTitleText(failText);

    }



}
