package com.example.news.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class History_News_Bean {

    private int code;
    private String msg;
    private DataDTO data;

    public static History_News_Bean objectFromData(String str) {

        return new Gson().fromJson(str, History_News_Bean.class);
    }

    public static History_News_Bean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), History_News_Bean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<History_News_Bean> arrayHistory_News_BeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<History_News_Bean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<History_News_Bean> arrayHistory_News_BeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<History_News_Bean>>() {
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
        private List<NewsDTO> news;

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

        public List<NewsDTO> getNews() {
            return news;
        }

        public void setNews(List<NewsDTO> news) {
            this.news = news;
        }

        public static class NewsDTO {
            private String time;
            private String title;
            private String avatar;
            private String avatar_90x90;
            private String nickname;
            private int news_id;

            public static NewsDTO objectFromData(String str) {

                return new Gson().fromJson(str, NewsDTO.class);
            }

            public static NewsDTO objectFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    return new Gson().fromJson(jsonObject.getString(str), NewsDTO.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public static List<NewsDTO> arrayNewsDTOFromData(String str) {

                Type listType = new TypeToken<ArrayList<NewsDTO>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }

            public static List<NewsDTO> arrayNewsDTOFromData(String str, String key) {

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Type listType = new TypeToken<ArrayList<NewsDTO>>() {
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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getNews_id() {
                return news_id;
            }

            public void setNews_id(int news_id) {
                this.news_id = news_id;
            }
        }
    }
}
