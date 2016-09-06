package service.bind.test.draglayout;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.OverScroller;

/**
 * 在workspace里面去判别，移动和，缩放2个操作
 * Created by ferris.xu on 2016/9/5.
 */
public class WorkSpace extends BaseLayout implements DropTarget {
    private final static int INVALID_ID = -1;
    private int mActivePointerId = INVALID_ID;
    private float mLastY = 0;
    private float mLastX = 0;

    private int mSecondaryPointerId = INVALID_ID;
    private float mSecondaryLastX = 0;
    private float mSecondaryLastY = 0;


    private int mTouchSlop;
    private int mMinFlingSpeed;
    private int mMaxFlingSpeed;
    private int mOverFlingDistance;
    private int mOverScrollDistance;
    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;

    int width = 0, height = 0;
    //触摸主要要3种状态
    private final static int TOUCH_SCROLL = 1;
    private final static int TOUCH_MULTI = 2;
    private final static int TOUCH_STATE_RESET = 3;
    private int mTouchStase = TOUCH_STATE_RESET;
    /**
     * 缩放开始时的手指间距
     */
    private float mStartDis;
    //界面缩放值
    //用来作为判断双指缩放参数
    private float scale = 1.0f;

    CellLayout mCellLayout;

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

    public void init() {
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

        // 将celllayout做为唯一子View 给Workspace做缩放

        mCellLayout = new CellLayout(getContext());
        addView(mCellLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isTouchMui=false;
                int index = MotionEventCompat.getActionIndex(ev);
                float y = MotionEventCompat.getY(ev, index);
                float x = MotionEventCompat.getX(ev, index);
                initVelocityTrackerIfNotExist();
                mVelocityTracker.addMovement(ev);
                mLastY = y;
                mLastX = x;
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                //分两种情况，一种是初始动作，一个是界面正在滚动，down触摸停止滚动
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                if (mTouchStase == TOUCH_SCROLL || mTouchStase == TOUCH_MULTI) {
                    return true;
                } else {
                    mTouchStase = TOUCH_STATE_RESET;
                }

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                index = MotionEventCompat.getActionIndex(ev);
                mSecondaryPointerId = MotionEventCompat.getPointerId(ev, index);
                mSecondaryLastY = MotionEventCompat.getY(ev, index);
                mSecondaryLastX = MotionEventCompat.getX(ev, index);
                mStartDis = distance(ev);
                isTouchMui=true;
                break;

            case MotionEvent.ACTION_MOVE:

                index = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                y = MotionEventCompat.getY(ev, index);
                x = MotionEventCompat.getX(ev, index);
                final float yDiff = Math.abs(y - mLastY);
                final float xDiff = Math.abs(x - mLastX);


                if (yDiff > mTouchSlop || xDiff > mTouchSlop) {

                    //如果判别到又第二根手指撸下来，就拦截了
                    if (mSecondaryPointerId != INVALID_ID) {
                        mTouchStase = TOUCH_MULTI;
                    } else {
                        //是滚动状态啦
                        mTouchStase = TOUCH_SCROLL;

                        initVelocityTrackerIfNotExist();
                        mVelocityTracker.addMovement(ev);


                        final ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }


                    mLastY = y;
                    mLastX = x;
                }

                break;
            case MotionEvent.ACTION_POINTER_UP:
                index = MotionEventCompat.getActionIndex(ev);
                int curId = MotionEventCompat.getPointerId(ev, index);
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
                mStartDis = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mTouchStase == TOUCH_SCROLL || mTouchStase == TOUCH_MULTI) {
                    //抬起的时候如果之前是滚动状态，记得恢复下overscroller
                    return true;
                }
                mTouchStase = TOUCH_STATE_RESET;
                mActivePointerId = INVALID_ID;
                recycleVelocityTracker();
                break;
            default:
        }
        return mTouchStase != TOUCH_STATE_RESET;
    }


    boolean isTouchMui=false;
    /**
     * 1、处理move状态
     * 2、处理缩放状态
     *
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
                isTouchMui=false;
                if (!mScroller.isFinished()) { //fling
                    mScroller.abortAnimation();
                }
                index = MotionEventCompat.getActionIndex(event);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                mLastY = MotionEventCompat.getY(event, index);
                mLastX = MotionEventCompat.getX(event, index);
                if (mTouchStase == TOUCH_SCROLL || mTouchStase == TOUCH_MULTI) {
                    return true;
                } else {
                    mTouchStase = TOUCH_STATE_RESET;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                index = MotionEventCompat.getActionIndex(event);
                mSecondaryPointerId = MotionEventCompat.getPointerId(event, index);
                mSecondaryLastY = MotionEventCompat.getY(event, index);
                mSecondaryLastX = MotionEventCompat.getX(event, index);
                mStartDis = distance(event);
                isTouchMui=true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                index = MotionEventCompat.getActionIndex(event);
                int curId = MotionEventCompat.getPointerId(event, index);
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
                mStartDis = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_ID) {
                    break;
                }
                index = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                if (index == -1) {
                    break;
                }
                float y = MotionEventCompat.getY(event, index);
                float x = MotionEventCompat.getX(event, index);
                float deltaY = mLastY - y;
                float deltaX = mLastX - x;

                mLastX = x;
                mLastY = y;
                //真正处理双手指缩放
                int count = event.getPointerCount();
                //如果可以移动
                if (Math.abs(deltaY) > mTouchSlop || Math.abs(deltaX) > mTouchSlop) {

                    if ( mTouchStase != TOUCH_SCROLL) {
                        requestParentDisallowInterceptTouchEvent();
                        mTouchStase = TOUCH_SCROLL;
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
                }

                if (  count == 2&&mSecondaryPointerId != INVALID_ID && mTouchStase != TOUCH_MULTI) {
                    requestParentDisallowInterceptTouchEvent();
                    mTouchStase = TOUCH_MULTI;
                }

                if (!isTouchMui&&mTouchStase == TOUCH_SCROLL&& count == 1 ) {
                    //直接滑动
                    Log.e("TEST", "overscroll" + deltaY + " scrollRange" + getScrollRange() + " overScrollDistance" + mOverScrollDistance);
                    //overScrollBy((int)deltaX,(int)deltaY,getScrollX(),getScrollY(),0,getScrollRange(),0,mOverScrollDistance,true);
                    scrollBy((int) deltaX, (int) deltaY);
                }else if (mTouchStase == TOUCH_MULTI && count == 2) {
                    //保持比例缩放
                    float endDis = distance(event);// 结束距离
                    if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        float scale = endDis / mStartDis;// 得到缩放倍数
                        float detalScale=(endDis-mStartDis)/endDis;
                        float oldScaleX=mCellLayout.getScaleX();
                        float oldScaleY=mCellLayout.getScaleX();
                        //向外放大
                        mCellLayout.setScacle(oldScaleX+detalScale,oldScaleY+ detalScale);
                        mStartDis = endDis;//重置距离
                    }
                }

                if (mSecondaryPointerId != INVALID_ID) {
                    index = MotionEventCompat.findPointerIndex(event, mSecondaryPointerId);
                    if (index != -1) {
                        mSecondaryLastY = MotionEventCompat.getY(event, index);
                        mSecondaryLastX = MotionEventCompat.getX(event, index);
                    }

                }


                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mTouchStase == TOUCH_SCROLL) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingSpeed);
                    int initialVelocity = (int) mVelocityTracker.getYVelocity(mActivePointerId);
                    int initialVelocitx = (int) mVelocityTracker.getXVelocity(mActivePointerId);
                    Log.e("TEST", "velocity" + initialVelocity + " " + mMinFlingSpeed);
                    if (Math.abs(initialVelocity) > mMinFlingSpeed || Math.abs(initialVelocitx) > mMinFlingSpeed) {
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
        Log.d("scrollXY","scrollx="+getScrollX()+",scrolly="+getScrollY());
        return true;
    }


    /**
     * 计算两个手指间的距离
     *
     * @param event
     * @return
     */
    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        /** 使用勾股定理返回两点之间的距离 */
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private void endDrag() {
        mTouchStase = TOUCH_STATE_RESET;
        recycleVelocityTracker();
        mActivePointerId = INVALID_ID;
        mLastY = 0;
        mLastX = 0;
        mStartDis = 0;
    }

    private void doFling(int speedx, int speedy) {
        if (mScroller == null) {
            return;
        }
        mScroller.fling(getScrollX(), getScrollY(), speedx, speedy, 0, 0, -500, 10000);
        invalidate();
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            int totalHeight = 0;
            if (getChildCount() > 0) {
                for (int i = 0; i < getChildCount(); i++) {
                    totalHeight += getChildAt(i).getHeight();
                    //先假设没有margin的情况
                }
            }
            scrollRange = Math.max(0, totalHeight - getHeight());
        }
        Log.e("TEST", "scrollRange is" + scrollRange);
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
        if (dragObject != null) {
            DragView mDragView = (DragView) dragObject.dragView;
            if (mDragView != null) {
                if (mDragView.getParent() != null) {
                    ((ViewGroup) mDragView.getParent()).removeView(mDragView);
                }
                //这里需要记录最好放下的位置，方便后面进行view的缩放操作
                if (mDragView.getTag() != null && mDragView.getTag() instanceof ItemInfo) {
                    float tanslationx = (dragObject.x +getScrollX()-(1-mCellLayout.getScaleX())*mCellLayout.getWidth()/2)/mCellLayout.getScaleX();
                    float tanslationy = (dragObject.y +getScrollY()-(1-mCellLayout.getScaleY())*mCellLayout.getHeight()/2)/mCellLayout.getScaleY();

                    ((ItemInfo) mDragView.getTag()).x = (int) tanslationx;
                    ((ItemInfo) mDragView.getTag()).y = (int) tanslationy;

                    mDragView.setTranslationX(tanslationx);
                    mDragView.setTranslationY(tanslationy);
                }

                mCellLayout.addView(mDragView);

            }
        }
    }


    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        Log.e("TEST", "onOverScrolled x" + scrollX + " y" + scrollY);
        if (!mScroller.isFinished()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            scrollTo(scrollX, scrollY);
            onScrollChanged(scrollX, scrollY, oldX, oldY);
            if (clampedY) {
                Log.e("TEST1", "springBack");
                mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange());
            }
        } else {
            // TouchEvent中的overScroll调用
            super.scrollTo(scrollX, scrollY);
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
                Log.e("TEST", "computeScroll value is" + (y - oldY) + "oldY" + oldY);
//                overScrollBy(x-oldX,y-oldY,oldX,oldY,0,range,0,mOverFlingDistance,false);
                scrollTo(x, y);
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
