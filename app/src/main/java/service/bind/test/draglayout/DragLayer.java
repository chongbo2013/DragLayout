package service.bind.test.draglayout;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;

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

    //    public static Rect testRect=new Rect();
    @Override
    protected void dispatchDraw(Canvas canvas) {
//        if(testRect!=null){
//            canvas.setDrawColor(Color.BLUE);
//            canvas.drawRect(testRect.left,testRect.top,testRect.right,testRect.bottom);
//        }
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


}
