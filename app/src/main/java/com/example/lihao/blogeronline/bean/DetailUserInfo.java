package com.example.lihao.blogeronline.bean;

/**
 * Created by lihao on 17-11-2.
 */

public class DetailUserInfo {


    /**
     * returnCode : 1
     * user : {"uid":123456,"nickname":"时间之外","u_icon":"public/static/uploads/user.png","u_age":0,"rank_level":1,"user_type":5,"create_time":20171005,"address":"123","job":123,"sex":"男","visitorSum":0,"user_type_icon":"public/static/uploads/type_icon_default.png","user_type_name":"普通会员","publish_time":"2017-11-03 00:00:00","content":"愿你如阳光，明媚不忧伤","face":1}
     */

    private int returnCode;
    private UserBean user;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * uid : 123456
         * nickname : 时间之外
         * u_icon : public/static/uploads/user.png
         * u_age : 0
         * rank_level : 1
         * user_type : 5
         * create_time : 20171005
         * address : 123
         * job : 123
         * sex : 男
         * visitorSum : 0
         * user_type_icon : public/static/uploads/type_icon_default.png
         * user_type_name : 普通会员
         * publish_time : 2017-11-03 00:00:00
         * content : 愿你如阳光，明媚不忧伤
         * face : 1
         */

        private long uid;
        private String nickname;
        private String u_icon;
        private int u_age;
        private int rank_level;
        private int user_type;
        private long create_time;
        private String address;
        private int job;
        private String sex;
        private int visitorSum;
        private String user_type_icon;
        private String user_type_name;
        private String publish_time;
        private String content;
        private int face;

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getU_icon() {
            return u_icon;
        }

        public void setU_icon(String u_icon) {
            this.u_icon = u_icon;
        }

        public int getU_age() {
            return u_age;
        }

        public void setU_age(int u_age) {
            this.u_age = u_age;
        }

        public int getRank_level() {
            return rank_level;
        }

        public void setRank_level(int rank_level) {
            this.rank_level = rank_level;
        }

        public int getUser_type() {
            return user_type;
        }

        public void setUser_type(int user_type) {
            this.user_type = user_type;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getJob() {
            return job;
        }

        public void setJob(int job) {
            this.job = job;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getVisitorSum() {
            return visitorSum;
        }

        public void setVisitorSum(int visitorSum) {
            this.visitorSum = visitorSum;
        }

        public String getUser_type_icon() {
            return user_type_icon;
        }

        public void setUser_type_icon(String user_type_icon) {
            this.user_type_icon = user_type_icon;
        }

        public String getUser_type_name() {
            return user_type_name;
        }

        public void setUser_type_name(String user_type_name) {
            this.user_type_name = user_type_name;
        }

        public String getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(String publish_time) {
            this.publish_time = publish_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getFace() {
            return face;
        }

        public void setFace(int face) {
            this.face = face;
        }
    }
}
