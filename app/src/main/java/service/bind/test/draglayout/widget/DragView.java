package service.bind.test.draglayout.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import service.bind.test.draglayout.Launcher;
import service.bind.test.draglayout.bean.ItemInfo;
import service.bind.test.draglayout.drag.DragSource;
import service.bind.test.draglayout.drag.IDragView;

/**
 * Created by Administrator on 2016/9/5.
 */
public class DragView extends AppCompatImageView implements IDragView,IWidget {
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
    public ItemInfo getInfo() {
        return info;
    }
    ItemInfo info;
    @Override
    public void setInfo(ItemInfo info) {
        this.info=info;
    }
}
