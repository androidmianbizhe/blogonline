package com.example.lihao.blogeronline.bean;

/**
 * Created by lihao on 17-10-22.
 */

public class SimpleUserInfo {


    /**
     * returnCode : 1
     * user : {"uid":123456,"nickname":"时间之外","u_icon":"public/static/uploads/user.png","rank_level":1,"user_type":5,"user_type_icon":"public/static/uploads/type_icon_default.png","content":"愿你如阳光，明媚不忧伤"}
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
         * rank_level : 1
         * user_type : 5
         * user_type_icon : public/static/uploads/type_icon_default.png
         * content : 愿你如阳光，明媚不忧伤
         */

        private int uid;
        private String nickname;
        private String u_icon;
        private int rank_level;
        private int user_type;
        private String user_type_icon;
        private String content;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
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

        public String getUser_type_icon() {
            return user_type_icon;
        }

        public void setUser_type_icon(String user_type_icon) {
            this.user_type_icon = user_type_icon;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
