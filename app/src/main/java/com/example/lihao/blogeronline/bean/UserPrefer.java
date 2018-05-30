package com.example.lihao.blogeronline.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihao on 17-12-5.
 */

public class UserPrefer {

    private long uid;

    private List<Prefer> prefer;

    public UserPrefer(){
        prefer = new ArrayList<>();
    }

    public static class Prefer {

        int type_id;

        int viewCount;

        int from_id;

        public Prefer(int id,int count,int from_id){
            this.type_id = id;
            this.viewCount = count;
            this.from_id = from_id;
        }

        public Prefer(){

        }

        public int getFrom_id() {
            return from_id;
        }

        public void setFrom_id(int from_id) {
            this.from_id = from_id;
        }

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public List<Prefer> getPrefer() {
        return prefer;
    }

    public void setPrefer(List<Prefer> prefer) {
        this.prefer = prefer;
    }
}
