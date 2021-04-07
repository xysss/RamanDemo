package com.xysss.ramandemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xysss.ramandemo.adapter.MySelfLibraryAdapter;
import com.xysss.ramandemo.dao.CustomLibs;
import com.xysss.ramandemo.db.MyDataBaseHelper;
import com.xysss.ramandemo.utils.DBUtils;

import java.util.ArrayList;

public class historyActivity extends AppCompatActivity {
    private final String TAG = historyActivity.class.getSimpleName();
    private RecyclerView library_recycleView;
    private MySelfLibraryAdapter adapter;
    private ArrayList<CustomLibs> mCustomLibsRecycle=new ArrayList<>();
    private MyDataBaseHelper mDBHelper;
    private SwipeRefreshLayout library_refreshLayout;
    private RelativeLayout empty_view;
    private LinearLayoutManager layoutManager;
    private boolean isDeleteSuccess;
    private int startIndex; //记录当前的角标；.
    private int seekNum;
    private final int ONCE_MAX_COUNT = 20; //当前最大加载数量
    private int lastVisibleItemPosition = 0;
    private boolean controlSingle;
    private ArrayList<CustomLibs> pageDatas = new ArrayList<>();  //分页查询列表
    private static final int GET_DATA = 0;
    private static final int PAGE_DATA = 1;
    private static final int GET_ALL_COUNT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        initView();
    }

    public void initView(){
        mDBHelper = MyDataBaseHelper.getInstance(historyActivity.this);

        // 数量多的时候放到线程里处理
        new Thread() {
            public void run() {
                int allCount = DBUtils.getCustomNum(mDBHelper);
                handler1.obtainMessage(GET_ALL_COUNT,allCount).sendToTarget();
            }
        }.start();


    }

    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PAGE_DATA:
                    pageDatas = (ArrayList<CustomLibs>) msg.obj;
                    if (pageDatas != null) {
                        updateRecyclerView(pageDatas);
                    }
                    break;
                case GET_ALL_COUNT:
                    int allCount = (int) msg.obj;
                    seekNum = ONCE_MAX_COUNT;
                    startIndex=allCount;
                    if (startIndex <= ONCE_MAX_COUNT) {
                        seekNum = startIndex;
                        startIndex = 0;
                    }else {
                        startIndex = allCount - ONCE_MAX_COUNT;
                    }

                    CurrThread cThread = new CurrThread(seekNum, startIndex);
                    cThread.start();
                    break;
                case GET_DATA:

                    mCustomLibsRecycle = (ArrayList<CustomLibs>) msg.obj;

                    empty_view=findViewById(R.id.empty_view);

                    library_recycleView=findViewById(R.id.library_recycleView);
                    adapter = new MySelfLibraryAdapter(historyActivity.this, mCustomLibsRecycle, mCustomLibsRecycle.size() > 0 ? true : false);
                    layoutManager = new LinearLayoutManager(historyActivity.this, LinearLayoutManager.VERTICAL, false);
                    library_recycleView.setLayoutManager(layoutManager);
                    library_recycleView.setAdapter(adapter);
                    library_recycleView.setItemAnimator(new DefaultItemAnimator());
                    //刷新控件；
                    library_refreshLayout = (SwipeRefreshLayout) findViewById(R.id.library_refreshLayout);

                    library_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            pageModify();
                /*library_refreshLayout.setRefreshing(true);
                if (adapter != null) {
                    library_refreshLayout.setRefreshing(false);
                    adapter.resetDatas();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //获取自建库中所有数据
                            mCustomLibs = DBUtils.getAllLibraryDate(mDBHelper);
                            if (mCustomLibs.size() != 0) {
                                updateRecyclerView(mCustomLibs);
                                library_refreshLayout.setRefreshing(false);
                            } else {
                                library_refreshLayout.setRefreshing(false);
                            }
                        }
                    }.start();
                } else {
                    library_refreshLayout.setRefreshing(false);
                }*/
                        }
                    });

                    library_recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);

                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {  //静止时
                                if (adapter.isFadeTips() == false && lastVisibleItemPosition + 1 == adapter.getItemCount() && !controlSingle) {
                                    scrollProcess(1);
                                }
                                if (adapter.isFadeTips() == true && lastVisibleItemPosition + 2 == adapter.getItemCount() && !controlSingle) {
                                    scrollProcess(2);
                                }
                            }
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            //当前最后一个可见的item；
                            lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                        }
                    });


                    adapter.setOnItemClickLitener(new MySelfLibraryAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position, CustomLibs mResults) {

                            /*final int codeClick=mResults.getCode();
                            final EditText et = new EditText(historyActivity.this);
                            new AlertDialog.Builder(historyActivity.this).setTitle("请输入修改后的名字")
                                    *//*.setIcon(android.R.drawable.sym_def_app_icon)*//*
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //按下确定键后的事件
                                            DBUtils.upSelfLibraryName(mDBHelper,codeClick,et.getText().toString());
                                            Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_LONG).show();
                                            RefresLibraryhUI();
                                        }
                                    }).setNegativeButton("取消",null).show();*/
                        }

                        @Override
                        public void onItemLongClick(View view, int position, CustomLibs mResults) {

                            final int codeLongClick=mResults.getCode();
                            final String url=mResults.getSaveUrl();
                            //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                            AlertDialog.Builder builder = new AlertDialog.Builder(historyActivity.this);
                            //    设置Title的图标
                            builder.setIcon(R.mipmap.history_delete_icon);
                            //    设置Title的内容
                            //builder.setTitle("弹出警告框");
                            //    设置Content来显示一个信息
                            builder.setMessage("确定删除这条记录吗？");
                            //    设置一个PositiveButton
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    isDeleteSuccess= DBUtils.deleteLibraryItem(mDBHelper,codeLongClick);
                                    if (isDeleteSuccess){
                                        pageModify();
                                        Toast.makeText(historyActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //    设置一个NegativeButton
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    //Toast.makeText(historyActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                                }
                            });
                            //    设置一个NeutralButton
                                /*builder.setNeutralButton("忽略", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        Toast.makeText(mActivity, "neutral: " + which, Toast.LENGTH_SHORT).show();
                                    }
                                });*/
                            //    显示出该对话框
                            builder.show();
                        }
                    });

                    break;
            }
        }
    };

    private class CurrThread extends Thread {
        private int number;
        private int startIndex;

        public CurrThread(int number, int startIndex) {
            this.number = number;
            this.startIndex = startIndex;
        }

        @Override
        public void run() {
            super.run();
            ArrayList<CustomLibs> mResultses = DBUtils.getCustormLimitDatas(mDBHelper, number, startIndex);
            if (mResultses != null) {
                Message msg = handler1.obtainMessage();
                msg.what = GET_DATA;
                msg.obj = mResultses;
                handler1.sendMessage(msg);
            }
        }
    }


    private void scrollProcess(int a) {
        controlSingle = true;
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                controlSingle = false;
            }
        }, 500);

        Log.e(TAG, "分页");

        seekNum = ONCE_MAX_COUNT;
        if (startIndex <= ONCE_MAX_COUNT) {
            seekNum=startIndex;
            startIndex = 0;
        }
        else {
            startIndex = startIndex - ONCE_MAX_COUNT;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                pageDatas.clear();
                if (a == 1) {
                    pageDatas = DBUtils.getCustormLimitDatas(mDBHelper, seekNum,startIndex);

                } else {
                    pageDatas = DBUtils.getCustormLimitDatas(mDBHelper, seekNum,startIndex );
                }
                Message msg1 = handler1.obtainMessage();
                msg1.what = PAGE_DATA;
                msg1.obj = pageDatas;
                handler1.sendMessage(msg1);
            }
        }.start();
    }


    private void pageModify() {
        library_refreshLayout.setRefreshing(true);
        if (adapter != null) {
            library_refreshLayout.setRefreshing(false);
            adapter.resetDatas();
            int allCount = DBUtils.getCustomNum(mDBHelper);
            startIndex = allCount;
            seekNum = ONCE_MAX_COUNT;
            if (startIndex <= ONCE_MAX_COUNT) {
                seekNum = startIndex;
                startIndex = 0; //
            }else {
                startIndex = allCount - ONCE_MAX_COUNT;
            }

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mCustomLibsRecycle.clear();
                    adapter.mCustomLibsAdapter.clear();
                    mCustomLibsRecycle = DBUtils.getCustormLimitDatas(mDBHelper, seekNum, startIndex);  //取前几条数据
                            /*//获取表中所有数据
                            final ArrayList<MResults> mResultses = DBUtils.getAllTestDate(mDBHelper);*/
                    historyActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mCustomLibsRecycle != null) {
                                updateRecyclerView(mCustomLibsRecycle);
                                library_refreshLayout.setRefreshing(false);
                            } else {
                                library_refreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }
            }.start();
        } else {
            library_refreshLayout.setRefreshing(false);
        }
    }

    //刷新界面
    private void RefresLibraryhUI(){
        final ArrayList<CustomLibs> mDeleteResultses = DBUtils.getAllLibraryDate(mDBHelper);
        adapter.updateList(mDeleteResultses,true);
    }


    private void updateRecyclerView(ArrayList<CustomLibs> datas) {
        if (datas.size() > 0) {
            if(datas.size()>10) {
                adapter.updateList(datas, true);
            }else {
                adapter.updateList(datas, false);
            }
        } else {
            adapter.updateList(datas, false);
        }
    }
}