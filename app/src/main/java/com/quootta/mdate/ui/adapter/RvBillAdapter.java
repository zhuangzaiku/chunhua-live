package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.domain.BillList;
import com.quootta.mdate.utils.TimeUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ryon on 2016/3/21.
 */
public class RvBillAdapter extends RecyclerView.Adapter<BillViewHolder> {
    public static final int CANCEL_PROGRESS = 0;
    public static final int HALF_PROGRESS = 50;
    public static final int FINISH_PROGRESS = 100;

    private LayoutInflater inflater;
    private Context mContext;
    private List<BillList.Bills> billsList;

    public RvBillAdapter(Context mContext, List<BillList.Bills> billsList) {
        this.mContext = mContext;
        this.billsList = billsList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bill, parent, false);
        BillViewHolder billViewHolder = new BillViewHolder(view);
        return billViewHolder;
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        holder.tv_time.setText(TimeUtil.stamp2Date(billsList.get(position).create_time));
        switch (billsList.get(position).type) {
            case "withdraw" :
                holder.tv_title.setText(R.string.withdraw);
                holder.tv_amount.setText("-"+billsList.get(position).sum);
                break;
            case "recharge" :
                holder.tv_title.setText(R.string.recharge);
                holder.tv_amount.setText("+"+billsList.get(position).sum);
                break;
            case "income" :
                holder.tv_title.setText(R.string.income);
                holder.tv_amount.setText("+"+billsList.get(position).sum);
                break;
            case "outgo" :
                holder.tv_title.setText(R.string.outgo);
                holder.tv_amount.setText("-"+billsList.get(position).sum);
                break;
        }
        switch (billsList.get(position).status) {
            case "process":
                holder.progressBar.setProgress(HALF_PROGRESS);
                holder.tv_middle1.setVisibility(View.VISIBLE);
                holder.tv_right.setVisibility(View.GONE);
                holder.tv_middle1.setTextColor(ContextCompat.getColor(mContext, R.color.myPink));
                break;
            case "done":
                holder.progressBar.setProgress(FINISH_PROGRESS);
                holder.tv_middle1.setVisibility(View.VISIBLE);
                holder.tv_middle1.setTextColor(ContextCompat.getColor(mContext, R.color.myGrayLight));
                holder.tv_right.setVisibility(View.VISIBLE);
                holder.tv_right.setTextColor(ContextCompat.getColor(mContext, R.color.myPink));
                break;
            case "cancel":
                holder.progressBar.setProgress(CANCEL_PROGRESS);
                holder.tv_middle1.setVisibility(View.GONE);
                holder.tv_right.setVisibility(View.VISIBLE);
                holder.tv_right.setTextColor(ContextCompat.getColor(mContext, R.color.myGray));
                holder.tv_right.setText(R.string.canceled);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return billsList.size();
    }
}

class BillViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.tv_time_bill_item)TextView tv_time;
    @Bind(R.id.tv_title_bill_item)TextView tv_title;
    @Bind(R.id.tv_amount_bill_item)TextView tv_amount;
    @Bind(R.id.pb_bill_item)ProgressBar progressBar;
    @Bind(R.id.tv_middle1_status_bill_item)TextView tv_middle1;
    @Bind(R.id.tv_middle2_status_bill_item)TextView tv_middle2;
    @Bind(R.id.tv_right_status_bill_item)TextView tv_right;

    public BillViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
