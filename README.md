# DragRecyclerView
 
#RecyclerView + ItemTouchHelper +ItemDecoration 实现 自定义分割线、列表拖拽、滑动动画删除；
 
# 很早的代码， 最近项目整理，留存一下；
 
# 1：导入v7+RecyclerView 包；


dependencies {
  
    compile 'com.android.support:appcompat-v7:26.+'

    compile 'com.android.support:recyclerview-v7:26.0.0-alpha1'
}

# 2: Mainactivity

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
 
# 3 ItemTouchHelper.Callback 实现主要类

public class QQMsgItemTouchCallBack extends ItemTouchHelper.Callback {

    private ItemMoveOrSwipeListener callbacklistener;

    public void setAdapterCallBck(ItemMoveOrSwipeListener listener){
        this.callbacklistener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        callbacklistener.onItemDragMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        callbacklistener.onItemSwipedDelete(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //判断选中状态
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 恢复
        viewHolder.itemView.setBackgroundColor(Color.WHITE);
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        float alpha = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //透明度动画
            viewHolder.itemView.setAlpha(alpha);//1~0
            viewHolder.itemView.setScaleX(alpha);//1~0
            viewHolder.itemView.setScaleY(alpha);//1~0
        }

        //删掉一个条目之后，恢复原状
        if (alpha == 0) {
            viewHolder.itemView.setAlpha(1);//1~0
            viewHolder.itemView.setScaleX(1);//1~0
            viewHolder.itemView.setScaleY(1);//1~0
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
 
# 4：数据源适配器

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
 
# 5: 为了解耦 创建接口类，实现 logo按下拖拽

public interface LogoDownMoveListener {

    void onLogoDownItemMove(RecyclerView.ViewHolder holder);
}

# 6: ItemDecoration 分割线 （如需要 粘性头部 列表，也在这个继承类中实现,本篇不做赘述）

public class MyItemDecroation extends RecyclerView.ItemDecoration {

    private final ColorDrawable mDivider;
    private final int orientation;//方向
    private int space;

    public MyItemDecroation(int space,int orientation,int color) {
        this.space = space;
        this.orientation = orientation;
        mDivider = new ColorDrawable(color);
    }

    /**
     * 绘制装饰
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (orientation == LinearLayoutManager.VERTICAL) {//垂直
            drawHorizontalLines(c, parent);
        }
    }

    /**
     * 绘制垂直布局 水平分割线
     */
    private void drawHorizontalLines(Canvas c, RecyclerView parent) {
        //  final int itemCount = parent.getChildCount()-1;//出现问题的地方  下面有解释
        final int itemCount = parent.getChildCount();
        Log.d("Bruce","---->"+itemCount);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < itemCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == null) return;
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top +space;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}

# 实体类 代码不贴了， 数据源的创建 自行解决把。 之前也做过相似功能，但效果并没有 ItemTouchHelper 好，并且代码量简洁。各种各样的功能代码待大家挖掘，祝所有程序猿有一个好身体，撸代码爽~~
