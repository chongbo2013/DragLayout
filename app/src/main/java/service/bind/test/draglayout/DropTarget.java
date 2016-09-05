
package service.bind.test.draglayout;


import android.graphics.Rect;

public interface DropTarget {

    public class DragObject {
        public int x = -1;
        public int y = -1;
        public IDragView dragView = null;

        public DragSource dragSource = null;

        public DragObject() {
        }
    }

    public void onDrop(DragObject dragObject);

    public  void onDragEnter(DragObject dragObject);

    public  void onDragOver(DragObject dragObject);

    public void onDragExit(DragObject dragObject);

    public void getHitRectRelativeToDragLayer(Rect outRect);

}
