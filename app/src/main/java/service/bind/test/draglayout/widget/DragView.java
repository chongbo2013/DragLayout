package service.bind.test.draglayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import service.bind.test.draglayout.Launcher;
import service.bind.test.draglayout.drag.DragSource;
import service.bind.test.draglayout.drag.IDragView;

/**
 * Created by Administrator on 2016/9/5.
 */
public class DragView extends ImageView implements IDragView,DragSource {
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
                Launcher.get().getDragController().startDragNotCreate(DragView.this,DragView.this);
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
