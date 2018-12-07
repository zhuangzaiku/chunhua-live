package com.quootta.mdate.provider;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.ui.message.RongGiftMessage;
import com.android.volley.toolbox.ImageLoader;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;

import static io.rong.imlib.model.Message.MessageDirection.SEND;


/**
 * Created by Ryon on 2016/11/4/0004.
 */
@ProviderTag(messageContent =RongGiftMessage.class, showPortrait = true, showProgress = true, centerInHorizontal = false)
public class RongGiftMessageProvider extends IContainerItemProvider.MessageProvider<RongGiftMessage> {
private ImageLoader imageLoader;

    @Override
    public void bindView(View view, int i, RongGiftMessage rongGiftMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();

        imageLoader= BaseApp.getImageLoader();

        if (uiMessage.getMessageDirection()==SEND) {
            // 消息方向，自己发送的  赠送礼物
            holder.gift_background.setBackgroundResource(R.mipmap.give_gift);
            holder.gift_give.setVisibility(View.VISIBLE);
            holder.gift_get.setVisibility(View.GONE);

//            holder.gift_charm_give.setText("土豪值");
            holder.gift_charm_give.setText(BaseApp.getApplication().getText(R.string.app_tips_text9));


            imageLoader.get(
                    LocalUrl.getPicUrl(rongGiftMessage.getImageName()),
                    imageLoader.getImageListener(holder.gift_give, R.mipmap.test, R.mipmap.test));

            holder.gift_charm.setText("+"+rongGiftMessage.getGoldNum());
            holder.gift_gold.setText("-"+rongGiftMessage.getGoldNum());

        }else {

            //消息方向，别人发送的  收到礼物
            holder.gift_background.setBackgroundResource(R.mipmap.get_gift);
            holder.gift_get.setVisibility(View.VISIBLE);
            holder.gift_give.setVisibility(View.GONE);

//            holder.gift_charm_give.setText("魅力值");
            holder.gift_charm_give.setText(BaseApp.getApplication().getText(R.string.app_tips_text10));


            imageLoader.get(
                    LocalUrl.getPicUrl(rongGiftMessage.getImageName()),
                    imageLoader.getImageListener(holder.gift_get, R.mipmap.test, R.mipmap.test));

            holder.gift_charm.setText("+"+rongGiftMessage.getCharamScore());
            holder.gift_gold.setText("+"+(Integer.parseInt(rongGiftMessage.getGoldNum())/2));

        }





    }

    @Override
    public Spannable getContentSummary(RongGiftMessage rongGiftMessage) {
        return new SpannableString(rongGiftMessage.getContent()+"");
    }

    @Override
    public void onItemClick(View view, int i, RongGiftMessage rongGiftMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int i, RongGiftMessage rongGiftMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view= LayoutInflater.from(context).inflate(R.layout.message_gift,null);
        ViewHolder holder = new ViewHolder();
        holder.gift_background= (RelativeLayout) view.findViewById(R.id.give_background);
        holder.gift_get= (ImageView) view.findViewById(R.id.gift_get);
        holder.gift_give= (ImageView) view.findViewById(R.id.gift_give);

        holder.gift_charm= (TextView) view.findViewById(R.id.gift_charm_give_num);
        holder.gift_gold= (TextView) view.findViewById(R.id.gift_gold_give_num);


        holder.gift_charm_give= (TextView) view.findViewById(R.id.gift_charm_give);


        // holder.message = (TextView) view.findViewById(R.id.textView1);
        //holder.view = (View) view.findViewById(R.id.rc_img);
        view.setTag(holder);
        return view;
    }


    class ViewHolder {
        TextView message;
        View view;
        RelativeLayout gift_background;
        ImageView gift_give;
        ImageView gift_get;
        TextView gift_charm;
        TextView gift_gold;

        TextView gift_charm_give;

    }

}
