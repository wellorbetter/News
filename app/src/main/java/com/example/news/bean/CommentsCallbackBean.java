package com.example.news.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CommentsCallbackBean {
    private int code;
    private String msg;

    public static CommentsCallbackBean objectFromData(String str) {

        return new Gson().fromJson(str, CommentsCallbackBean.class);
    }

    public static CommentsCallbackBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), CommentsCallbackBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<CommentsCallbackBean> arrayCommentsCallbackBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<CommentsCallbackBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<CommentsCallbackBean> arrayCommentsCallbackBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<CommentsCallbackBean>>() {
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
}
