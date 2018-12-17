
package service.bind.test.draglayout.drag;


import android.graphics.Rect;

import service.bind.test.draglayout.widget.IWidget;

public interface DropTarget {

    public class DragObject {
        public int x = -1;
        public int y = -1;
        /**
         * X offset from the upper-left corner of the cell to where we touched.
         * 触摸点到cell右上角的x位置
         */
        public int xOffset = -1;

        /**
         * Y offset from the upper-left corner of the cell to where we touched.
         * 触摸点到cell右上角的y位置
         */
        public int yOffset = -1;

        /**
         * This indicates whether a drag is in final stages, either drop or
         * cancel. It differentiates onDragExit, since this is called when the
         * drag is ending, above the current drag target, or when the drag moves
         * off the current drag object. 表示一个拖动操作是否在最后的状态，放下或者取消。和onDragExit不同，
         * 这个只有在当前拖动对象上拖动终结时或者拖动出了当前对象时才会产生？
         */
        public boolean dragComplete = false;

        //拖动哪个view
        public IWidget dragView = null;
        //从哪里开始拖出来的
        public DragSource dragSource = null;

        /** 表示是否拖动操作已经被取消 */
        public boolean cancelled = false;

        public DragObject() {
        }
    }

    public void onDrop(DragObject dragObject);

    public  void onDragEnter(DragObject dragObject);

    public  void onDragOver(DragObject dragObject);

    public void onDragExit(DragObject dragObject);

    public void getHitRectRelativeToDragLayer(Rect outRect);

}
