package service.bind.test.draglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by ferri.sxu on 2016/9/5.
 */
public class BottomToolbar extends LinearLayout implements View.OnLongClickListener{

    public BottomToolbar(Context context) {
        super(context);
    }

    public BottomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for(int i=0;i<getChildCount();i++){
            ItemInfo mInfo=new ItemInfo();
            mInfo.dragIcon=R.drawable.icon_door;
            mInfo.key=i;
            getChildAt(i).setTag(mInfo);
            getChildAt(i).setOnLongClickListener(this);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(v instanceof IconView) {
            IconView iconView= (IconView) v;
            MainActivity.get().getDragController().startDrag(iconView,iconView);
        }
        return true;
    }
}
