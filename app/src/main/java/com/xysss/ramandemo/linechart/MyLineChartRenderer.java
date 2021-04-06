package com.xysss.ramandemo.linechart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;
import java.util.Map;

public class MyLineChartRenderer extends LineChartRenderer {

    float hLength = Utils.convertDpToPixel(15f);//横线长15dp
    float vLength = Utils.convertDpToPixel(10f);//竖线长10dp
    float rect= Utils.convertDpToPixel(8f);//矩形高低差/2
    float textX= Utils.convertDpToPixel(2f);//文本x坐标偏移量
    float textY= Utils.convertDpToPixel(3f);//文本y偏移量
    boolean isShowHLPoint = true;//是否显示最高点和最低点标识,默认显示
    int mWidth;//屏幕宽度,在构造方法中传进来赋值
    float textSixe = 10f;//文字大小
    float myOrderLength = Utils.convertDpToPixel(2f);//竖线长2dp

    public MyLineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler mViewPortHandler, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    public void drawValues(Canvas c) {
        super.drawValues(c);
        if(isShowHLPoint) {
            LineDataSet dataSetByIndex = (LineDataSet)mChart.getLineData().getDataSetByIndex(0);
            Transformer trans =mChart.getTransformer(dataSetByIndex.getAxisDependency());
            Paint paint =new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿画笔
            paint.setTextSize(Utils.convertDpToPixel(textSixe));//设置字体大小

            //画点标记
            //drawLowPoint(dataSetByIndex,trans,paint,c);
            float[] minFloat = new float[2];
            for(Map.Entry<Integer, Integer> entry: ConstValue.getData().entrySet()) {
                minFloat[0] = entry.getKey();
                minFloat[1] = entry.getValue();
                drawPoint(minFloat,trans,paint,c);
            }
        }
    }

    private void drawPoint(float[] values, Transformer trans, Paint paint, Canvas c) {
        //通过trans得到最低点的屏幕位置
        MPPointD minPoint = trans.getPixelForValues(values[0],values[1]);
        float lowX = (float)minPoint.x;
        float lowY = (float)minPoint.y;
        float rectLength = Utils.convertDpToPixel((values[1] +"").length() * Utils.convertDpToPixel(1.7f));//矩形框长
        //paint.setColor(Color.TRANSPARENT);
        //paint.setAlpha(00);
        //设置画笔为无锯齿
        //paint.setAntiAlias(true);
        //设置画笔颜色
        paint.setColor(0xFFFF7F00);//橙色
        //线宽
        paint.setStrokeWidth((float) 2.0);
        //空心效果
        paint.setStyle(Paint.Style.STROKE);
        //float rectLength = Utils.convertDpToPixel((values[1] +"").length() * Utils.convertDpToPixel(1.7f));//矩形框长
        //画横竖线
        //c.drawLine(lowX,lowY,lowX,lowY+vLength,paint);
        if(lowX >mWidth-mWidth/3) {//标识朝左
            //画矩形`
            c.drawRect(new Rect((int)(lowX-vLength-myOrderLength),(int)(lowY -vLength-vLength-myOrderLength),(int)(lowX+hLength+myOrderLength+textY+textX),(int)(lowY-vLength+myOrderLength)),paint);
            c.drawLine(lowX,lowY ,lowX,lowY-vLength,paint);
            //白色背景
            //c.drawColor(Color.WHITE);
            //画圆形
            c.drawCircle(lowX ,lowY,5f,paint );
            //c.drawText((int)values[0] +"",lowX - rectLength -hLength+textX,lowY +vLength+textY,paint);
            paint.setColor(Color.BLACK);
            c.drawText(values[0] +"",lowX - vLength,lowY-vLength-myOrderLength,paint );
        }  else {//标识朝右
            c.drawLine(lowX,lowY +vLength,lowX -hLength,lowY +vLength,paint);
            //画矩形
            c.drawRect(new Rect((int)(lowX -hLength- rectLength),(int) (lowY +vLength-rect),(int)(lowX -hLength),(int)(lowY +vLength+rect)),paint);
            //c.drawLine(lowX,lowY +vLength,lowX +hLength,lowY +vLength,paint);
            //c.drawRect(new Rect((int)(lowX +hLength),(int) (lowY +vLength-rect),(int)(lowX +hLength + rectLength),(int)(lowY +vLength+rect)),paint);
            //c.drawText((int)values[0] +"",lowX +hLength+textX,lowY +vLength+textY,paint);
            //写数字
            c.drawCircle(lowX ,lowY,5f,paint );
            c.drawText(values[0] +"",lowX - vLength,lowY-myOrderLength,paint );

        }
    }

    private void drawLowPoint(LineDataSet dataSetByIndex, Transformer trans, Paint paint, Canvas c) {
        float[] minFloat = getMinFloat(dataSetByIndex.getValues());//根据数据集获取最低点
        //通过trans得到最低点的屏幕位置
        MPPointD minPoint = trans.getPixelForValues(minFloat[0],minFloat[1]);
        float lowX = (float)minPoint.x;
        float lowY = (float)minPoint.y;
        paint.setColor(Color.parseColor("#1ab546"));
        float rectLength = Utils.convertDpToPixel((minFloat[1] +"").length() * Utils.convertDpToPixel(1.7f));//矩形框长
        //画横竖线
        c.drawLine(lowX,lowY,lowX,lowY +vLength,paint);
        if(lowX >mWidth-mWidth/3) {//标识朝左
            c.drawLine(lowX,lowY +vLength,lowX -hLength,lowY +vLength,paint);
            //画矩形
            c.drawRect(new Rect((int)(lowX -hLength- rectLength),(int) (lowY +vLength-rect),(int)(lowX -hLength),(int)(lowY +vLength+rect)),paint);
            //写数字
            paint.setColor(Color.WHITE);
            c.drawText(minFloat[0] +"",lowX - rectLength -hLength+textX,lowY +vLength+textY,paint);
        }  else {//标识朝右
            c.drawLine(lowX,lowY +vLength,lowX +hLength,lowY +vLength,paint);
            c.drawRect(new Rect((int)(lowX +hLength),(int) (lowY +vLength-rect),(int)(lowX +hLength + rectLength),(int)(lowY +vLength+rect)),paint);
            paint.setColor(Color.WHITE);
            c.drawText(minFloat[0] +"",lowX +hLength+textX,lowY +vLength+textY,paint);
        }
    }

    private float[] getMinFloat(List<Entry> lists) {
        float[] mixEntry =new float[2];
        for(int i =0; i < lists.size() -1; i++) {
            if(i ==0) {
                mixEntry[0] = lists.get(i).getX();
                mixEntry[1] = lists.get(i).getY();
            }
            if(mixEntry[1] < lists.get(i +1).getY()) {
                mixEntry[0] = lists.get(i +1).getX();
                mixEntry[1] = lists.get(i +1).getY();
            }
        }
        return mixEntry;
    }
}
