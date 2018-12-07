package com.quootta.mdate.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryon on 2016/11/16.
 */

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 3;  //说明是带有Header和Footer的

    protected List<T> mDatas = new ArrayList<>();
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected View mHeaderView;
    protected View mFooterView;

    AdapterView.OnItemClickListener mItemClickListener;

    private static String TAG = "BaseRecyclerAdapter";

    public BaseRecyclerAdapter(Context mContext, List<T> mDatas) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }

    /**
     * 通过判断item的类型，从而绑定不同的view
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        if (mHeaderView == null && mFooterView == null){
            return TYPE_NORMAL;
        }

        if (mHeaderView != null && position == 0 ){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }

        if (mFooterView != null && position == getItemCount()-1 ){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }

        return TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            final T item = getItem(position);
            bindItemData(holder, item, position);
            setupOnItemClick(holder, position);
            return;
        } else if (getItemViewType(position) == TYPE_HEADER) {
            bindHeaderData(holder);
            return;
        } else if (getItemViewType(position) == TYPE_FOOTER) {
            bindFooterData(holder);
            return;
        } else {

            return;
        }
    }

    protected abstract void bindHeaderData(VH holder);

    protected abstract void bindFooterData(VH holder);

    protected abstract void bindItemData(VH viewHolder, T data, int position);

    protected void setupOnItemClick(final VH viewHolder, final int position) {
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(null, viewHolder.itemView, getRealPosition(viewHolder), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null) {
            return mDatas.size();
        } else if (mHeaderView == null && mFooterView != null) {
            return mDatas.size() + 1;
        } else if (mHeaderView != null && mFooterView == null) {
            return mDatas.size() + 1;
        } else {
            return mDatas.size() + 2;
        }
    }

    public T getItem(int position) {
        if (mHeaderView != null) {
            position = position - 1;
        }
        position = Math.max(0, position);

        return mDatas.get(position);
    }

    private int getRealPosition(VH holder) {
        int position = holder.getLayoutPosition();

        return mHeaderView == null ? position : position - 1;
    }

    public List<T> getDataSource() {
        return mDatas;
    }

    public void addData(List<T> newItems) {
        if (newItems != null) {
            mDatas.addAll(newItems);
            notifyDataSetChanged();
        }
    }

    public void updateListViewData(List<T> lists) {
        mDatas.clear();
        if (lists != null) {
            mDatas.addAll(lists);
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return ( getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER ) ?
                            gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }
}