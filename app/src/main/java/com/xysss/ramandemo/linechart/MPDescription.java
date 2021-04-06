package com.xysss.ramandemo.linechart;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.utils.MPPointF;

public class MPDescription extends Description {


    /**
     * the text used in the description
     */
    private String text = "";

    /**
     * the custom position of the description text
     */
    private MPPointF mPosition;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public MPPointF getmPosition() {
        return mPosition;
    }

    public void setmPosition(MPPointF mPosition) {
        this.mPosition = mPosition;
    }
    public MPDescription(String text){
        this.text = text;
    }


}
