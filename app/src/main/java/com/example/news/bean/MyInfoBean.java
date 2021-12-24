package com.example.news.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyInfoBean {

    private int code;
    private String msg;
    private DataDTO data;

    public static MyInfoBean objectFromData(String str) {

        return new Gson().fromJson(str, MyInfoBean.class);
    }

    public static MyInfoBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), MyInfoBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<MyInfoBean> arrayMyInfoBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<MyInfoBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<MyInfoBean> arrayMyInfoBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<MyInfoBean>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private int selfid;
        private String username;
        private String nickname;
        private String info;
        private String gender;
        private int like_num;
        private int star_num;
        private int follow_num;
        private int fans_num;
        private int status;
        private String avatar;
        private String avatar_90x90;

        public static DataDTO objectFromData(String str) {

            return new Gson().fromJson(str, DataDTO.class);
        }

        public static DataDTO objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), DataDTO.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<DataDTO> arrayDataDTOFromData(String str) {

            Type listType = new TypeToken<ArrayList<DataDTO>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<DataDTO> arrayDataDTOFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<DataDTO>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public int getSelfid() {
            return selfid;
        }

        public void setSelfid(int selfid) {
            this.selfid = selfid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public int getStar_num() {
            return star_num;
        }

        public void setStar_num(int star_num) {
            this.star_num = star_num;
        }

        public int getFollow_num() {
            return follow_num;
        }

        public void setFollow_num(int follow_num) {
            this.follow_num = follow_num;
        }

        public int getFans_num() {
            return fans_num;
        }

        public void setFans_num(int fans_num) {
            this.fans_num = fans_num;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAvatar_90x90() {
            return avatar_90x90;
        }

        public void setAvatar_90x90(String avatar_90x90) {
            this.avatar_90x90 = avatar_90x90;
        }
    }
}
