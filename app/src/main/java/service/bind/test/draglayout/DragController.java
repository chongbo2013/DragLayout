
package service.bind.test.draglayout;


import android.graphics.Rect;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class DragController {

    private MainActivity mLauncher;

    private boolean mDragging;

    private int mMotionDownX;

    private int mMotionDownY;

    private int mOffsetX;

    private int mOffsetY;

    private int mMidOffsetX;

    private int mMidOffsetY;

    private Rect mRectTemp = new Rect();

    private DropTarget.DragObject mDragObject;

    private ArrayList<DropTarget> mDropWorkSpace = new ArrayList<DropTarget>();



    private ArrayList<DragListener> mListeners = new ArrayList<DragListener>();

    private ArrayList<DropTarget> mDropTargets;

    private DropTarget mLastDropTarget;

    public DragController(MainActivity launcher) {
        mLauncher = launcher;
    }

    public void setLauncher(MainActivity launcher){
        this.mLauncher=launcher;
    }
    public void addDropWorkSpace(DropTarget target) {
        mDropWorkSpace.add(target);
    }

    public void removeDropWorkSpace(DropTarget target) {
        mDropWorkSpace.remove(target);
    }



    public DropTarget.DragObject getCurrentDragObject() {
        return mDragObject;
    }

    /**
     * Interface to receive notifications when a drag starts or stops
     */
    interface DragListener {

        void onDragStart(DragSource source, View mView);

        /**
         * The drag has ended
         */
        void onDragEnd();
    }


    /**
     * 从icon里面创建一个拖动的图标，放到拖动层里面
     * @param source
     * @param seleteview
     */
    public void startDrag(DragSource source, IconView seleteview) {
        mDragging = true;
        mDropTargets = mDropWorkSpace;

        // get a offset rectangle of workspace.
        Rect r = new Rect();
        mLauncher.getToolBar().getGlobalVisibleRect(r);
        r.offset(seleteview.getLeft(),seleteview.getTop());

        DragView mDragView=seleteview.createDragView(r);
        mDragView.setTag(seleteview.getTag());

        mDragObject = new DropTarget.DragObject();
        mDragObject.dragView = mDragView;
        mLauncher.getDragLayer().addView(mDragView);
        mDragObject.dragSource = source;
        mLauncher.getDragLayer().performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        mOffsetX =seleteview.getWidth()/2;
        mOffsetY = seleteview.getHeight()/2;
        mMidOffsetX = 0;
        mMidOffsetY = 0;
        mDragObject.dragView.move(r.left,r.top);
        for (DragListener listener : mListeners) {
            listener.onDragStart(source, seleteview);
        }
    }

    /**
     * 直接在拖动层里拖动，自己
     * @param source
     * @param mDragView
     */
    public void startDragNotCreate(DragSource source, DragView mDragView) {
        mDragging = true;
        mDropTargets = mDropWorkSpace;

        // get a offset rectangle of workspace.
        Rect r = new Rect();
        mDragView.getGlobalVisibleRect(r);


        mDragObject = new DropTarget.DragObject();
        mDragObject.dragView = mDragView;

        if(mDragView.getParent()!=null) {
            ((ViewGroup)mDragView.getParent()).removeView(mDragView);
        }
        mLauncher.getDragLayer().addView(mDragView);
        mLauncher.getDragLayer().bringChildToFront(mDragView);
        mDragObject.dragSource = source;
        mLauncher.getDragLayer().performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        mOffsetX =mDragView.getWidth()/2;
        mOffsetY = mDragView.getHeight()/2;
        mMidOffsetX = 0;
        mMidOffsetY = 0;
//        mDragObject.dragView.move(0,0);
        for (DragListener listener : mListeners) {
            listener.onDragStart(source, mDragView);
        }
    }



    public boolean isDragging() {
        return mDragging;
    }

    private void endDrag() {
        if (mDragging) {
            mDragging = false;
            // Only end the drag if we are not deferred
                for (DragListener listener : mListeners) {
                    listener.onDragEnd();
                }

        }
    }

    /**
     * Sets the drag listner which will be notified when a drag starts or ends.
     */
    public void addDragListener(DragListener l) {
        mListeners.add(l);
    }

    /**
     * Remove a previously installed drag listener.
     */
    public void removeDragListener(DragListener l) {
        mListeners.remove(l);
    }

    public void cancelDrag() {
        if (mDragging) {
            if (mLastDropTarget != null) {
                mLastDropTarget.onDragExit(mDragObject);
            }
            mDragObject.dragSource.onDropCompleted(null);
        }
        endDrag();
    }
    //262 801
    private DropTarget findDropTarget(int x, int y) {
        final Rect r = mRectTemp;
        final ArrayList<DropTarget> dropTargets = mDropTargets;
        final int count = dropTargets.size();
        for (int i = 0; i < count; i++) {
            DropTarget target = dropTargets.get(i);
            target.getHitRectRelativeToDragLayer(r);

            if (r.contains(x, y)) {
                return target;
            }
        }
        return null;
    }

    private void drop(int x, int y) {
        final DropTarget dropTarget = findDropTarget(x - mMidOffsetX, y - mMidOffsetY);

        if (dropTarget != null) {
            mDragObject.x = x - mMidOffsetX;
            mDragObject.y = y - mMidOffsetY;
            dropTarget.onDrop(mDragObject);
        }
        mDragObject.dragSource.onDropCompleted((View) dropTarget);
    }

    private int[] getClampedDragLayerPos(float x, float y) {
        int mTmpPoint[] = new int[2];
        Rect mDragLayerRect = new Rect();
        mLauncher.getDragLayer().getLocalVisibleRect(mDragLayerRect);
        mTmpPoint[0] = (int) Math.max(mDragLayerRect.left, Math.min(x, mDragLayerRect.right - 1));
        mTmpPoint[1] = (int) Math.max(mDragLayerRect.top, Math.min(y, mDragLayerRect.bottom - 1));
        return mTmpPoint;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final int[] dragLayerPos = getClampedDragLayerPos(ev.getX(), ev.getY());
        final int dragLayerX = dragLayerPos[0];
        final int dragLayerY = dragLayerPos[1];

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mMotionDownX = dragLayerX;
                mMotionDownY = dragLayerY;
                mLastDropTarget = null;
                break;
            case MotionEvent.ACTION_UP:
                if (mDragging) {
                    drop(dragLayerX, dragLayerY);
                }
                endDrag();
                break;
            case MotionEvent.ACTION_CANCEL:
                cancelDrag();
                break;
        }

        return mDragging;
    }

    private void checkTouchMove(DropTarget dropTarget) {
        if (dropTarget != null) {
            if (mLastDropTarget != dropTarget) {
                if (mLastDropTarget != null) {
                    mLastDropTarget.onDragExit(mDragObject);
                }
                dropTarget.onDragEnter(mDragObject);
            }
            dropTarget.onDragOver(mDragObject);
        } else {
            if (mLastDropTarget != null) {
                mLastDropTarget.onDragExit(mDragObject);
            }
        }
        mLastDropTarget = dropTarget;
    }

    private void handleMoveEvent(int x, int y) {
        mDragObject.dragView.move(x - mOffsetX, y - mOffsetY);

        DropTarget dropTarget = findDropTarget(x - mMidOffsetX, y - mMidOffsetY);
        mDragObject.x = x - mMidOffsetX;
        mDragObject.y = y - mMidOffsetY;
        checkTouchMove(dropTarget);

    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!mDragging) {
            return false;
        }

        final int action = ev.getAction();
        final int[] dragLayerPos = getClampedDragLayerPos(ev.getX(), ev.getY());
        final int dragLayerX = dragLayerPos[0];
        final int dragLayerY = dragLayerPos[1];

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Remember where the motion event started
                mMotionDownX = dragLayerX;
                mMotionDownY = dragLayerY;
                handleMoveEvent(dragLayerX, dragLayerY);
                break;
            case MotionEvent.ACTION_MOVE:
                handleMoveEvent(dragLayerX, dragLayerY);
                break;
            case MotionEvent.ACTION_UP:
                // Ensure that we've processed a move event at the current
                // pointer location.
                handleMoveEvent(dragLayerX, dragLayerY);
                if (mDragging) {
                    drop(dragLayerX, dragLayerY);
                }
                endDrag();
                break;
            case MotionEvent.ACTION_CANCEL:
                cancelDrag();
                break;
        }

        return true;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return mDragging;
    }

}
