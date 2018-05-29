package com.example.lihao.blogeronline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lihao.blogeronline.R;
import com.example.lihao.blogeronline.adapter.item.ItemBasePage;
import com.example.lihao.blogeronline.bean.Article;

import java.util.List;


/**
 * Created by lihao on 17-11-21.
 */

public class MyBasePageAdapter extends AbstractFooterAdapter<Article, ItemBasePage> {

    private View mFooterView;

    private LoadStatus status = LoadStatus.NORMAL;

    public MyBasePageAdapter(Context context, List<Article> data) {
        super(context, data);
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                updateFooterView();
            }
        });
    }

    @Override
    public ItemBasePage createView(Context mContext) {
        return new ItemBasePage(mContext);
    }

    @Override
    protected boolean isFooterEnabled() {
        return true;
    }


    @Override
    protected View createFooterView() {

        if (mFooterView == null) {
            mFooterView = View.inflate(mContext, R.layout.layout_load_more, null);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.gravity = Gravity.CENTER;
            mFooterView.setLayoutParams(layoutParams);
        }
        updateFooterView();

        return mFooterView;
    }

    public void updateFooterView() {

        if(mFooterView == null){
            return;
        }

        ProgressBar progressBar = (ProgressBar)mFooterView.findViewById(R.id.progress);
        TextView tvDes = (TextView)mFooterView.findViewById(R.id.tv_des);
        if(status == LoadStatus.IS_NO_MORE){

            progressBar.setVisibility(View.GONE);
            tvDes.setVisibility(View.VISIBLE);
            tvDes.setText("没有更多了");

            mFooterView.setVisibility(View.VISIBLE);
        }else if(status == LoadStatus.IS_LOAD_MORE){

            progressBar.setVisibility(View.VISIBLE);
            tvDes.setVisibility(View.VISIBLE);
            tvDes.setText("加载更多..");

            mFooterView.setVisibility(View.VISIBLE);
        }else {
            mFooterView.setVisibility(View.GONE);
        }
    }

    public LoadStatus getStatus() {
        return status;
    }

    public void setStatus(LoadStatus status) {
        this.status = status;
    }

    public static enum LoadStatus {
        IS_NO_MORE,
        IS_LOAD_MORE,
        NORMAL
    }
}
