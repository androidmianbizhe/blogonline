package com.example.lihao.blogeronline.http;

import com.example.lihao.blogeronline.bean.Article;
import com.example.lihao.blogeronline.bean.ArticleType;
import com.example.lihao.blogeronline.bean.DealCode;
import com.example.lihao.blogeronline.bean.DetailUserInfo;
import com.example.lihao.blogeronline.bean.From;
import com.example.lihao.blogeronline.bean.HtmlParseReg;
import com.example.lihao.blogeronline.bean.LoginInfo;
import com.example.lihao.blogeronline.bean.SimpleUserInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by lihao on 17-11-14.
 */

public interface MyApi {


    @POST("login/login")
    Observable<LoginInfo> login(@Query("username") long user,
                                @Query("password") String passwd);

    @POST("login/register")
    Observable<LoginInfo> register(@Query("nickname") String nickname,
                                @Query("openID") String openId,
                                @Query("loginWay") int loginWay,
                                @Query("phone") String phone);

    @GET("user/getSimpleInfoOfUser")
    Observable<SimpleUserInfo> getSimpleUserInfo(@Query("uid") long uid,
                                                 @Query("token")String token);


    @GET("user/getDetailInfoOfUser")
    Observable<DetailUserInfo> getDetailUserInfo(@Query("uid") long uid,
                                                 @Query("requestUid") long requestUid,
                                                 @Query("token")String token);

    @Multipart
    @POST("user/updateUserIcon")
    Observable<DealCode> uploadUserIcon (@PartMap Map<String,RequestBody> params);


    @GET("user/updateUserNickname")
    Observable<DealCode> updateUserNickname(@Query("uid") long uid,
                                            @Query("nickname") String nickname,
                                            @Query("token")String token);

    @GET("user/addUserTag")
    Observable<DealCode> updateUserTag(@Query("uid") long uid,
                                       @Query("tag") String tag,
                                       @Query("token")String token);

    @GET("user/updateUserSex")
    Observable<DealCode> updateUserSex(@Query("uid") long uid,
                                       @Query("sex") String sex,
                                       @Query("token")String token);

    @GET("user/updateUserBirth")
    Observable<DealCode> updateUserBirth(@Query("uid") long uid,
                                         @Query("birth") long birth,
                                         @Query("token")String token);

    @Multipart
    @POST("user/uploadBlog")
    Observable<DealCode> uploadBlogToServer(@PartMap Map<String,RequestBody> params);


    @Multipart
    @POST("user/uploadPrefer")
    Observable<DealCode> uploadPreferToServer(@PartMap Map<String,RequestBody> params);

    @GET("user/getBlog")
    Observable<List<Article>> getRecInfoAtPage(@Query("uid") long uid,
                                               @Query("token")String token,
                                               @Query("page") int page,
                                               @Query("from_id") int from_id);

    @Multipart
    @POST("Login/registerByCell")
    Observable<DealCode> registerAcount(@PartMap Map<String,RequestBody> params);

    @POST("Login/checkCode")
    Observable<DealCode> checkCodeByPhone(@Query("phone") String cell,
                                          @Query("code")String code);

    @POST("Login/settingPwd")
    Observable<LoginInfo> setPasswd(@Query("phone") long user,
                                    @Query("password") String passwd);

    @GET("Login/getAllFrom")
    Observable<List<From>> getFroms();

    @GET("Login/getTypeByFromId")
    Observable<ArticleType> getTypeByFromId(@Query("from_id") int from_id,
                                                  @Query("size") int size);

    @GET("Login/getArticleRegx")
    Observable<HtmlParseReg> getRegxById(@Query("from_id") int from_id);

}
