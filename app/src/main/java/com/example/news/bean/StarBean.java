package com.example.news.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StarBean {

    private int code;
    private String msg;
    private DataDTO data;

    public static StarBean objectFromData(String str) {

        return new Gson().fromJson(str, StarBean.class);
    }

    public static StarBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), StarBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<StarBean> arrayStarBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<StarBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<StarBean> arrayStarBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<StarBean>>() {
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
        private int isStar;

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

        public int getIsStar() {
            return isStar;
        }

        public void setIsStar(int isStar) {
            this.isStar = isStar;
        }
    }
}
