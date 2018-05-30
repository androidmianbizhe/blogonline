package com.example.lihao.blogeronline.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.lihao.blogeronline.adapter.MyBasePageAdapter;

/**
 * Created by lihao on 17-11-22.
 */

public class LoadMoreRecyclerView extends RecyclerView {


    private boolean isLoading = false;

    private int last;

    OnLoadMoreListener listener;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == RecyclerView.SCROLL_STATE_IDLE){

                    MyBasePageAdapter adapter = (MyBasePageAdapter)recyclerView.getAdapter();

                    if(last + 1 == adapter.getItemCount() && !isLoading) {
                        isLoading = true;
                        //加载
                        if(listener != null){
                            listener.onLoad();
                        }
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                last = layoutManager.findLastVisibleItemPosition();
            }
        });



    }


    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        this.listener = listener;
    }


    public interface OnLoadMoreListener {
        void onLoad();
    }


}
