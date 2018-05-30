package com.example.lihao.blogeronline.ui.common.view;

import com.example.lihao.blogeronline.base.BaseView;
import com.example.lihao.blogeronline.bean.Article;
import com.example.lihao.blogeronline.ui.common.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by lihao on 17-11-21.
 */

public interface MyBaseView<C extends BasePresenter> extends BaseView <BasePresenter>{

    void startLoadMore();

    void infoLoaded(ArrayList<Article> s);

    void loadMoreFinished();

    void refreshed(ArrayList<Article> s);

    void handError(Throwable e);

    void emptyView();

    void interNetException();

    void loadingView();

}
