package service.bind.test.draglayout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 在workspace里面去判别，移动和，缩放2个操作
 * Created by ferris.xu on 2016/9/5.
 */
public class WorkSpace extends FrameLayout implements DropTarget{
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
    }

    @Override
    public void onDrop(DragObject dragObject) {
        if(dragObject!=null){
            DragView mDragView= (DragView) dragObject.dragView;
            if(mDragView.getParent()!=null){
                ((ViewGroup)mDragView.getParent()).removeView(mDragView);
            }
            addView(mDragView);
        }
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
        return super.onInterceptTouchEvent(ev);
    }


    /**
     * 1、处理move状态
     * 2、处理缩放状态
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
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
