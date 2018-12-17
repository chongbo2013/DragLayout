
package service.bind.test.draglayout.drag;


import android.graphics.Rect;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import service.bind.test.draglayout.Launcher;
import service.bind.test.draglayout.widget.DragView;
import service.bind.test.draglayout.widget.IconView;

public class DragController {

    private Launcher mLauncher;

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

    public DragController(Launcher launcher) {
        mLauncher = launcher;
    }

    public void setLauncher(Launcher launcher){
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

    private final int[] mCoordinatesTemp = new int[2];
    /**
     * 从icon里面创建一个拖动的图标，放到拖动层里面
     * @param v
     */
    public void startDrag( DragSource source,IconView v) {
        mDragging = true;
        mDropTargets = mDropWorkSpace;

        // get a offset rectangle of workspace.
        int[] loc = mCoordinatesTemp;
        mLauncher.getDragLayer().getLocationInDragLayer(v, loc);

        int dragLayerX = loc[0];
        int dragLayerY = loc[1];
        int width=v.getWidth();
        int height=v.getHeight();

        if(v.getParent()!=null) {
            ((ViewGroup)v.getParent()).removeView(v);
        }

        mDragObject = new DropTarget.DragObject();
        mDragObject.dragView = v;
        mLauncher.getDragLayer().addView(v,width,height);
        mDragObject.dragSource = source;
        mLauncher.getDragLayer().performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        mOffsetX =mMotionDownX -dragLayerX;
        mOffsetY =mMotionDownY -dragLayerY;
        mMidOffsetX = width/2;
        mMidOffsetY = height/2;
        move(v,dragLayerX,dragLayerY);
        for (DragListener listener : mListeners) {
            listener.onDragStart(source, v);
        }
    }
    public void move(View view,int x, int y) {
        view.setTranslationX(x);
        view.setTranslationY(y);
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
        move((View)mDragObject.dragView,x - mOffsetX, y - mOffsetY);
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
