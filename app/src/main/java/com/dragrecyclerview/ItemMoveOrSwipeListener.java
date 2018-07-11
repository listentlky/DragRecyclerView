package com.dragrecyclerview;

/**
 * Created by AED on 2018/7/10.
 */

public interface ItemMoveOrSwipeListener {

    void onItemDragMove(int formPosition,int toPosition);

    void onItemSwipedDelete(int position);
}
