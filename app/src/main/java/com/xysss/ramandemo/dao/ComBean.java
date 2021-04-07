package com.xysss.ramandemo.dao;

/**
 * Author:bysd-2
 * Time:2021/3/2916:24
 */
public class ComBean {
    public byte[] bRec = null;
    public ComBean(byte[] paramArrayOfByte, int paramInt) {
        this.bRec = new byte[paramInt];
        for (int i = 0; ; i++) {
            if (i >= paramInt) {
                return;
            }
            this.bRec[i] = paramArrayOfByte[i];
        }
    }
}
