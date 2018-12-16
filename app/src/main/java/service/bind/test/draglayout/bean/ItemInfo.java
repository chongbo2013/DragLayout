package service.bind.test.draglayout.bean;

import com.free.launcher.wallpaperstore.mxdownload.xutils.db.annotation.Column;
import com.free.launcher.wallpaperstore.mxdownload.xutils.db.annotation.Table;

/**
 * Created by 图标信息 on 2016/9/5.
 */
@Table(name = "ItemInfo")
public class ItemInfo {
    //数据库ID
    @Column(name = "id", isId = true)
    public int id;
    //图标唯一标识
    public int key;
    //标题

    @Column(name = "title")
    public String title;
    //图标
    public int icon;
    //拖拽到拖动层显示图片
    public int dragIcon;
    //缩放值
    public float scacle=1f;
    //位置
    public int x=0;
    public int y=0;

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
