package com.zzj.zhizuji.fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.zzj.zhizuji.R;
import com.zzj.zhizuji.RegisterActivity;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.util.ViewUtils;

import butterknife.OnClick;

/**
 * Created by shawn on 17/3/5.
 */

public class RegisterFirstFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_first;
    }

    @Override
    protected String getTitle() {
        return "注册";
    }

    @OnClick(R.id.next)
    public void next(View view){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,ViewUtils.createFragment(RegisterSecondFragment.class)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack("second").commit();
    }
}
