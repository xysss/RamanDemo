package com.xysss.ramandemo.linechart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.xysss.ramandemo.R;

import java.math.BigDecimal;

/**
 * @author Lai
 * @time 2018/5/13 17:32
 * @describe describe
 */

public class DetailsMarkerView extends MarkerView {

    private TextView mTvMonth;
    private TextView mTvChart1;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     */
    public DetailsMarkerView(Context context) {
        super(context, R.layout.item_chart_des_marker_item_3);
        mTvMonth = findViewById(R.id.tv_chart_month);
        mTvChart1 = findViewById(R.id.tv_chart_1);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        try {
            //收入
            if (e.getY() == 0) {
                mTvChart1.setText("暂无数据");
            } else {
                mTvChart1.setText("Y: "+e.getY());
            }
            mTvMonth.setText("X: "+ String.valueOf((int) e.getX() + 1));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        super.refreshContent(e, highlight);
    }


    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }


    public String concat(float money, String values) {
        return values + new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "";
    }

}
