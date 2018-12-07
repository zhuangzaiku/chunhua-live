package com.quootta.mdate.ui.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Ryon on 2016/11/4/0004.
 */
@MessageTag(value = "presentMsg",flag =MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class RongGiftMessage extends MessageContent {




    private String charamScore;
    private String goldNum;
    private String imageName;


    private String giftName;

    private String content;

    public RongGiftMessage(){

    }

    public static RongGiftMessage obtain(String giftName, String gift_charm,String gift_gold,String gift_image,String content){
        RongGiftMessage rongGiftMessage =new RongGiftMessage();

        rongGiftMessage.charamScore=gift_charm;
        rongGiftMessage.goldNum=gift_gold;
        rongGiftMessage.imageName=gift_image;
        rongGiftMessage.content=content;
        rongGiftMessage.giftName=giftName;

        return rongGiftMessage;
    }

    //给消息赋值
    public RongGiftMessage(byte[] data){


        try {
            String jsonStr= null;
            try {
                jsonStr = new String(data,"UTF-8");

                JSONObject jsonObject=new JSONObject(jsonStr);
                setContent(jsonObject.getString("content"));
                setCharamScore(jsonObject.getString("charamScore"));
                setGoldNum(jsonObject.getString("goldNum"));
                setImageName(jsonObject.getString("imageName"));
                setGiftName(jsonObject.getString("giftName"));


                if (jsonObject.has("user")) {
                    setUserInfo(parseJsonToUserInfo(jsonObject.getJSONObject("user")));
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 构造函数。
     *
     * @param in 初始化传入的 Parcel。
     */
    public RongGiftMessage(Parcel in) {

        setCharamScore(ParcelUtils.readFromParcel(in));
        setGoldNum(ParcelUtils.readFromParcel(in));
        setImageName(ParcelUtils.readFromParcel(in));
        setContent(ParcelUtils.readFromParcel(in));
        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));
        setGiftName(ParcelUtils.readFromParcel(in));

    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<RongGiftMessage> CREATOR = new Creator<RongGiftMessage>() {

        @Override
        public RongGiftMessage createFromParcel(Parcel source) {
            return new RongGiftMessage(source);
        }

        @Override
        public RongGiftMessage[] newArray(int size) {
            return new RongGiftMessage[size];
        }
    };


    /**
     * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
     *
     * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     *
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        ParcelUtils.writeToParcel(dest,charamScore);
        ParcelUtils.writeToParcel(dest,goldNum);
        ParcelUtils.writeToParcel(dest,imageName);
        ParcelUtils.writeToParcel(dest,content);
        ParcelUtils.writeToParcel(dest,getUserInfo());
        ParcelUtils.writeToParcel(dest,giftName);
    }


    /**
     * 将消息属性封装成 json 串，再将 json 串转成 byte 数组，该方法会在发消息时调用
     */
    @Override
    public byte[] encode() {

        JSONObject jsonObject=new JSONObject();

        try {

            jsonObject.put("charamScore",charamScore);
            jsonObject.put("goldNum",goldNum);
            jsonObject.put("imageName",imageName);
            jsonObject.put("content",content);
            jsonObject.put("giftName",giftName);
            if (getJSONUserInfo()!=null){
                jsonObject.putOpt("user",getJSONUserInfo());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            return jsonObject.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;

    }

    public String getCharamScore() {
        return charamScore;
    }

    public void setCharamScore(String charamScore) {
        this.charamScore = charamScore;
    }

    public String getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(String goldNum) {
        this.goldNum = goldNum;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

}
