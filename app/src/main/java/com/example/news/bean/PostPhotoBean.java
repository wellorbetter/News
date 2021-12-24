package com.example.news.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PostPhotoBean {

    private int code;
    private String msg;
    private DataDTO data;

    public static PostPhotoBean objectFromData(String str) {

        return new Gson().fromJson(str, PostPhotoBean.class);
    }

    public static PostPhotoBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), PostPhotoBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<PostPhotoBean> arrayPostPhotoBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<PostPhotoBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<PostPhotoBean> arrayPostPhotoBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<PostPhotoBean>>() {
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
        private String img_url;
        private int img_id;

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

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getImg_id() {
            return img_id;
        }

        public void setImg_id(int img_id) {
            this.img_id = img_id;
        }
    }
}
