package com.dragrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by AED on 2018/7/10.
 */

public class QQMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemMoveOrSwipeListener {

    private List<QQMessage> qqMessageList;
    private LogoDownMoveListener downMoveListener;

    public QQMsgAdapter(List<QQMessage> qqMessageList) {
        this.qqMessageList = qqMessageList;
    }

    public void setListener(LogoDownMoveListener moveListener){
        this.downMoveListener = moveListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.qqmsg_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            QQMessage qqMessage = qqMessageList.get(position);
            ((ViewHolder) holder).logo.setImageResource(qqMessage.getImgId());
            ((ViewHolder) holder).nickName.setText(qqMessage.getName());
            ((ViewHolder) holder).endMsg.setText(qqMessage.getEndMsg());
            ((ViewHolder) holder).time.setText(qqMessage.getTime());

            ((ViewHolder) holder).logo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        downMoveListener.onLogoDownItemMove(holder);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return qqMessageList != null ? qqMessageList.size() : 0;
    }


    @Override
    public void onItemDragMove(int formPosition, int toPosition) {
        Collections.swap(qqMessageList, formPosition, toPosition);
        notifyItemMoved(formPosition, toPosition);
    }

    @Override
    public void onItemSwipedDelete(int position) {
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView nickName;
        TextView endMsg;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logo);
            nickName = itemView.findViewById(R.id.nickName);
            endMsg = itemView.findViewById(R.id.endMsg);
            time = itemView.findViewById(R.id.time);
        }
    }
}
