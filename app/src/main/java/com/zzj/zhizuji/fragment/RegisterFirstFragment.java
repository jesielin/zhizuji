package com.zzj.zhizuji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.RegisterActivity;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.util.DebugLog;
import com.zzj.zhizuji.util.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by shawn on 17/3/5.
 */

public class RegisterFirstFragment extends BaseFragment {

    @BindView(R.id.name)
    TextInputEditText etName;
    @BindView(R.id.verify)
    TextInputEditText etVerify;
    @BindView(R.id.pwd)
    TextInputEditText etPwd;
    @BindView(R.id.repwd)
    TextInputEditText etRePwd;

    String type = "1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);



        return mContentView;
    }

    @Override
    protected String getTitle() {
        return "注册";
    }

    @OnClick({R.id.single,R.id.operator})
    public void choose(View view){
        switch (view.getId()){
            case R.id.single:
                type = "1";
                break;
            case R.id.operator:
                type = "2";
                break;
        }
    }

    @OnClick(R.id.next)
    public void next(View view){
        String name = etName.getText().toString();
        String verify = etVerify.getText().toString();
        String pwd = etPwd.getText().toString();
        String repwd = etRePwd.getText().toString();



        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(verify)) {
            Toast.makeText(getActivity(), "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(repwd)){
            Toast.makeText(getActivity(), "确认密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!repwd.equals(pwd)){
            Toast.makeText(getActivity(), "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        Network.getInstance().register(name,verify,pwd)
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugLog.e("error message:"+e.getMessage());
                    }

                    @Override
                    public void onNext(Object o) {
                        DebugLog.e("register result:"+new Gson().toJson(o));
                    }
                });
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,ViewUtils.createFragment(RegisterSecondFragment.class)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack("second").commit();
    }

    @OnClick(R.id.back)
    public void back(View view){
        getActivity().onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_first;
    }


}
