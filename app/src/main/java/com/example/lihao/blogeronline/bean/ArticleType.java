package com.example.lihao.blogeronline.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */

public class ArticleType {


    /**
     * article_type : [{"type_id":10,"article_type_name":"移动应用","type_url":"https://blog.csdn.net/mobile/newarticle.html","from_id":1},{"type_id":11,"article_type_name":"架构","type_url":"https://blog.csdn.net/enterprise/newarticle.html","from_id":1}]
     * f_page_max_item : 30
     */

    private int f_page_max_item;

    private String article_body;

    private ArrayList<ArticleTypeBean> article_type;

    public int getF_page_max_item() {
        return f_page_max_item;
    }

    public void setF_page_max_item(int f_page_max_item) {
        this.f_page_max_item = f_page_max_item;
    }

    public ArrayList<ArticleTypeBean> getArticle_type() {
        return article_type;
    }

    public void setArticle_type(ArrayList<ArticleTypeBean> article_type) {
        this.article_type = article_type;
    }

    public String getArticle_body() {
        return article_body;
    }

    public void setArticle_body(String article_body) {
        this.article_body = article_body;
    }

    public static class ArticleTypeBean {
        /**
         * type_id : 10
         * article_type_name : 移动应用
         * type_url : https://blog.csdn.net/mobile/newarticle.html
         * from_id : 1
         */

        private int type_id;
        private String article_type_name;
        private String type_url;
        private int from_id;

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public String getArticle_type_name() {
            return article_type_name;
        }

        public void setArticle_type_name(String article_type_name) {
            this.article_type_name = article_type_name;
        }

        public String getType_url() {
            return type_url;
        }

        public void setType_url(String type_url) {
            this.type_url = type_url;
        }

        public int getFrom_id() {
            return from_id;
        }

        public void setFrom_id(int from_id) {
            this.from_id = from_id;
        }
    }
}
