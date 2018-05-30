package com.example.lihao.blogeronline.adapter;

import android.support.v4.app.FragmentManager;

import com.example.lihao.blogeronline.base.BaseFragment;
import com.example.lihao.blogeronline.base.BaseFragmentAdapter;
import com.wenhuaijun.easytagdragview.bean.SimpleTitleTip;

import java.util.List;

/**
 * Created by lihao on 17-11-17.
 */

public class MainFragmentAdapter extends BaseFragmentAdapter {

    public MainFragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm, fragmentList);
    }

    public MainFragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<SimpleTitleTip> mTitles) {
        super(fm, fragmentList, mTitles);
    }
}
