package com.example.lihao.blogeronline.ui.user.view;


import com.example.lihao.blogeronline.base.BaseView;
import com.example.lihao.blogeronline.bean.DetailUserInfo;
import com.example.lihao.blogeronline.ui.user.presenter.DetailUserPresenter;

/**
 * Created by lihao on 17-11-1.
 */

public interface DetailUserView extends BaseView<DetailUserPresenter> {

    void detailUserInfoLoaded(DetailUserInfo info);

    void handleError(Throwable throwable);

}
