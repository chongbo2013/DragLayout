package service.bind.test.draglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/9/5.
 */
public class DragView extends ImageView implements IDragView,DragSource{
    public DragView(Context context) {
        super(context);
        init();
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        setLongClickable(true);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.get().getDragController().startDragNotCreate(DragView.this,DragView.this);
                return true;
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }


    @Override
    public void move(int x, int y) {
        setTranslationX(x);
        setTranslationY(y);
    }

    @Override
    public void onDropCompleted(View targetView) {

    }

}
