
package com.quootta.mdate.utils;

import android.support.v4.util.ArrayMap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 字符串为null和""都返回true
     * 
     * @param text
     * @return
     */
    public static boolean isEmpty(CharSequence text) {
        boolean empty = false;
        if (text == null || text.toString().trim().length() == 0) {
            empty = true;
        }
        return empty;
    }

    public static boolean isNotEmpty(CharSequence text) {
        return !isEmpty(text);
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     * 
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 回车、换行符
     * 
     * @param str
     * @return
     */
    public static String replaceBlankToSpace(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll(" ");
        }
        return dest;
    }
    
    /**
     * 设置字符串中某些字的颜色
     * @param text   字符串
     * @param color  要改变的颜色
     * @param begin  开始位置
     * @param end    结束位置
     * @return
     */
    public static SpannableStringBuilder SetSpannableTextColor(String text , int color, int begin, int end ){
    	  SpannableStringBuilder style = new SpannableStringBuilder(text);
          style.setSpan(new ForegroundColorSpan(color), begin, end,
                  Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //
          return style;
    }

    /**
     * 将url的参数值解析保存进ArrayMap里
     *
     * @param url
     * @return
     */
    public static ArrayMap<String, String> getUrlParams(String url) {
        ArrayMap<String, String> urlParams = new ArrayMap<String, String>();
        try {
            if (url.contains("?")) {
                String paramsStr = url.split("\\?")[1];
                if (paramsStr.contains("&")) {
                    String[] params = paramsStr.split("&");

                    for (int i = 0; i < params.length; i++) {
                        String p = params[i];
                        String[] param = p.split("=");
                        urlParams.put(param[0], param[1]);
                    }

                } else if (StringUtil.isNotEmpty(paramsStr)) {
                    String[] param = paramsStr.split("=");
                    urlParams.put(param[0], param[1]);
                }

            }

        } catch (Exception e) {
            Log.e("StringUtil", "解析参数出错，请检查你的url格式是否正确！！！");
        }

        return urlParams;
    }

}
