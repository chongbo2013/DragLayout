package service.bind.test.draglayout;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * 在workspace里面去判别，移动和，缩放2个操作
 * Created by ferris.xu on 2016/9/5.
 */
public class WorkSpace extends ViewGroup implements DropTarget{
    private final static int INVALID_ID = -1;
    private int mActivePointerId = INVALID_ID;
    private float mLastY=0;
    private float mLastX=0;

    private int mSecondaryPointerId = INVALID_ID;
    private float mSecondaryLastX=0;
    private float mSecondaryLastY =0;



    private int mTouchSlop;
    private int mMinFlingSpeed;
    private int mMaxFlingSpeed;
    private int mOverFlingDistance;
    private int mOverScrollDistance;
    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;

    int width=0,height=0;
    //触摸主要要3种状态
    private final static int TOUCH_SCROLL =1;
    private final static int TOUCH_MULTI =2;
    private final static int TOUCH_STATE_RESET =3;
    private int mTouchStase=TOUCH_STATE_RESET;

    //界面缩放值
    //用来作为判断双指缩放参数
    private float scale=1.0f;



    public WorkSpace(Context context) {
        super(context);
        init();
    }

    public WorkSpace(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorkSpace(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        MainActivity.get().getDragController().addDropWorkSpace(this);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinFlingSpeed = configuration.getScaledMinimumFlingVelocity();
        mMaxFlingSpeed = configuration.getScaledMaximumFlingVelocity();
        mOverFlingDistance = configuration.getScaledOverflingDistance();
        mOverScrollDistance = configuration.getScaledOverscrollDistance();
        mScroller = new OverScroller(getContext());

        //一般来说mOverScrollDistance为0，OverFlingDistance不一致，这里为了整强显示效果
        mOverFlingDistance = 50;

        setOverScrollMode(OVER_SCROLL_ALWAYS);
        // 这里还是需要的。overScrollBy中会使用到
        /**
         * Because by default a layout does not need to draw,
         * so an optimization is to not call is draw method. By calling setWillNotDraw(
         * false) you tell the UI toolkit that you want to draw
         */
        setWillNotDraw(false); //必须！！！！
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;

        int cCount = getChildCount();

        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;

        // 用于计算左边两个childView的高度
        int lHeight = 0;
        // 用于计算右边两个childView的高度，最终高度取二者之间大值
        int rHeight = 0;

        // 用于计算上边两个childView的宽度
        int tWidth = 0;
        // 用于计算下面两个childiew的宽度，最终宽度取二者之间大值
        int bWidth = 0;

        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */
        for (int i = 0; i < cCount; i++)
        {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            // 上面两个childView
            if (i == 0 || i == 1)
            {
                tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }

            if (i == 2 || i == 3)
            {
                bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }

            if (i == 0 || i == 2)
            {
                lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }

            if (i == 1 || i == 3)
            {
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }

        }

        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }


    /**
     * layout的时候，必须让每个子view都，位于 0,0起始位置，通过设置tanslation来定位
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // layout child

        int left=0;
        int top=0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v != null) {
                v.layout(left, top, v.getWidth(), v.getHeight());
            }
        }

        width =getMeasuredWidth();
        height = getMeasuredHeight();
    }

    private void initVelocityTrackerIfNotExist() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }
    private void recycleVelocityTracker() {
//        if (mVelocityTracker != null) {
//            mVelocityTracker.recycle();
//            mVelocityTracker=null;
//        }
    }

    /**
     * 拦截条件
     * 1、如果是move
     * 2、多指并且有move
     * 3、其他条件super.onInterceptTouchEvent(ev);
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = MotionEventCompat.getActionIndex(ev);
                float y = MotionEventCompat.getY(ev,index);
                float x = MotionEventCompat.getX(ev,index);
                initVelocityTrackerIfNotExist();
                mVelocityTracker.addMovement(ev);
                mLastY = y;
                mLastX = x;
                mActivePointerId = MotionEventCompat.getPointerId(ev,index);
                //分两种情况，一种是初始动作，一个是界面正在滚动，down触摸停止滚动
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                if(mTouchStase==TOUCH_SCROLL||mTouchStase==TOUCH_MULTI){
                    return true;
                }else{
                    mTouchStase=TOUCH_STATE_RESET;
                }

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                index = MotionEventCompat.getActionIndex(ev);
                mSecondaryPointerId = MotionEventCompat.getPointerId(ev,index);
                mSecondaryLastY = MotionEventCompat.getY(ev,index);
                break;

            case MotionEvent.ACTION_MOVE:
                index = MotionEventCompat.findPointerIndex(ev,mActivePointerId);
                y = MotionEventCompat.getY(ev,index);
                x = MotionEventCompat.getX(ev,index);
                final float yDiff  = Math.abs(y-mLastY);
                final float xDiff  = Math.abs(x-mLastX);


                if (yDiff > mTouchSlop||xDiff>mTouchSlop) {
                    //是滚动状态啦
                    mTouchStase = TOUCH_SCROLL;
                    mLastY = y;
                    mLastX = x;
                    initVelocityTrackerIfNotExist();
                    mVelocityTracker.addMovement(ev);


                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }

                break;
            case MotionEvent.ACTION_POINTER_UP:
                index = MotionEventCompat.getActionIndex(ev);
                int curId = MotionEventCompat.getPointerId(ev,index);
                if (curId == mActivePointerId) {
                    mActivePointerId = mSecondaryPointerId;
                    mLastY = mSecondaryLastY;
                    mLastX = mSecondaryLastX;
                    mVelocityTracker.clear();
                } else {
                    mSecondaryPointerId = INVALID_ID;
                    mSecondaryLastY = 0;
                    mSecondaryLastX = 0;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(mTouchStase==TOUCH_SCROLL){
                    //抬起的时候如果之前是滚动状态，记得恢复下overscroller
                    return true;
                }
                mTouchStase=TOUCH_STATE_RESET;
                mActivePointerId = INVALID_ID;
                recycleVelocityTracker();
                break;
            default:
        }
        return mTouchStase!=TOUCH_STATE_RESET;
    }


    /**
     * 1、处理move状态
     * 2、处理缩放状态
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mScroller == null) {
            return false;
        }

        initVelocityTrackerIfNotExist();
        // ScrollView中设置了offsetLocation,这里需要设置吗？
        int action = MotionEventCompat.getActionMasked(event);
        int index = -1;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) { //fling
                    mScroller.abortAnimation();
                }
                index  = MotionEventCompat.getActionIndex(event);
                mActivePointerId = MotionEventCompat.getPointerId(event,index);
                mLastY = MotionEventCompat.getY(event,index);
                mLastX = MotionEventCompat.getX(event,index);
                if(mTouchStase==TOUCH_SCROLL||mTouchStase==TOUCH_MULTI){
                    return true;
                }else{
                    mTouchStase=TOUCH_STATE_RESET;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_ID) {
                    break;
                }
                index = MotionEventCompat.findPointerIndex(event,mActivePointerId);
                if (index == -1) {
                    break;
                }
                float y = MotionEventCompat.getY(event,index);
                float x = MotionEventCompat.getX(event,index);
                float deltaY = mLastY - y;
                float deltaX = mLastX - x;

                mLastX=x;
                mLastY=y;

                if (mTouchStase!=TOUCH_SCROLL && (Math.abs(deltaY) > mTouchSlop||Math.abs(deltaX) > mTouchSlop)) {
                    requestParentDisallowInterceptTouchEvent();
                    mTouchStase=TOUCH_SCROLL;
                    // 减少滑动的距离
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }

                if (mTouchStase==TOUCH_SCROLL) {
                    //直接滑动
                    Log.e("TEST","overscroll"+deltaY+" scrollRange"+getScrollRange()+" overScrollDistance"+mOverScrollDistance);
                    //overScrollBy((int)deltaX,(int)deltaY,getScrollX(),getScrollY(),0,getScrollRange(),0,mOverScrollDistance,true);
                    scrollBy((int)deltaX,(int)deltaY);
                }
                if (mSecondaryPointerId != INVALID_ID) {
                    index = MotionEventCompat.findPointerIndex(event,mSecondaryPointerId);
                    mSecondaryLastY = MotionEventCompat.getY(event,index);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                endDrag();
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchStase==TOUCH_SCROLL) {
                    mVelocityTracker.computeCurrentVelocity(1000,mMaxFlingSpeed);
                    int initialVelocity = (int)mVelocityTracker.getYVelocity(mActivePointerId);
                    int initialVelocitx = (int)mVelocityTracker.getXVelocity(mActivePointerId);
                    Log.e("TEST","velocity"+initialVelocity+" "+mMinFlingSpeed);
                    if (Math.abs(initialVelocity) > mMinFlingSpeed||Math.abs(initialVelocitx) > mMinFlingSpeed) {
                        // fling
//                        doFling(-initialVelocitx,-initialVelocity);
                    }

                }
                endDrag();
                break;
            default:
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(event);
        }
//        if(event.getAction()==MotionEvent.ACTION_DOWN){
//            return super.onTouchEvent(event);
//        }
        return true;
    }

    private void endDrag() {
        mTouchStase = TOUCH_STATE_RESET;
        recycleVelocityTracker();
        mActivePointerId = INVALID_ID;
        mLastY = 0;
        mLastX = 0;
    }
    private void doFling(int speedx,int speedy) {
        if (mScroller == null) {
            return;
        }
        mScroller.fling(getScrollX(),getScrollY(),speedx,speedy,0,0,-500,10000);
        invalidate();
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() >0) {
            int totalHeight = 0;
            if (getChildCount() > 0) {
                for(int i=0;i<getChildCount();i++) {
                    totalHeight += getChildAt(i).getHeight();
                    //先假设没有margin的情况
                }
            }
            scrollRange = Math.max(0,totalHeight-getHeight());
        }
        Log.e("TEST","scrollRange is"+scrollRange);
        return scrollRange;
    }


    private void requestParentDisallowInterceptTouchEvent() {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }
    @Override
    public void onDrop(DragObject dragObject) {
        if(dragObject!=null){
            DragView mDragView= (DragView) dragObject.dragView;
            if(mDragView!=null) {
                if (mDragView.getParent() != null) {
                    ((ViewGroup) mDragView.getParent()).removeView(mDragView);
                }
                //这里需要记录最好放下的位置，方便后面进行view的缩放操作
                if (mDragView.getTag()!=null&&mDragView.getTag() instanceof ItemInfo){
                    int tanslationx=dragObject.x+getScrollX();
                    int tanslationy=dragObject.y+getScrollY();;
                    ((ItemInfo)mDragView.getTag()).x=tanslationx;
                    ((ItemInfo)mDragView.getTag()).y=tanslationy;
                    mDragView.setTranslationX(tanslationx);
                    mDragView.setTranslationY(tanslationy);
                }

                addView(mDragView);

            }
        }
    }


    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        Log.e("TEST","onOverScrolled x"+scrollX+" y"+scrollY);
        if (!mScroller.isFinished()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            scrollTo(scrollX,scrollY);
            onScrollChanged(scrollX,scrollY,oldX,oldY);
            if (clampedY) {
                Log.e("TEST1","springBack");
                mScroller.springBack(getScrollX(),getScrollY(),0,0,0,getScrollRange());
            }
        } else {
            // TouchEvent中的overScroll调用
            super.scrollTo(scrollX,scrollY);
        }
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            int range = getScrollRange();
            if (oldX != x || oldY != y) {
                Log.e("TEST","computeScroll value is"+(y-oldY)+"oldY"+oldY);
//                overScrollBy(x-oldX,y-oldY,oldX,oldY,0,range,0,mOverFlingDistance,false);
                scrollTo(x,y);
            }

        }
    }


    @Override
    public void onDragEnter(DragObject dragObject) {

    }

    @Override
    public void onDragOver(DragObject dragObject) {

    }

    @Override
    public void onDragExit(DragObject dragObject) {

    }

    @Override
    public void getHitRectRelativeToDragLayer(Rect outRect) {
        getGlobalVisibleRect(outRect);
    }
}
