package com.example.lihao.blogeronline.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.lihao.blogeronline.app.AppManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lihao on 17-10-22.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doBeforeSetContentView();

        setContentView(setLayout());

        mUnbinder = ButterKnife.bind(this);

        initView();

        setPresenter();

        initData();
    }

    protected abstract void initView();

    protected void initData(){}

    @Override
    protected void onDestroy() {
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
        AppManager.getAppManager().removeActivity(this);

        super.onDestroy();
    }



    protected void doBeforeSetContentView(){
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);
    }

    protected abstract int setLayout();

    protected abstract void setPresenter();

}
