package service.bind.test.draglayout.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import service.bind.test.draglayout.Launcher;
import service.bind.test.draglayout.LauncherConfig;
import service.bind.test.draglayout.bean.ItemInfo;
import service.bind.test.draglayout.drag.DragSource;
import service.bind.test.draglayout.drag.DropTarget;

/**
 * 在workspace
 * Created by ferris.xu on 2016/9/5.
 */
public class WorkSpace extends PagedView implements DropTarget ,DragSource {


    public WorkSpace(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        Launcher.get().getDragController().addDropWorkSpace(this);
    }

    public CellLayout findCurrentCellLayout(){
        return (CellLayout) getChildAt(0);
    }
    private float[] mDragViewVisualCenter = new float[2];
    /**
     * Target drop area calculated during last acceptDrop call.
     */
    private int[] mTargetCell = new int[2];
    @Override
    public void onDrop(DragObject d) {
        mDragViewVisualCenter = getDragViewVisualCenter(d.x, d.y, d.xOffset,
                d.yOffset, d.dragView, mDragViewVisualCenter);

        CellLayout dropTargetLayout = findCurrentCellLayout();

        int spanX = 1;
        int spanY = 1;
        mTargetCell = findNearestArea((int) mDragViewVisualCenter[0],
                (int) mDragViewVisualCenter[1], spanX, spanY,
                dropTargetLayout, mTargetCell);
            View mDragView = (View) d.dragView;
            if (mDragView != null&&mDragView instanceof IWidget) {
                if (mDragView.getParent() != null) {
                    ((ViewGroup) mDragView.getParent()).removeView(mDragView);
                }
                //测试找到最近的位置放下
                IWidget iWidget= (IWidget) mDragView;
                ItemInfo itemInfo=iWidget.getInfo();
                int width = getWidth();
                int height = getHeight();
                int countX=LauncherConfig.COUNTX;
                int countY=LauncherConfig.COUNTY;
                int cellX= (int) (d.x/(float)width*(countX-1));
                int cellY= (int) (d.y/(float)height*(countY-1));
                itemInfo.cellX=cellX;
                itemInfo.cellY=cellY;
                mDragView.setTranslationX(0);
                mDragView.setTranslationY(0);
                findCurrentCellLayout().addView(mDragView);
            }

    }

    // This is used to compute the visual center of the dragView. This point is
    // then
    // used to visualize drop locations and determine where to drop an item. The
    // idea is that
    // the visual center represents the user's interpretation of where the item
    // is, and hence
    // is the appropriate point to use when determining drop location.
    private float[] getDragViewVisualCenter(int x, int y, int xOffset,
                                            int yOffset, IWidget dragView, float[] recycle) {
        float res[];
        if (recycle == null) {
            res = new float[2];
        } else {
            res = recycle;
        }

        // First off, the drag view has been shifted in a way that is not
        // represented in the
        // x and y values or the x/yOffsets. Here we account for that shift.
        x += 0;
        y += 0;

        // These represent the visual top and left of drag view if a dragRect
        // was provided.
        // If a dragRect was not provided, then they correspond to the actual
        // view left and
        // top, as the dragRect is in that case taken to be the entire dragView.
        // R.dimen.dragViewOffsetY.
        int left = x - xOffset;
        int top = y - yOffset;

        // In order to find the visual center, we shift by half the dragRect
        res[0] = left + ((View)dragView).getWidth() / 2;
        res[1] = top + ((View)dragView).getHeight()  / 2;

        return res;
    }

    /**
     * Calculate the nearest cell where the given object would be dropped.
     *
     * pixelX and pixelY should be in the coordinate system of layout
     */
    private int[] findNearestArea(int pixelX, int pixelY, int spanX, int spanY,
                                  CellLayout layout, int[] recycle) {
        return layout.findNearestArea(pixelX, pixelY, spanX, spanY, recycle);
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

    @Override
    public void onDropCompleted(View targetView) {

    }
}
