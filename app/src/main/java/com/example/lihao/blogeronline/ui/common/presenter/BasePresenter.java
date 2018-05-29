package com.example.lihao.blogeronline.ui.common.presenter;

/**
 * Created by lihao on 17-11-21.
 */

public interface BasePresenter {

    void getDataFromPage(String url,int page);

    void getRecDataFromPage(final int page,final int from_id);

    void getRefresh(boolean isRecom,String url,int from_id);

}
