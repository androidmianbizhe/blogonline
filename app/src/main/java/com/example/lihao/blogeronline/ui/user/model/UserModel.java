package com.example.lihao.blogeronline.ui.user.model;

import com.example.lihao.blogeronline.bean.DealCode;
import com.example.lihao.blogeronline.bean.DetailUserInfo;
import com.example.lihao.blogeronline.bean.SimpleUserInfo;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lihao on 17-11-1.
 */

public interface UserModel {


    Observable<SimpleUserInfo> getSimpleUserInfo(long uid);

    Observable<DetailUserInfo> getDetailUserInfo(long uid, long request);

    Observable<DealCode> uploadUserIcon(Map<String, RequestBody> params);

    Observable<DealCode> updateUserNickname(long uid,String nickname);

    Observable<DealCode> updateUserTag(long uid,String tag);

    Observable<DealCode> updateUserSex(long uid,String sex);

    Observable<DealCode> updateUserBirth(long uid,long birth);


}
