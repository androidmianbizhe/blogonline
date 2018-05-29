package com.example.lihao.blogeronline.data.db;

import com.example.lihao.blogeronline.BuildConfig;
import com.example.lihao.blogeronline.utils.UIUtils;
import com.litesuits.orm.LiteOrm;

/**
 * Created by lihao on 17-11-27.
 */

public class LiteOrmHelper {

    private static final String DB_NAME = "bloger_onlie.db";

    private static volatile LiteOrm sInstance;

    private LiteOrmHelper(){

    }

    public static LiteOrm getInstance(){

        if(sInstance == null){

            synchronized (LiteOrmHelper.class){

                if(sInstance == null){

                    sInstance = LiteOrm.newCascadeInstance(
                            UIUtils.getContext(),DB_NAME);

                    sInstance.setDebugged(BuildConfig.DEBUG);
                }
            }
        }

        return sInstance;
    }

}
