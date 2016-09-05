package service.bind.test.draglayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/9/5.
 */
public class BaseLayout extends ViewGroup {
    public BaseLayout(Context context) {
        super(context);
    }

    public BaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(sizeWidth,
                sizeHeight);
    }


    /**
     * layout的时候，必须让每个子view都，位于 0,0起始位置，通过设置tanslation来定位
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // layout child

        int left = 0;
        int top = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v != null) {
                v.layout(left, top, v.getMeasuredWidth(), v.getMeasuredHeight());
            }
        }


    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        int count=getChildCount();
        long drawtime=getDrawingTime();
        if(count>0){
            canvas.save();
            for(int i=0;i<count;i++){

                drawChild(canvas,getChildAt(i),drawtime);

            }
            canvas.restore();
        }else{
            super.dispatchDraw(canvas);
        }

    }
}
