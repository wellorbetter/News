package com.example.news.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class History_User_Bean {

    private int code;
    private String msg;
    private DataDTO data;

    public static History_User_Bean objectFromData(String str) {

        return new Gson().fromJson(str, History_User_Bean.class);
    }

    public static History_User_Bean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), History_User_Bean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<History_User_Bean> arrayHistory_BeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<History_User_Bean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<History_User_Bean> arrayHistory_BeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<History_User_Bean>>() {
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
        private List<UsersDTO> users;

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

        public List<UsersDTO> getUsers() {
            return users;
        }

        public void setUsers(List<UsersDTO> users) {
            this.users = users;
        }

        public static class UsersDTO {
            private String time;
            private String username;
            private String avatar;
            private String nickname;
            private int user_id;

            public static UsersDTO objectFromData(String str) {

                return new Gson().fromJson(str, UsersDTO.class);
            }

            public static UsersDTO objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), UsersDTO.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<UsersDTO> arrayUsersDTOFromData(String str) {

                Type listType = new TypeToken<ArrayList<UsersDTO>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<UsersDTO> arrayUsersDTOFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<UsersDTO>>() {
                    }.getType();

                    return new Gson().fromJson(jsonObject.getString(str), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ArrayList();


            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }
        }
    }
}
