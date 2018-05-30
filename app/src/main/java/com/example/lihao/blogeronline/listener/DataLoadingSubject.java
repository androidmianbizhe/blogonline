package com.example.lihao.blogeronline.listener;

/**
 * Created by lihao on 17-11-22.
 */

public interface DataLoadingSubject {


    boolean isDataLoading();

    void registerListener(DataLoadingListener listener);

    void unregistereListener(DataLoadingListener listener);

    interface DataLoadingListener {

        void dataStartedLoading();

        void dataFinishedLoading();
    }

}
