package service.bind.test.draglayout.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import service.bind.test.draglayout.drag.DragController;

public class DragLayer extends FrameLayout {
    private DragController mDragController;


    public DragLayer(Context context) {
        super(context);
    }

    public DragLayer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DragLayer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }


    public void setup(DragController controller) {
        mDragController = controller;

    }

    public void clear() {
        mDragController = null;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mDragController.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return mDragController.onInterceptTouchEvent(ev);

    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDragController.onTouchEvent(ev);
    }

    public void removeDragView() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof DragView) {
                removeView(getChildAt(i));
                return;
            }
        }
    }
    /**
     * 返回子控件在父控件上的坐标位置
     *
     * @param child
     * @param loc
     * @return
     */
    public float getLocationInDragLayer(View child, int[] loc) {
        loc[0] = 0;
        loc[1] = 0;
        return getDescendantCoordRelativeToSelf(child, loc);
    }

    /**
     * 返回相对子view的坐标point的相对于父控件的坐标
     *
     * @param descendant
     *            The descendant to which the passed coordinate is relative.
     * @param coord
     *            The coordinate that we want mapped.
     * @return The factor by which this descendant is scaled relative to this
     *         DragLayer. Caution this scale factor is assumed to be equal in X
     *         and Y, and so if at any point this assumption fails, we will need
     *         to return a pair of scale factors.
     */
    public float getDescendantCoordRelativeToSelf(View descendant, int[] coord) {
        float scale = 1.0f;
        float[] pt = { coord[0], coord[1] };
        descendant.getMatrix().mapPoints(pt);
        scale *= descendant.getScaleX();
        pt[0] += descendant.getLeft();
        pt[1] += descendant.getTop();
        ViewParent viewParent = descendant.getParent();
        while (viewParent instanceof View && viewParent != this) {
            final View view = (View) viewParent;
            view.getMatrix().mapPoints(pt);
            scale *= view.getScaleX();
            pt[0] += view.getLeft() - view.getScrollX();
            pt[1] += view.getTop() - view.getScrollY();
            viewParent = view.getParent();
        }
        coord[0] = (int) Math.round(pt[0]);
        coord[1] = (int) Math.round(pt[1]);
        return scale;
    }

    /**
     * 获取view在DragLayer中的占据的矩形，不一定是DragLayer的子控件
     *
     * @param v
     * @param r
     */
    public void getViewRectRelativeToSelf(View v, Rect r) {
        int[] loc = new int[2];
        getLocationInWindow(loc);
        int x = loc[0];
        int y = loc[1];

        v.getLocationInWindow(loc);
        int vX = loc[0];
        int vY = loc[1];

        int left = vX - x;
        int top = vY - y;
        r.set(left, top, left + v.getMeasuredWidth(),
                top + v.getMeasuredHeight());
    }

}
