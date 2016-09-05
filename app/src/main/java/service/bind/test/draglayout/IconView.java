package service.bind.test.draglayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by ferris.xu on 2016/9/5.
 */
public class IconView extends LinearLayout implements DragSource{
    public IconView(Context context) {
        super(context);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 创建一个快捷图标
     * @return
     */
    public DragView createDragView(Rect r){
        Bitmap mBitmap=createBitmap();
        if(mBitmap!=null) {
            DragView mDragView = new DragView(getContext());
            mDragView.setImageBitmap(mBitmap);
            FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            lp.leftMargin=r.left;
//            lp.topMargin=r.top;
            mDragView.setLayoutParams(lp);
            return mDragView;
        }

        return  null;
    }
    private Bitmap createBitmap(){
        clearFocus();
        setPressed(false);
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        draw(new Canvas(bitmap));
        return bitmap;
    }

    @Override
    public void onDropCompleted(View targetView) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                scaleToSmaller(this);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                scaleToNormal(this);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
    private void scaleToSmaller(View v)
    {
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_smaller_anim);
        v.clearAnimation();
        v.startAnimation(loadAnimation);
    }
    private void scaleToNormal(View v)
    {
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_normal_anim);
        v.clearAnimation();
        v.startAnimation(loadAnimation);
    }
}
