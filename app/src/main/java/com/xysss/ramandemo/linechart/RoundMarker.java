package com.xysss.ramandemo.linechart;

import android.content.Context;

import com.github.mikephil.charting.components.MarkerView;
import com.xysss.ramandemo.R;


/**
 * @author Lai
 * @time 2018/5/26 14:31
 * @describe describe
 */

public class RoundMarker extends MarkerView {

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     * @param context
     */
    public RoundMarker(Context context) {
        super(context, R.layout.item_chart_round);
    }
}
