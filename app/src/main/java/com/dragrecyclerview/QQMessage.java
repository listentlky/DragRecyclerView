package com.dragrecyclerview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AED on 2018/7/10.
 */

public class QQMessage implements Serializable {

    private int imgId;
    private String name;
    private String endMsg;
    private String time;

    public QQMessage(int imgId, String name, String endMsg, String time) {
        this.imgId = imgId;
        this.name = name;
        this.endMsg = endMsg;
        this.time = time;
    }

    public int getImgId() {
        return imgId;
    }

    public String getName() {
        return name;
    }

    public String getEndMsg() {
        return endMsg;
    }

    public String getTime() {
        return time;
    }

    public static List<QQMessage> initMsg() {
        List<QQMessage> qqMessageList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            QQMessage qqMessage = new QQMessage(R.mipmap.ic_launcher_round, "STRAWBERRY", "LABOOKELLIE", "2017-04-27 13:15:29");
            qqMessageList.add(qqMessage);
        }
        return qqMessageList;
    }
}
