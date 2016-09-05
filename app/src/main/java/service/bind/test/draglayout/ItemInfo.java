package service.bind.test.draglayout;

/**
 * Created by 图标信息 on 2016/9/5.
 */
public class ItemInfo {
    //数据库ID
    int id;
    //图标唯一标识
    int key;
    //标题
    String title;
    //图标
    int icon;
    //拖拽到拖动层显示图片
    int dragIcon;
    //缩放值
    float scacle=1f;
    //位置
    int x=0;
    int y=0;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getDragIcon() {
        return dragIcon;
    }

    public void setDragIcon(int dragIcon) {
        this.dragIcon = dragIcon;
    }

    public float getScacle() {
        return scacle;
    }

    public void setScacle(float scacle) {
        this.scacle = scacle;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
