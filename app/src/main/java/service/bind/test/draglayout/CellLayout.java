package service.bind.test.draglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Administrator on 2016/9/5.
 */
public class CellLayout  extends BaseLayout{
    AnimatorSet set = new AnimatorSet();
    public CellLayout(Context context) {
        super(context);
    }

    public CellLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CellLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void init(){
//        set.playTogether(
//                ObjectAnimator.ofFloat(myView, rotationX, 0, 360),
//                ObjectAnimator.ofFloat(myView, rotationY, 0, 180),
//                ObjectAnimator.ofFloat(myView, rotation, 0, -90),
//                ObjectAnimator.ofFloat(myView, translationX, 0, 90),
//                ObjectAnimator.ofFloat(myView, translationY, 0, 90),
//                ObjectAnimator.ofFloat(myView, scaleX, 1, 1.5f),
//                ObjectAnimator.ofFloat(myView, scaleY, 1, 0.5f),
//                ObjectAnimator.ofFloat(myView, alpha, 1, 0.25f, 1)
//        );
//        set.setDuration(5 * 1000);
    }


    public void setScacle(float v, float v1) {
//        setPivotX(getWidth()/2);
//        setPivotY(getHeight()/2);
//        ViewHelper.setScaleX(this,v);
//        ViewHelper.setScaleX(this,v1);
        setScaleX(v);
        setScaleY(v1);
//        animate().scaleX(v);
//        animate().scaleY(v1);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
