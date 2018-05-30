package com.example.lihao.blogeronline.ui.common.model.imple;

import android.util.Log;

import com.example.lihao.blogeronline.app.Constants;
import com.example.lihao.blogeronline.app.UserManager;
import com.example.lihao.blogeronline.bean.Article;
import com.example.lihao.blogeronline.bean.HtmlParseReg;
import com.example.lihao.blogeronline.bean.UserPrefer;
import com.example.lihao.blogeronline.http.Api;
import com.example.lihao.blogeronline.http.MyApi;
import com.example.lihao.blogeronline.http.MyNetwork;
import com.example.lihao.blogeronline.http.NetWork;
import com.example.lihao.blogeronline.ui.common.model.BaseModel;
import com.example.lihao.blogeronline.ui.user.LoginActivity;
import com.example.lihao.blogeronline.utils.SPUtils;
import com.example.lihao.blogeronline.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by lihao on 17-11-21.
 */

public class BaseModelImple implements BaseModel {


    @Override
    public Observable<ArrayList<Article>> getRecInfoFromPage(int page,int from_id) {
        MyApi api = MyNetwork.getInstance().getApi(UIUtils.getContext());

        UserManager userManager = UserManager.create(UIUtils.getContext());

        return api.getRecInfoAtPage(
                userManager.getUid(),userManager.getToken(),page,from_id)
                .compose(MyNetwork.schedulersTransformer);
    }

    @Override
    public Observable<ArrayList<Article>> getInfoFromPages(String url, int page) {

        String host = SPUtils.getSharedStringData(UIUtils.getContext(),"base_url", Constants.CSDN_URL);
        Api api = NetWork.getInstance().getApiByHost(UIUtils.getContext(),host);

        return api.getInfoAtPage(url,page).map(new Function<String, ArrayList<Article>>() {
            @Override
            public ArrayList<Article> apply(String s) throws Exception {

                return dealhtml(s);
            }
        }).compose(NetWork.schedulersTransformer);
    }

    //解析数据
    public ArrayList<Article> dealhtml(String result){

        String regx = SPUtils.getSharedStringData(UIUtils.getContext(), "regx", "");
        Gson gson = new Gson();
        HtmlParseReg htmlParseReg = gson.fromJson(regx, new TypeToken<HtmlParseReg>() {}.getType());
        if(htmlParseReg == null){
            return null;
        }
        String username = "";
        String title = "";
        String link = "";
        String time = "";
        ArrayList<Article> appInfolist = new ArrayList<Article>();

        //解析html
        Document doc = Jsoup.parse(result);
        Elements element = doc.getElementsByClass(htmlParseReg.getList_classname());

        for(int i = 0; i < element.size();i++) {

            Article article = new Article();

            Element blog = element.get(i);

            if(htmlParseReg.getUsername_classname().equals("")){
                username = blog.getElementsByClass(htmlParseReg.getUsername_parent_classname())
                        .get(htmlParseReg.getUsername_parent_index())
                        .child(htmlParseReg.getUsername_index()).text();
            }else {
                username = blog.getElementsByClass(htmlParseReg.getUsername_classname()).text();
            }
            if(htmlParseReg.getTitle_classname().equals("")){
                title = blog.getElementsByClass(htmlParseReg.getTitle_parent_classname())
                        .get(htmlParseReg.getTitle_parent_index())
                        .child(htmlParseReg.getTitle_index()).text();
            }else {
                title = blog.getElementsByClass(htmlParseReg.getTitle_classname()).text();
            }
            if(htmlParseReg.getLink_classname().equals("")){
                link = blog.getElementsByClass(htmlParseReg.getLink_parent_classname())
                        .get(htmlParseReg.getLink_parent_index())
                        .child(htmlParseReg.getLink_index())
                        .attr("href");
            }else {
                link = blog.getElementsByClass(htmlParseReg.getLink_classname()).attr("href");
            }
            if(htmlParseReg.getTime_classname().equals("")){
                time = blog.getElementsByClass(htmlParseReg.getTime_parent_classname())
                        .get(htmlParseReg.getTime_parent_index())
                        .child(htmlParseReg.getTime_index()).text();
            }else {
                time = blog.getElementsByClass(htmlParseReg.getTime_classname()).text();
            }

            article.setTitle(title);
            article.setLink(link);
            article.setTime(time);
            article.setUsername(username);

            //添加到集合中
            appInfolist.add(article);
        }

        return appInfolist;
    }

}
