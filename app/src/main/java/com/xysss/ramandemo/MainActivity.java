package com.xysss.ramandemo;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.xysss.ramandemo.dao.ComBean;
import com.xysss.ramandemo.dao.CustomLibs;
import com.xysss.ramandemo.db.MyDataBaseHelper;
import com.xysss.ramandemo.linechart.DetailsMarkerView;
import com.xysss.ramandemo.linechart.MPDescription;
import com.xysss.ramandemo.linechart.MyLineChart;
import com.xysss.ramandemo.linechart.PositionMarker;
import com.xysss.ramandemo.linechart.RoundMarker;
import com.xysss.ramandemo.utils.DBUtils;
import com.xysss.ramandemo.utils.MyFunc;

import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //192.168.0.7
    private Button button1,button2,button3,button4,openSpe,setDevice,cancle,commit,history;
    private TextView test_tv_name,testing;
    private ImageView image_begin_test,test_test_anim_iv_top;
    private DatagramPacket dp, packet;
    private DatagramSocket ds; //???????????????????????????
    private boolean isopen = true;
    private List<Byte> recList = new ArrayList();
    private List<Integer>  xData= new ArrayList();
    private List<Integer>  yData= new ArrayList();
    private static int arrx[];
    private static int arry[];
    private ConstraintLayout test_btn_fast;
    private ObjectAnimator objectAnimator = null;
    private Handler mHandler;
    private static final int chartMessage=1;
    private static final int portMessage=2;
    private static final int TIMER_OUT = 5;
    private static final int uiToast = 6;
    private final int requestSpe=3;
    private final int requestName=4;
    private int switchCode=0,timerFlag = 0;
    private MyLineChart mLineChart;
    private MPDescription mDataSource = new MPDescription("?????????");
    private byte[] bytesResult=null;
    private HashMap<Integer,Integer> orderResult = new HashMap<>();  //?????????????????????
    private HashMap<Integer,Integer> orderNullResult = new HashMap<>();  //????????????????????????
    private HashMap<Integer,Integer> allResult = new HashMap<>();  //???????????????
    private static BlockingDeque<ComBean> blockingDeque = new LinkedBlockingDeque(10000);  //??????
    private String sreiaData = "";//????????????
    private LinearLayout setDetail,result_warning;
    private ConstraintLayout settingLinL;
    private final String ipSP="IP";
    private final String portSP="PORT";
    private String ipStr="",portStr="";
    private EditText ipET,portET;
    private MyDataBaseHelper mDBHelper;

    @SuppressLint("HandlerLeak")
    private static class MyHandler  extends Handler {
        private WeakReference<MainActivity>  mWeakReferenceActivity;
        public MyHandler(MainActivity activity) {
            mWeakReferenceActivity = new WeakReference<MainActivity> (activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != mWeakReferenceActivity) {
                MainActivity activity = mWeakReferenceActivity.get();
                switch (msg.what) {
                    case portMessage:
                        try {
                            activity.controlAnimation(false);
                            activity.test_btn_fast.setVisibility(View.VISIBLE);
                            activity.testing.setVisibility(View.GONE);
                            activity.settingLinL.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Object[] objsSors = (Object[]) msg.obj;
                        String strSors = (String) objsSors[0];
                        activity.test_tv_name.setText(strSors);
                        activity.test_tv_name.setVisibility(View.VISIBLE);

                        //??????????????????
                        CustomLibs cLib = new CustomLibs();
                        if (strSors != null && !strSors.equals("")) {
                            cLib.setName(strSors);
                        }

                        //String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = formatter.format(new Date());
                        cLib.setSaveUrl(time);

                        DBUtils.setCustomToDB(activity.mDBHelper, cLib);

                        //??????
                        activity.switchCode=activity.requestSpe;
                        activity.sendUdp("AT+?dataraman\r");

                        break;
                    case chartMessage:
                        LineData mLineData = activity.getLineData(arrx, arry);
                        activity.showChart( activity.mLineChart, mLineData, Color.TRANSPARENT);
                        activity.mLineChart.setVisibility(View.VISIBLE);
                        activity.setDetail.setVisibility(View.GONE);
                        break;
                    case TIMER_OUT:
                        activity.ShowMessage("????????????");
                        try {
                            activity.controlAnimation(false);
                            activity.test_btn_fast.setVisibility(View.VISIBLE);
                            activity.testing.setVisibility(View.GONE);
                            activity.setDetail.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case uiToast:
                        Object[] objsSor1 = (Object[]) msg.obj;
                        String strSor1 = (String) objsSor1[0];
                        activity.ShowMessage("strSor1");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * ???????????????
     */
    public void createMakerView() {
        DetailsMarkerView detailsMarkerView = new DetailsMarkerView(this);
        detailsMarkerView.setChartView(mLineChart);
        mLineChart.setDetailsMarkerView(detailsMarkerView);
        mLineChart.setPositionMarker(new PositionMarker(this));
        mLineChart.setRoundMarker(new RoundMarker(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //??????????????????
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView(){
        test_tv_name=findViewById(R.id.test_tv_name);
        test_btn_fast=findViewById(R.id.test_btn_fast);
        image_begin_test=findViewById(R.id.image_begin_test);
        testing = findViewById(R.id.testing);
        //????????????
        test_test_anim_iv_top = findViewById(R.id.test_test_anim_iv_top);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);
        openSpe=findViewById(R.id.openSpe);
        setDevice=findViewById(R.id.setDevice);
        setDetail=findViewById(R.id.setDetail);
        result_warning=findViewById(R.id.result_warning);
        cancle=findViewById(R.id.cancle);
        commit=findViewById(R.id.commit);
        mLineChart =findViewById(R.id.mLineChart);
        ipET=findViewById(R.id.ipET);
        portET=findViewById(R.id.portET);
        settingLinL=findViewById(R.id.settingLinL);
        history=findViewById(R.id.history);

        settingLinL.setVisibility(View.VISIBLE);
        mLineChart.setVisibility(View.INVISIBLE);
        setDetail.setVisibility(View.GONE);
        result_warning.setVisibility(View.GONE);
        //???????????????
        createMakerView();
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        test_btn_fast.setOnClickListener(this);
        openSpe.setOnClickListener(this);
        setDevice.setOnClickListener(this);
        cancle.setOnClickListener(this);
        commit.setOnClickListener(this);
        history.setOnClickListener(this);
        //image_begin_test.setOnClickListener(this);
    }

    private void initData(){
        if (ds == null) {
            try {
                ds = new DatagramSocket(6677); //???????????????????????????
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        //????????????
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
        }
        new ReceiveThread().start();
        mHandler = new MyHandler(MainActivity.this);

        mDBHelper = MyDataBaseHelper.getInstance(MainActivity.this);
    }

    private void sendUIData(int what, String message) throws Exception {
        Object[] objs = new Object[]{message};
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.obj = objs;
        mHandler.sendMessage(msg);
    }

    private void sendUdp(final String testCode) {
        new Thread() {
            public void run() {
                try {
                    byte[] buf = testCode.getBytes();
                    //byte[] buf = MyFunc.HexToByteArr(testCode);
                    //UDP???????????????????????????????????????????????????????????????????????????ip???port
                    String userIpStr= MyFunc.getStringSharedPreference(MainActivity.this,ipSP);
                    String userPortStr=MyFunc.getStringSharedPreference(MainActivity.this,portSP);
                    if(userIpStr!=null&&userPortStr!=null&&!userIpStr.equals("")&&!userPortStr.equals("")) {
                        dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(userIpStr, Integer.parseInt(userPortStr)));
                        ds.send(dp);
                        //ds.close();
                    }
                    else {
                        sendUIData(uiToast,"????????????????????????!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ds != null) {
            ds.close();
        }
        mHandler.removeCallbacks(timerTest);
        isopen = false;
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * ??????  ???
     */
    private void controlAnimation(Boolean isStart) throws Exception {
        if (objectAnimator != null) {
            if (isStart) {
                objectAnimator.start();
            } else {
                objectAnimator.end();
            }
        } else {
            objectAnimator = ObjectAnimator.ofFloat(test_test_anim_iv_top, "rotation", 0.0f, 360.0f);
            objectAnimator.setDuration(1000);
            objectAnimator.setInterpolator(new LinearInterpolator());
            objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
            if (isStart) {
                objectAnimator.start();
            } else {
                objectAnimator.end();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                sendUdp("AT+detect\r");
                ShowMessage("????????????!");
                break;
            case R.id.button2:
                sendUdp("AT+?dataraw\r");
                break;
            case R.id.button3:
                switchCode=requestSpe;
                sendUdp("AT+?dataraman\r");
                break;
            case R.id.button4:
                sendUdp("AT+?libinfo\r");
                break;
            case R.id.test_btn_fast:
                switchCode=requestName;
                sendUdp("AT+detect\r");
                test_btn_fast.setVisibility(View.GONE);
                mLineChart.setVisibility(View.INVISIBLE);
                testing.setVisibility(View.VISIBLE);
                setDetail.setVisibility(View.GONE);
                try {
                    controlAnimation(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                test_tv_name.setText("");
                result_warning.setVisibility(View.GONE);
                mHandler.postDelayed(timerTest, 30000);//??????30000??????
                break;
            case R.id.openSpe:
                result_warning.setVisibility(View.VISIBLE);
                settingLinL.setVisibility(View.GONE);
                mLineChart.setVisibility(View.GONE);
                test_tv_name.setVisibility(View.GONE);
                sendUdp("AT+L3\r");
                break;
            case R.id.setDevice:
                String userIpStr=MyFunc.getStringSharedPreference(MainActivity.this,ipSP);
                String userPortStr=MyFunc.getStringSharedPreference(MainActivity.this,portSP);
                if(userIpStr!=null&&userPortStr!=null) {
                   ipET.setText(userIpStr);
                   portET.setText(userPortStr);
                }
                mLineChart.setVisibility(View.GONE);
                setDetail.setVisibility(View.VISIBLE);
                break;
            case R.id.cancle:
                mLineChart.setVisibility(View.INVISIBLE);
                setDetail.setVisibility(View.GONE);
                break;
            case R.id.commit:
                mLineChart.setVisibility(View.INVISIBLE);
                setDetail.setVisibility(View.GONE);
                ipStr=ipET.getText().toString();
                portStr=portET.getText().toString();
                if(!ipStr.equals("")&&!portStr.equals("")) {
                    MyFunc.setStringSharedPreference(MainActivity.this,ipSP,ipStr);
                    MyFunc.setStringSharedPreference(MainActivity.this,portSP,portStr);
                }else{
                    ShowMessage("???????????????!");
                }
                break;
            case R.id.history:
                startActivity(new Intent(MainActivity.this, historyActivity.class));
                break;
            default:
                break;
        }
    }

    Runnable timerTest = new Runnable() {
        @Override
        public void run() {
            if (timerFlag == 0) {
                try {
                    sendUIData(TIMER_OUT,"");
                } catch (Exception e) {
                    ShowMessage( e.getMessage());
                }
            }
        }
    };

    private void ShowMessage(String sMsg) {
        Toast.makeText(MainActivity.this, sMsg, Toast.LENGTH_SHORT).show();
    }

    public class ReceiveThread extends Thread {

        @Override
        public void run() {
            while (isopen) {//???????????????isAlive() ?????????????????????????????????
                try {
                    Thread.sleep(100);//???????????????????????????????????????????????????
                    recList.clear();
                    byte buffer[] = new byte[10240];
                    packet = new DatagramPacket(buffer, buffer.length);
                    ds.receive(packet); //?????????????????????????????? packet
                    //String dd=MyFunc.bytesToHexString(buffer);
                    //String text=new String(packet.getData(),0,packet.getLength(),"GBK");
                    if (packet.getLength() > 0) {
                        ComBean ComRecData = new ComBean(packet.getData(), packet.getLength());
                        blockingDeque.add(ComRecData);
                    }
                    timerFlag = 1;
                    mHandler.removeCallbacksAndMessages(null);
                    final ComBean comData;
                    while ((comData = blockingDeque.poll()) != null) {
                        DispRecData(comData);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break; //??? catch ???????????????????????????
                }
            }
        }
    }

    private void DispRecData(ComBean ComRecData) {
        try {
            final StringBuilder sMsg = new StringBuilder();
            sMsg.append(MyFunc.ByteArrToHex(ComRecData.bRec));
            sreiaData += sMsg;
            byte[] getData = MyFunc.hexStringToBytes(sreiaData);
            if(((getData[getData.length-4] & 0xff) == 0x0d)&&((getData[getData.length-3] & 0xff) == 0x0a)&&
                    ((getData[getData.length-2] & 0xff) == 0x0d)&&((getData[getData.length-1] & 0xff) == 0x0a)) {
                int t=0;
                for (int i=0;i<getData.length;i++){
                    if ((getData[i] & 0xff) == 0x0a) {
                        t=t+1;
                        if(t==2) {
                            for (int j=i+1;j<getData.length;j++){
                                if(((getData[j] & 0xff) == 0x0d)&&((getData[j+1] & 0xff) == 0x0a)&&
                                        ((getData[j+2] & 0xff) == 0x0d)&&((getData[j+3] & 0xff) == 0x0a)) {
                                    break;
                                }else {
                                    recList.add(getData[j]);
                                }
                            }
                            break;
                        }
                    }
                }
                bytesResult=new byte[recList.size()];
                for (int p=0;p<recList.size();p++){
                    bytesResult[p]=recList.get(p);
                }

                sreiaData = "";

                if(switchCode==requestName) {
                    String textResult=new String(bytesResult,0,bytesResult.length,"GBK");
                    sendUIData(portMessage,textResult);
                }else if(switchCode==requestSpe) {
                    String textResultdd = MyFunc.bytesToHexString(bytesResult);
                    xData.clear();
                    yData.clear();
                    byte[] n = new byte[4];
                    byte[] x = new byte[4];
                    byte[] y = new byte[4];
                    if (bytesResult.length != 0) {
                        n[0] = bytesResult[3];
                        n[1] = bytesResult[2];
                        n[2] = bytesResult[1];
                        n[3] = bytesResult[0];
                        int speLength = MyFunc.bytesToIntLittle(n, 0);
                        for (int k = 4; k < bytesResult.length; k = k + 8) {
                            if (k + 8 > bytesResult.length) {
                                break;
                            }
                            for (int d = 0; d < 4; d++) {
                                x[d] = bytesResult[k + d];
                                y[d] = bytesResult[k + d + 4];
                            }
                            String logx = MyFunc.bytesToHexString(x);
                            String logy = MyFunc.bytesToHexString(y);
                            float xx = MyFunc.getFloat(x, 0);
                            float yy = MyFunc.getFloat(y, 0);
                            xData.add((int) xx);
                            yData.add((int) yy);
                           /* xData.add(MyFunc.bytesToIntLittle(x, 0));
                            yData.add(MyFunc.bytesToIntLittle(y, 0));*/
                        }
                        arrx = new int[xData.size()];
                        arry = new int[yData.size()];
                        if (xData.size() != 0) {
                            for (int i = 0; i < xData.size(); i++) {
                                arrx[i] = xData.get(i);
                                arry[i] = yData.get(i);
                            }
                        }
                        sendUIData(chartMessage, "");
                    }
                }
            }else {
                Log.e("1111","????????????");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private LineData getLineData(int count[], int countY[]) {
        orderResult.clear();
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count.length; i++) {
            // x?????????????????????????????????????????????????????????
            xValues.add("" + count[i]);   ///  TODO i
        }
        ArrayList<Entry> yValues = new ArrayList<Entry>();    // y????????????
        for (int i = 0; i < countY.length; i++) {
            //float value = (float) countY[i];
            yValues.add(new Entry(count[i],countY[i]));
            allResult.put(count[i],countY[i]);
        }

        // create a dataset and give it a type
        // y??????????????????
        LineDataSet lineDataSet = new LineDataSet(yValues, "" /*?????????????????????*/);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);
        //???y???????????????????????????
        lineDataSet.setLineWidth(1f); // ??????
        lineDataSet.setCircleSize(2f);// ?????????????????????
        // ??????????????????
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        // ??????????????????????????????
        lineDataSet.setDrawCircles(false);
        // ???????????????????????????
        lineDataSet.setDrawValues(false);
        // ??????????????????
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setCircleHoleColor(Color.RED);
        lineDataSet.setColor(Color.RED);// ????????????
        lineDataSet.setCircleColor(Color.RED);// ???????????????
        lineDataSet.setHighLightColor(Color.TRANSPARENT); // ?????????????????????
        /*List<PeakBean> labels=new ArrayList<>();
        for (int i=0;i<peakLength;i++){
            PeakBean peakBean=new PeakBean();
            peakBean.setxPeak(getPeakXInt[i]);
            peakBean.setyPeak(getPeakYInt[i]);
            labels.add(peakBean);
        }*/
        //lineDataSet.setValues(labels);
        //lineDataSet.setValueFormatter(new LineValueFormatter(labels));

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet); // add the datasets
        // create a data object with the datasets
        LineData lineData = new LineData(lineDataSets);
        return lineData;
    }

    // ?????????????????????
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false);  //?????????????????????????????????
        lineChart.setBackgroundColor(color);  //????????????
        // no description text
        //lineChart.setDescription(" ");// ????????????
        lineChart.setDescription(mDataSource);
        lineChart.getDescription().setTextSize(10);
        // ??????????????????????????????????????????????????????listview???emtpyview
        //lineChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable / disable grid background
        lineChart.setDrawGridBackground(true); // ????????????????????????
        lineChart.setGridBackgroundColor(color); // ??????????????????????????????????????????????????????????????????
//        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // ??????????????????????????????????????????????????????????????????
        // enable touch gestures
        lineChart.setTouchEnabled(true); // ????????????????????????
        // enable scaling and dragging
        lineChart.setDragEnabled(true);// ??????????????????
        lineChart.setScaleEnabled(true);// ??????????????????
        //?????????????????????
        lineChart.setScaleMinima(0.5f, 1f);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);//
        lineChart.setHighlightPerDragEnabled(true);  // ?????????????????????????????????
        // add data
        lineData.setDrawValues(false);  //????????????
        lineData.setValueTextSize(6f);
        lineChart.setData(lineData); // ????????????
        //??????Y???????????????
        //lineChart.setUnit("%")
        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // ??????????????????????????????????????????y???value???
        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// ??????
        mLegend.setFormSize(6f);// ??????
        mLegend.setTextColor(Color.WHITE);// ??????
//      mLegend.setTypeface(mTf);// ??????
        lineChart.getAxisRight().setEnabled(false);
//        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(true);
        lineChart.getXAxis().setEnabled(true);//???????????????????????? ??????????????????????????????????????????
        lineChart.getXAxis().setDrawAxisLine(true);//??????????????????
        lineChart.getXAxis().setDrawGridLines(true);//??????x???????????????????????????
        lineChart.getXAxis().setDrawLabels(true);//????????????  ???x?????????????????????
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //X???????????????
        //lineChart.setScaleX(1.2f);
        //Y???????????????
        //lineChart.setScaleY(1.5f);
        lineChart.setScaleYEnabled(true);//?????????????????? ???y???
        lineChart.setScaleXEnabled(true); //?????????????????? ???x???
        /*//?????????????????????
        Matrix matrix = new Matrix();
        //1f???????????????
        //matrix.postScale(1.2f, 1f);
        lineChart.getViewPortHandler().refresh(matrix, mLineChart, false);*/
        //????????????????????????????????????????????????????????????????????????????????????
        lineChart.fitScreen();
        lineChart.getViewPortHandler().getMatrixTouch().postScale(1.2f, 1f);
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        lineChart.animateX(0); // ?????????????????????,x??? time
    }
}
