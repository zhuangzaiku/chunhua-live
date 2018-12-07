package com.quootta.mdate.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.quootta.mdate.base.BaseApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float, Long类型的参数
 * 同样调用getParam就能获取到保存在手机里面的数据
 */
public class DBUtil {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "user_Config";
    public static final String LOCATION_DATE = "location_date";

    private static SharedPreferences mSharePres = BaseApp.getApplication().getSharedPreferences(FILE_NAME,
                                                                                      Context.MODE_PRIVATE);

//	private DBUtil() {
//		mSharePres = MyApplication.getApplicationInstance().getSharedPreferences(FILE_NAME,
//				Context.MODE_PRIVATE);
//	}
//
//	public synchronized static DBUtil getInstance() {
//		if (prefUtil == null) {
//			prefUtil = new DBUtil();
//		}
//		return prefUtil;
//	}

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static void setParam(String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor editor = mSharePres.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.apply();
    }




    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        if ("String".equals(type)) {
            return mSharePres.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return mSharePres.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return mSharePres.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return mSharePres.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return mSharePres.getLong(key, (Long) defaultObject);
        }

        return null;
    }


    public static void saveBeanToPrefences(String key, Object bean) {
        if (bean != null) {
            ByteArrayOutputStream baos = null;
            ObjectOutputStream oos = null;
            try {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(bean);
                String personBase64 = new String(Base64.encode(baos
                        .toByteArray()));
                SharedPreferences.Editor editor = mSharePres.edit();
                editor.putString(key, personBase64);
                editor.apply();

                LogUtil.i("DBUtil", "save pre key == " + key + " || bean == "
                        + bean.getClass().getSimpleName());
            } catch (IOException e) {
                LogUtil.e("DBUtil", e.toString());
            } finally {
                FileUtil.closeQuietly(oos);
                FileUtil.closeQuietly(baos);
            }
        } else {
            LogUtil.i("DBUtil", "save pre key == " + key + " || bean == null");
            SharedPreferences.Editor editor = mSharePres.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public static Object getBeanFromPrefences(String key) {
        String personBase64 = mSharePres.getString(key, "");
        Object bean = null;
        if (StringUtil.isNotEmpty(personBase64)) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                byte[] base64Bytes = Base64.decode(personBase64);
                bais = new ByteArrayInputStream(base64Bytes);
                ois = new ObjectInputStream(bais);
                bean = ois.readObject();
            } catch (Exception e) {
                LogUtil.e("DBUtil", e.toString());
            } finally {
                FileUtil.closeQuietly(ois);
                FileUtil.closeQuietly(bais);
            }
        }
        if (bean == null) {
            LogUtil.i("DBUtil", "get pre key == " + key + " || bean == null");
        } else {
            LogUtil.i("DBUtil", "get pre key == " + key + " || bean == "
                    + bean.getClass().getSimpleName());
        }
        return bean;
    }

    public static void clearData() {
        mSharePres.edit().clear().commit();
    }
}
