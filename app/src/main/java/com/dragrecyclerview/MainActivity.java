package com.dragrecyclerview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

public class MainActivity extends Activity implements LogoDownMoveListener {

    private RecyclerView recyclerView;
    private MyItemDecroation itemDecroation;
    private ItemTouchHelper touchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (itemDecroation == null) {
            itemDecroation = new MyItemDecroation(1, LinearLayoutManager.VERTICAL, Color.GRAY);
            recyclerView.addItemDecoration(itemDecroation);
        }

        List<QQMessage> qqMessageList = QQMessage.initMsg();
        QQMsgAdapter adapter = new QQMsgAdapter(qqMessageList);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

        QQMsgItemTouchCallBack itemTouchCallBack = new QQMsgItemTouchCallBack();
        itemTouchCallBack.setAdapterCallBck(adapter);

        touchHelper = new ItemTouchHelper(itemTouchCallBack);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onLogoDownItemMove(RecyclerView.ViewHolder holder) {
        touchHelper.startDrag(holder);
    }
}
