package com.xysss.ramandemo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MySelfLibraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Context context;
    public ArrayList<CustomLibs> mCustomLibsAdapter;
    private boolean hasMore = true;
    private boolean fadeTips = false;
    private MySelfLibraryAdapter.OnItemClickLitener mOnItemClickLitener;
    private int footType = 1;
    private int normalType = 0;
    private int visibleItemCount = 0;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, CustomLibs mResults);

        void onItemLongClick(View view, int position, CustomLibs mResults);
    }

    public void setOnItemClickLitener(MySelfLibraryAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

//    public void setVisibleItemCount(int visibleItemCount) {
//        this.visibleItemCount = visibleItemCount;
//    }

    public MySelfLibraryAdapter(Context context, ArrayList<CustomLibs> mResultses, boolean hasMore) {
        this.context = context;
        this.mCustomLibsAdapter = mResultses;
        this.hasMore = hasMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == normalType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.library_item_recycle, parent, false);
            return new MySelfLibraryAdapter.MyLibraryViewHolder(itemView);
        } else {
            return new MySelfLibraryAdapter.LibraryFootHolder(LayoutInflater.from(context).inflate(R.layout.footview, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MySelfLibraryAdapter.MyLibraryViewHolder) {

            MySelfLibraryAdapter.MyLibraryViewHolder myViewHolder = (MySelfLibraryAdapter.MyLibraryViewHolder) holder;

            if (mCustomLibsAdapter.size()!=0) {
                myViewHolder.code.setText(String.valueOf(mCustomLibsAdapter.get(position).getCode()));
                myViewHolder.name.setText(mCustomLibsAdapter.get(position).getName());
                myViewHolder.time.setText(mCustomLibsAdapter.get(position).getSaveUrl());
            }
        }

        else{
            ((MySelfLibraryAdapter.LibraryFootHolder) holder).tips.setVisibility(View.VISIBLE);

            if (hasMore == true) {
                fadeTips = false;
                if (mCustomLibsAdapter.size() > 20) {  // // TODO: 2017/12/12   限制item数量；
                    ((MySelfLibraryAdapter.LibraryFootHolder) holder).tips.setText("正在加载更多...");
                }
            } else {
                if (mCustomLibsAdapter.size() > 0) {
                    ((MySelfLibraryAdapter.LibraryFootHolder) holder).tips.setText("没有更多数据了");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((MySelfLibraryAdapter.LibraryFootHolder) holder).tips.setVisibility(View.GONE);
                            fadeTips = true;
                            hasMore = true;
                        }
                    }, 500);
                }
            }
        }

    }


    public int getRealLastPosition() {
        return mCustomLibsAdapter.size();
    }


    public void updateList(List<CustomLibs> newDatas, boolean hasMore) {
        if (newDatas.size()!= 0) {
            //mCustomLibsAdapter.clear();
            mCustomLibsAdapter.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }


    public boolean isFadeTips() {
        return fadeTips;
    }

    public void resetDatas() {
//        int size = mResultses.size();
        mCustomLibsAdapter = new ArrayList<>();
//        notifyItemRangeRemoved(0,size);
        notifyDataSetChanged(); //  防止在移除数据后在刷新新数据时导致内部错误；
    }


    @Override
    public int getItemCount() {
//        return mResultses != null ? mResultses.size() + 1 : 0;
        // 这里添加了一个脚view  注意多加1；
        return mCustomLibsAdapter.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }

    class MyLibraryViewHolder extends RecyclerView.ViewHolder {

        private TextView code,name,time;
        private LinearLayout item_linearLayout;

        public MyLibraryViewHolder(View itemView) {
            super(itemView);

            code = itemView.findViewById(R.id.code);
            name = itemView.findViewById(R.id.name);
            time=itemView.findViewById(R.id.time);
            item_linearLayout=itemView.findViewById(R.id.item_linearLayout);

            item_linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onItemClick(v, getLayoutPosition(), mCustomLibsAdapter.get(getLayoutPosition()));
                    }
                }
            });

            item_linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onItemLongClick(v, getLayoutPosition(), mCustomLibsAdapter.get(getLayoutPosition()));
                    }
                    return false;
                }
            });

        }
    }

    class LibraryFootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public LibraryFootHolder(View itemView) {
            super(itemView);
            tips = itemView.findViewById(R.id.tips);
        }
    }
}
