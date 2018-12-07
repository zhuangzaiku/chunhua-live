package com.quootta.mdate.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.utils.AssetsUtils;
import com.quootta.mdate.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.qqtheme.framework.picker.AddressPicker;

/**
 * Created by kky on 2016/4/5.
 */
public class AddressInitTask extends AsyncTask<String, Void, ArrayList<AddressPicker.Province>> {
    private Activity activity;
    private ProgressDialog dialog;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private boolean hideCounty=false;
    private OnCitySelectListener onCitySelectListener;

    public interface OnCitySelectListener {
        void onSelect(String province, String city, String county);
    }

    /**
     * 初始化为不显示区县的模式
     * @param activity
     * @param hideCounty   is hide County
     */
    public AddressInitTask(Activity activity,boolean hideCounty,OnCitySelectListener onCitySelectListener) {
        this.activity = activity;
        this.hideCounty=hideCounty;
//        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
        dialog = ProgressDialog.show(activity, null, BaseApp.getApplication().getString(R.string.app_tips_text11), true, true);
        this.onCitySelectListener = onCitySelectListener;
    }

    public AddressInitTask(Activity activity) {
        this.activity = activity;
//        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
        dialog = ProgressDialog.show(activity, null, BaseApp.getApplication().getString(R.string.app_tips_text11), true, true);
    }

    @Override
    protected ArrayList<AddressPicker.Province> doInBackground(String... params) {
        if (params != null) {
            switch (params.length) {
                case 1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
                default:
                    break;
            }
        }
        ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();
        try {
            String json = AssetsUtils.readText(activity, "city.json");
//            data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
            Type type = new TypeToken<ArrayList<AddressPicker.Province>>() {
            }.getType();
            ArrayList<AddressPicker.Province> provinces = GsonUtil.parse(json, type);
            data.addAll(provinces);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<AddressPicker.Province> result) {
        dialog.dismiss();
        if (result.size() > 0) {
            AddressPicker picker = new AddressPicker(activity, result);
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(String province, String city, String county) {
                    if (county==null){
                        onCitySelectListener.onSelect(province, city, county);
                    } else {
                        onCitySelectListener.onSelect(province, city, county);
                    }
                }
            });
            picker.show();
        } else {
//            Toast.makeText(activity, "数据初始化失败", Toast.LENGTH_SHORT).show();
            Toast.makeText(activity, BaseApp.getApplication().getString(R.string.app_tips_text12), Toast.LENGTH_SHORT).show();
        }
    }

}