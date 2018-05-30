package com.example.lihao.blogeronline.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lihao on 17-11-17.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;

    private Unbinder mUnbinder;

    protected boolean isInited = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        doSomethingBeforeSetContentView();

        if(rootView == null){
            rootView = inflater.inflate(getLayoutResource(),container,false);
        }
        mUnbinder = ButterKnife.bind(this,rootView);

        initView();

        if(!isInited){
            initData();
            isInited = true;
        }
        return rootView;
    }

    protected void doSomethingBeforeSetContentView(){};

    //获取布局文件
    protected abstract int getLayoutResource();

    //初始化View
    protected abstract void initView();

    //初始化Data
    public abstract void initData();


    @Override
    public void onDestroyView() {
        if(mUnbinder == null){
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }
}
