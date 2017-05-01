package com.example.attendance.bean;

/**
 * Created by 陈振聪 on 2017/4/25.
 */
public class Cid {

    public String cid ;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    private static Cid sharedCid = null;
    public static Cid sharedCid() {
        if (sharedCid == null) {
            sharedCid = new Cid();
        }
        return sharedCid;
    }
}
