package com.quootta.mdate.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ryon on 2016/3/3.
 * email:para.ryon@foxmail.com
 */
public class GsonUtil {
    private static final Gson gson = new Gson();


    public static <T> T parse(String str,Type typeOfT) {
        if (str == null) return null;
        try {
            return gson.fromJson(str, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T parse(String str, Class<T> clazz) {
        if (str == null) return null;
        try {
            return gson.fromJson(str, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T parse(JSONObject json, Class<T> clazz) {
        if (json == null) return null;
        try {
            return parse(json.toString(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseString(Object obj) {
        if (obj == null) return "";
        return gson.toJson(obj);
    }

    public static <T> List<T> stringToList(String str, Class<T[]> clazz) {
        if (TextUtils.isEmpty(str)) return new ArrayList<>();
        return Arrays.asList(parse(str, clazz));
    }

    public static <T> List<List<T>> stringToListList(String str, Class<T[][]> clazz) {
        if (TextUtils.isEmpty(str)) return new ArrayList<>();
        T[][] tmpList = parse(str, clazz);
        List<List<T>> resultList = new ArrayList<>();
        for (T[] it : tmpList) {
            resultList.add(Arrays.asList(it));
        }
        return resultList;
    }

}
