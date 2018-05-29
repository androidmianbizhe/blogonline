package com.example.lihao.blogeronline.ui.common.presenter.imple;

import android.content.Context;

import com.example.lihao.blogeronline.bean.Article;
import com.example.lihao.blogeronline.ui.common.model.BaseModel;
import com.example.lihao.blogeronline.ui.common.model.imple.BaseModelImple;
import com.example.lihao.blogeronline.ui.common.presenter.BasePresenter;
import com.example.lihao.blogeronline.ui.common.view.MyBaseView;
import com.example.lihao.blogeronline.utils.NetworkUtil;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lihao on 17-11-21.
 */

public class BasePresenterImple implements BasePresenter {

    private Context context;

    private BaseModel baseModel;

    private MyBaseView<BasePresenter> baseView;

    public BasePresenterImple(Context context, MyBaseView<BasePresenter> baseView){
        this.context = context;
        this.baseView = baseView;
        this.baseModel = createModelImple();

        this.baseView.setPresenter(this);
    }

    public BaseModel createModelImple(){

        return new BaseModelImple();
    }

    @Override
    public void getDataFromPage(final String url,final int page) {

        Observable<ArrayList<Article>> infoFromPage = baseModel.getInfoFromPages(url,page);
        infoFromPage.subscribe(new Observer<ArrayList<Article>>() {
            @Override
            public void onSubscribe(Disposable d) {
                if(page == 1){
                    baseView.loadingView();
                }else {
                    baseView.startLoadMore();
                }
            }

            @Override
            public void onNext(ArrayList<Article> s) {

                if(s != null && !s.isEmpty()){
                    baseView.infoLoaded(s);

                } else {
                    baseView.emptyView();
                }
            }

            @Override
            public void onError(Throwable e) {

                boolean available = NetworkUtil.isNetworkAvailable(context);
                if(!available){
                    baseView.interNetException();
                }else {

                    baseView.handError(e);
                }

            }

            @Override
            public void onComplete() {
            }
        });

    }

    @Override
    public void getRecDataFromPage(final int page,final int from_id) {
        //推荐
        Observable<ArrayList<Article>> infoFromPage = baseModel.getRecInfoFromPage(page,from_id);
        infoFromPage.subscribe(new Observer<ArrayList<Article>>() {
            @Override
            public void onSubscribe(Disposable d) {
                if(page == 1){
                    baseView.loadingView();
                }else {
                    baseView.startLoadMore();
                }
            }

            @Override
            public void onNext(ArrayList<Article> s) {

                if(s != null && !s.isEmpty()){
                    baseView.infoLoaded(s);

                } else {
                    baseView.emptyView();
                }
            }

            @Override
            public void onError(Throwable e) {

                boolean available = NetworkUtil.isNetworkAvailable(context);
                if(!available){
                    baseView.interNetException();
                }else {

                    baseView.handError(e);
                }

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getRefresh(boolean isRecom,String url,int from_id) {

        Observable<ArrayList<Article>> infoFromPage = null;
        if(isRecom){

            infoFromPage = baseModel.getRecInfoFromPage(1,from_id);

        }else {

            infoFromPage = baseModel.getInfoFromPages(url,1);
        }

        infoFromPage.subscribe(new Observer<ArrayList<Article>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ArrayList<Article> s) {

                if(s != null){
                    baseView.refreshed(s);
                } else {
                    baseView.emptyView();
                }
            }

            @Override
            public void onError(Throwable e) {
                boolean available = NetworkUtil.isNetworkAvailable(context);
                if(!available){
                    baseView.interNetException();
                }else {

                    baseView.handError(e);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
