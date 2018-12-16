package service.bind.test.draglayout.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import service.bind.test.draglayout.Launcher;
import service.bind.test.draglayout.bean.ItemInfo;
import service.bind.test.draglayout.drag.DropTarget;

/**
 * 在workspace
 * Created by ferris.xu on 2016/9/5.
 */
public class WorkSpace extends PagedView implements DropTarget {

    CellLayout mCellLayout;
    public WorkSpace(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        Launcher.get().getDragController().addDropWorkSpace(this);
        mCellLayout = new CellLayout(getContext());
        addView(mCellLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }


    @Override
    public void onDrop(DragObject dragObject) {
        if (dragObject != null) {
            DragView mDragView = (DragView) dragObject.dragView;
            if (mDragView != null) {
                if (mDragView.getParent() != null) {
                    ((ViewGroup) mDragView.getParent()).removeView(mDragView);
                }
                //这里需要记录最好放下的位置，方便后面进行view的缩放操作
                if (mDragView.getTag() != null && mDragView.getTag() instanceof ItemInfo) {
                    float tanslationx = (dragObject.x +getScrollX()-(1-mCellLayout.getScaleX())*mCellLayout.getWidth()/2)/mCellLayout.getScaleX();
                    float tanslationy = (dragObject.y +getScrollY()-(1-mCellLayout.getScaleY())*mCellLayout.getHeight()/2)/mCellLayout.getScaleY();

                    ((ItemInfo) mDragView.getTag()).x = (int) tanslationx;
                    ((ItemInfo) mDragView.getTag()).y = (int) tanslationy;
                    mDragView.setTranslationX(tanslationx);
                    mDragView.setTranslationY(tanslationy);
                }

                mCellLayout.addView(mDragView);

            }
        }
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
