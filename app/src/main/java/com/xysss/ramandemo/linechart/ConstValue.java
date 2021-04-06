package com.xysss.ramandemo.linechart;

import java.util.HashMap;

public class ConstValue {

    private static HashMap<Integer, Integer> data=new HashMap<>();

    public static void setData(HashMap<Integer, Integer> value) {
        data = value;
    }

    public static HashMap<Integer, Integer> getData() {
        return data;
    }

}
