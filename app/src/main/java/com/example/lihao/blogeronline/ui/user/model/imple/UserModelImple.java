package com.example.lihao.blogeronline.ui.user.model.imple;

import com.example.lihao.blogeronline.app.UserManager;
import com.example.lihao.blogeronline.bean.DealCode;
import com.example.lihao.blogeronline.bean.DetailUserInfo;
import com.example.lihao.blogeronline.bean.SimpleUserInfo;
import com.example.lihao.blogeronline.http.MyApi;
import com.example.lihao.blogeronline.http.MyNetwork;
import com.example.lihao.blogeronline.ui.user.model.UserModel;
import com.example.lihao.blogeronline.utils.UIUtils;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by lihao on 17-11-1.
 */

public class UserModelImple implements UserModel {

    @Override
    public Observable<SimpleUserInfo> getSimpleUserInfo(long uid) {
        MyApi myApi = MyNetwork.getInstance().getApi(UIUtils.getContext());

        String token = UserManager.create(UIUtils.getContext()).getToken();

        return myApi.getSimpleUserInfo(uid,token).compose(MyNetwork.schedulersTransformer);
    }

    @Override
    public Observable<DetailUserInfo> getDetailUserInfo(long uid, long requestid) {
        MyApi api = MyNetwork.getInstance().getApi(UIUtils.getContext());

        return api.getDetailUserInfo(uid,requestid,UserManager.create(UIUtils.getContext()).getToken())
                .compose(MyNetwork.schedulersTransformer);
    }

    @Override
    public Observable<DealCode> uploadUserIcon(Map<String,RequestBody> params) {

        MyApi myApi = MyNetwork.getInstance().getApi(UIUtils.getContext());

        return myApi.uploadUserIcon(params).compose(MyNetwork.schedulersTransformer);
    }

    @Override
    public Observable<DealCode> updateUserNickname(long uid, String nickname) {
        MyApi myApi = MyNetwork.getInstance().getApi(UIUtils.getContext());
        String token = UserManager.create(UIUtils.getContext()).getToken();
        return myApi.updateUserNickname(uid,nickname,token).compose(MyNetwork.schedulersTransformer);
    }

    @Override
    public Observable<DealCode> updateUserTag(long uid, String tag) {
        MyApi myApi = MyNetwork.getInstance().getApi(UIUtils.getContext());
        String token = UserManager.create(UIUtils.getContext()).getToken();
        return myApi.updateUserTag(uid,tag,token).compose(MyNetwork.schedulersTransformer);
    }

    @Override
    public Observable<DealCode> updateUserSex(long uid, String sex) {
        MyApi myApi = MyNetwork.getInstance().getApi(UIUtils.getContext());
        String token = UserManager.create(UIUtils.getContext()).getToken();
        return myApi.updateUserSex(uid,sex,token).compose(MyNetwork.schedulersTransformer);
    }

    @Override
    public Observable<DealCode> updateUserBirth(long uid, long birth) {
        MyApi myApi = MyNetwork.getInstance().getApi(UIUtils.getContext());
        String token = UserManager.create(UIUtils.getContext()).getToken();
        return myApi.updateUserBirth(uid,birth, token).compose(MyNetwork.schedulersTransformer);
    }
}
