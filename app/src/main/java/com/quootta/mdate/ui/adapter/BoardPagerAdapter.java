package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.ui.view.RoundNetImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by ryon on 2016/11/20.
 */

public class BoardPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private List<List<String>> boardImgs;
    private OnBoardClickListener onBoardClickListener;
    private OnItemClickListener onItemClickListener;


    public interface OnBoardClickListener {
        void onClick(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int index);
    }

    public BoardPagerAdapter(Context context, List<List<String>> boardImgs, OnBoardClickListener onBoardClickListener) {
        this.context = context;
        this.boardImgs = boardImgs;
        this.onBoardClickListener = onBoardClickListener;
        mInflater = LayoutInflater.from(context);
        imageLoader = BaseApp.getImageLoader();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return boardImgs.size();

    }

    private int getRealPos(int position) {
        if (position == getCount() - 1) {
            return 0;
        } else {
            return position;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        int realPos = getRealPos(position);

        View rootView = mInflater.inflate(R.layout.item_board, container, false);
        if (onBoardClickListener != null) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBoardClickListener.onClick(position);
                }
            });
        }

        setType(rootView, realPos);
        setPics(rootView, realPos);

        container.addView(rootView);
        return rootView;
    }

    private void setType(View rootView, int position) {
        ImageView label = (ImageView) rootView.findViewById(R.id.iv_board_label);

        if (onBoardClickListener != null) {
            if (position == 0 || position == getCount() -1) {
                label.setImageResource(R.mipmap.ic_charm);
            } else {
                label.setImageResource(R.mipmap.ic_rich);
            }
        } else if (onItemClickListener != null) {

            if (position == 0 || position == getCount() -1) {
                label.setImageResource(R.mipmap.ic_recent_callin);
            } else {
                label.setImageResource(R.mipmap.ic_recent_callout);
            }
        }
    }

    private void setPics(View rootView, final int position) {
        RoundNetImageView iv1 = (RoundNetImageView) rootView.findViewById(R.id.iv_pic1);

        RoundNetImageView iv2 = (RoundNetImageView) rootView.findViewById(R.id.iv_pic2);

        RoundNetImageView iv3 = (RoundNetImageView) rootView.findViewById(R.id.iv_pic3);

        if (!"null".equalsIgnoreCase(boardImgs.get(position).get(0))) {
            imageLoader.get(
                    LocalUrl.getPicUrl(boardImgs.get(position).get(0)),
                    imageLoader.getImageListener(iv1, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
        }
        if (!"null".equalsIgnoreCase(boardImgs.get(position).get(1))) {
            imageLoader.get(
                    LocalUrl.getPicUrl(boardImgs.get(position).get(1)),
                    imageLoader.getImageListener(iv2, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
        }
        if (!"null".equalsIgnoreCase(boardImgs.get(position).get(2))) {
            imageLoader.get(
                    LocalUrl.getPicUrl(boardImgs.get(position).get(2)),
                    imageLoader.getImageListener(iv3, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
        }

        if(onItemClickListener != null) {
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position, 0);
                }
            });

            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position, 1);
                }
            });

            iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position, 2);
                }
            });
        } else if(onBoardClickListener!= null) {
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBoardClickListener.onClick(position);
                }
            });

            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBoardClickListener.onClick(position);
                }
            });

            iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBoardClickListener.onClick(position);
                }
            });

        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
