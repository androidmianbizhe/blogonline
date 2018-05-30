package com.example.lihao.blogeronline.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by lihao on 17-11-27.
 */

@Table("uploadarticle")
public class UploadArticle implements Parcelable {

    @PrimaryKey(AssignType.BY_MYSELF)
    private String article_link = "";

    private int article_type_id = 0;

    private String article_auther = "";

    private String article_title = "";

    private int article_from_id = 1;


    public UploadArticle(){

    }

    protected UploadArticle(Parcel in) {

        article_link = in.readString();
        article_type_id = in.readInt();
        article_auther = in.readString();
        article_title = in.readString();
        article_from_id = in.readInt();
    }

    public static final Creator<UploadArticle> CREATOR = new Creator<UploadArticle>() {
        @Override
        public UploadArticle createFromParcel(Parcel in) {
            return new UploadArticle(in);
        }

        @Override
        public UploadArticle[] newArray(int size) {
            return new UploadArticle[size];
        }
    };

    public int getArticle_from_id() {
        return article_from_id;
    }

    public void setArticle_from_id(int article_from_id) {
        this.article_from_id = article_from_id;
    }

    public String getArticle_link() {
        return article_link;
    }

    public void setArticle_link(String article_link) {
        this.article_link = article_link;
    }

    public int getArticle_type_id() {
        return article_type_id;
    }

    public void setArticle_type_id(int article_type_id) {
        this.article_type_id = article_type_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getArticle_auther() {
        return article_auther;
    }

    public void setArticle_auther(String article_auther) {
        this.article_auther = article_auther;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }



    public void readFromParcel(Parcel in){

        this.article_link = in.readString();
        this.article_type_id = in.readInt();
        this.article_auther = in.readString();
        this.article_title = in.readString();
        this.article_from_id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(article_link);
        dest.writeInt(article_type_id);
        dest.writeString(article_auther);
        dest.writeString(article_title);
        dest.writeInt(article_from_id);
    }

}
