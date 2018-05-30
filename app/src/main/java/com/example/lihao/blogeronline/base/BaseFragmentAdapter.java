package com.example.lihao.blogeronline.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.example.lihao.blogeronline.utils.MyTextUtils;
import com.wenhuaijun.easytagdragview.bean.SimpleTitleTip;

import java.util.ArrayList;
import java.util.List;

public class BaseFragmentAdapter extends FragmentPagerAdapter {

    List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    private List<SimpleTitleTip> mTitles;

    public BaseFragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public BaseFragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<SimpleTitleTip> mTitles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.mTitles = mTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitles == null || mTitles.isEmpty()){
            return "";
        }
        return mTitles.get(position).getTip();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    //刷新fragment
        public void setFragments(FragmentManager fm, List<BaseFragment> fragments, List<SimpleTitleTip> mTitles) {
        this.mTitles = mTitles;
        if (this.fragmentList != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragmentList) {
                ft.remove(f);
            }
            /**
             * 由于activity存储fragment之后 再次commit回会报异常，所以使用commitAllowingStateLoss允许状态丢失
             */
            ft.commitAllowingStateLoss();
            ft = null;
            fm.executePendingTransactions();//主线程执行事务
        }
        this.fragmentList = fragments;
        notifyDataSetChanged();
    }
}
