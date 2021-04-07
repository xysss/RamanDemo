package com.xysss.ramandemo.linechart;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.LineChart;

import java.lang.ref.WeakReference;

public class MyLineChart extends LineChart {

    //弱引用覆盖物对象,防止内存泄漏
    private WeakReference<DetailsMarkerView> mDetailsReference;
    private WeakReference<RoundMarker> mRoundMarkerReference;
    private WeakReference<PositionMarker> mPositionMarkerReference;

    /**
     * 所有覆盖物是否为空
     *
     * @return TRUE FALSE
     */
    public boolean isMarkerAllNull() {
        return mDetailsReference.get() == null && mRoundMarkerReference.get() == null && mPositionMarkerReference.get() == null;
    }

    public void setDetailsMarkerView(DetailsMarkerView detailsMarkerView) {
        mDetailsReference = new WeakReference<>(detailsMarkerView);
    }

    public void setRoundMarker(RoundMarker roundMarker) {
        mRoundMarkerReference = new WeakReference<>(roundMarker);
    }

    public void setPositionMarker(PositionMarker positionMarker) {
        mPositionMarkerReference = new WeakReference<>(positionMarker);
    }

    public MyLineChart(Context context) {
        super(context);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * draws all MarkerViews on the highlighted positions
     */
    //点击自定义显示坐标信息
    /*protected void drawMarkers(Canvas canvas) {

        // if there is no marker view or drawing marker is disabled
        DetailsMarkerView mDetailsMarkerView = mDetailsReference.get();  //（详情）
        RoundMarker mRoundMarker = mRoundMarkerReference.get();  //（中间的标杆）
        PositionMarker mPositionMarker = mPositionMarkerReference.get();  //（圆点）

        if (mDetailsMarkerView == null || mRoundMarker == null || mPositionMarker == null || !isDrawMarkersEnabled() || !valuesToHighlight())
            return;

        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Highlight highlight = mIndicesToHighlight[i];

            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());

            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);

            int entryIndex = set.getEntryIndex(e);

            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX())
                continue;

            float[] pos = getMarkerPosition(highlight);

            LineDataSet dataSetByIndex = (LineDataSet) getLineData().getDataSetByIndex(highlight.getDataSetIndex());

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                continue;

            float circleRadius = dataSetByIndex.getCircleRadius();

            // callbacks to update the content
            mDetailsMarkerView.refreshContent(e, highlight);

            mDetailsMarkerView.draw(canvas, pos[0], pos[1] - mPositionMarker.getHeight());


            mPositionMarker.refreshContent(e, highlight);
            mPositionMarker.draw(canvas, pos[0] - mPositionMarker.getWidth() / 2, pos[1] - mPositionMarker.getHeight());

            mRoundMarker.refreshContent(e, highlight);
            mRoundMarker.draw(canvas, pos[0] - mRoundMarker.getWidth() / 2, pos[1] + circleRadius - mRoundMarker.getHeight());
        }
    }*/

    @Override
    protected void init() {
        super.init();
        //获取屏幕宽度,因为默认是向右延伸显示数字的(如图1),当最值在屏幕右端,屏幕不够显示时要向左延伸(如图2)
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics =new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        //将mRenderer换成自己写的继承自LineChartRenderer的类
        mRenderer = new MyLineChartRenderer(this,mAnimator,mViewPortHandler,mViewPortHandler);
    }
}
