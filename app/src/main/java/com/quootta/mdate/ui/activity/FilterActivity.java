package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class FilterActivity extends BaseActivity {

    @Bind(R.id.iv_back_title_bar) ImageView iv_back;
    @Bind(R.id.tv_ensure_title_bar) TextView tv_ensure;

    @Bind(R.id.rg_sex_filter) RadioGroup rg_sex;
    @Bind(R.id.rg_age_filter) RadioGroup rg_age;
    @Bind(R.id.rg_time_filter) RadioGroup rg_time;
    @Bind(R.id.rg_distance_filter) RadioGroup rg_distance;
    
//    @Bind(R.id.rb_sex_all_filter) RadioButton rb_sex_all;
//    @Bind(R.id.rb_sex_male_filter) RadioButton rb_sex_male;
//    @Bind(R.id.rb_sex_female_filter) RadioButton rb_sex_female;
//    @Bind(R.id.rb_age_1_filter) RadioButton rb_age1;
//    @Bind(R.id.rb_age_2_filter) RadioButton rb_age2;
//    @Bind(R.id.rb_age_3_filter) RadioButton rb_age3;
//    @Bind(R.id.rb_age_4_filter) RadioButton rb_age4;
//    @Bind(R.id.rb_time_1_filter) RadioButton rb_time1;
//    @Bind(R.id.rb_time_2_filter) RadioButton rb_time2;
//    @Bind(R.id.rb_time_3_filter) RadioButton rb_time3;
//    @Bind(R.id.rb_time_4_filter) RadioButton rb_time4;
//    @Bind(R.id.rb_distance_1_filter) RadioButton rb_distance1;
//    @Bind(R.id.rb_distance_2_filter) RadioButton rb_distance2;
//    @Bind(R.id.rb_distance_3_filter) RadioButton rb_distance3;
//    @Bind(R.id.rb_distance_4_filter) RadioButton rb_distance4;


    @Override
    protected void init() {
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_filter;
    }

    @Override
    protected void initData() {
        iv_back.setVisibility(View.VISIBLE);
        tv_ensure.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

        tv_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> paramsMap = getSelectedOption();
                List<Map<String, String>> list = new ArrayList<>();
                list.add(paramsMap);
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                //须定义一个list用于在bundle中传递需要传递的ArrayList<Object>,这个是必须要的
                ArrayList bundleList = new ArrayList();
                bundleList.add(list);
                bundle.putParcelableArrayList("list", bundleList);
                resultIntent.putExtras(bundle);
                setResult(RESULT_OK, resultIntent);
                ActivityUtil.finishActivty();
            }
        });
    }

    public Map<String,String> getSelectedOption() {

        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("page","0");

        switch (rg_sex.getCheckedRadioButtonId()) {
            case R.id.rb_sex_all_filter:
                paramsMap.put("gender","");
                break;
            case R.id.rb_sex_male_filter:
                paramsMap.put("gender","male");
                break;
            case R.id.rb_sex_female_filter:
                paramsMap.put("gender","female");
                break;
            default:
                break;
        }

        switch (rg_age.getCheckedRadioButtonId()) {
            case R.id.rb_age_1_filter:
                paramsMap.put("age_l","");
                paramsMap.put("age_h","18");
                break;
            case R.id.rb_age_2_filter:
                paramsMap.put("age_l","18");
                paramsMap.put("age_h","23");
                break;
            case R.id.rb_age_3_filter:
                paramsMap.put("age_l","23");
                paramsMap.put("age_h","30");
                break;
            case R.id.rb_age_4_filter:
                paramsMap.put("age_l","30");
                paramsMap.put("age_h","");
                break;
            default:
                break;
        }

        switch (rg_time.getCheckedRadioButtonId()) {
            case R.id.rb_time_1_filter:
                paramsMap.put("time_ago","15");
                break;
            case R.id.rb_time_2_filter:
                paramsMap.put("time_ago","60");
                break;
            case R.id.rb_time_3_filter:
                paramsMap.put("time_ago","1440");
                break;
            case R.id.rb_time_4_filter:
                paramsMap.put("time_ago","");
                break;
            default:
                break;
        }

        switch (rg_distance.getCheckedRadioButtonId()) {
            case R.id.rb_distance_1_filter:
                paramsMap.put("distance","1");
                break;
            case R.id.rb_distance_2_filter:
                paramsMap.put("distance","3");
                break;
            case R.id.rb_distance_3_filter:
                paramsMap.put("distance","5");
                break;
            case R.id.rb_distance_4_filter:
                paramsMap.put("distance","");
                break;
            default:
                break;
        }

        return paramsMap;
    }
}
