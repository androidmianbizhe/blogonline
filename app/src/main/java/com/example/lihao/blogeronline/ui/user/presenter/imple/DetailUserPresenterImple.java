package com.example.lihao.blogeronline.ui.user.presenter.imple;

import android.content.Context;

import com.example.lihao.blogeronline.bean.DetailUserInfo;
import com.example.lihao.blogeronline.ui.user.model.UserModel;
import com.example.lihao.blogeronline.ui.user.model.imple.UserModelImple;
import com.example.lihao.blogeronline.ui.user.presenter.DetailUserPresenter;
import com.example.lihao.blogeronline.ui.user.view.DetailUserView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lihao on 17-11-1.
 */

public class DetailUserPresenterImple implements DetailUserPresenter {

    private Context context;

    private UserModel userModel;

    private DetailUserView detailUserView;

    public DetailUserPresenterImple(Context context, DetailUserView detailUserView){
        this.context = context;
        this.detailUserView = detailUserView;

        this.detailUserView.setPresenter(this);

        this.userModel = new UserModelImple();
    }

    @Override
    public void getDetailUserInfo(long uid, long requestUid) {

        Observable<DetailUserInfo> observable = userModel.getDetailUserInfo(uid, requestUid);
        observable.subscribe(new Observer<DetailUserInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DetailUserInfo detailUserInfo) {
                if(detailUserInfo.getReturnCode() == 1){
                    detailUserView.detailUserInfoLoaded(detailUserInfo);
                } else if(detailUserInfo.getReturnCode() == 0){
                    detailUserView.handleError(new Throwable("uid空的"));
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
