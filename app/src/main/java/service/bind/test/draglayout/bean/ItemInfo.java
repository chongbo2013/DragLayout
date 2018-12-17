package service.bind.test.draglayout.bean;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.free.launcher.wallpaperstore.mxdownload.xutils.db.annotation.Column;
import com.free.launcher.wallpaperstore.mxdownload.xutils.db.annotation.Table;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by 图标信息 on 2016/9/5.
 */
@Table(name = "ItemInfo")
public class ItemInfo {
    static final int NO_ID = -1;

    //数据库ID
    @Column(name = "id", isId = true)
    public int id;
    //图标唯一标识
    @Column(name = "title")
    public String title;
    //图标
    public int icon;

    /**
     * 类型，有可能是应用、快捷方式、文件夹、插件
     * {@link LauncherSettings.Favorites#ITEM_TYPE_APPLICATION},
     * {@link LauncherSettings.Favorites#ITEM_TYPE_SHORTCUT},
     * {@link LauncherSettings.Favorites#ITEM_TYPE_FOLDER}, or
     * {@link LauncherSettings.Favorites#ITEM_TYPE_APPWIDGET}.
     */
    public int itemType;

    /**
     * 放这个item的窗口，对于桌面来说，这个是
     * {@link LauncherSettings.Favorites#CONTAINER_DESKTOP}
     *
     * 对于“所有程序”界面来说是 {@link #NO_ID} (因为不用保存在数据库里)
     *
     * 对于用户文件夹来说是文件夹id
     */
    public long container = NO_ID;

    /**
     * 所在屏幕index
     */
    public int screen = -1;

    /**
     * 在屏幕上的x位置
     */
    public int cellX = -1;

    /**
     * 在屏幕上的y位置
     */
    public int cellY = -1;

    /**
     * 宽度
     */
    public int spanX = 1;

    /**
     * 高度
     */
    public int spanY = 1;

    /**
     * 最小宽度
     */
    public int minSpanX = 1;

    /**
     * 最小高度
     */
    public int minSpanY = 1;





    /**
     * 在拖放操作中的位置
     */
    public  int[] dropPos = null;

    /**
     * 返回包名，不存在则为空
     */
    public static String getPackageName(Intent intent) {
        if (intent != null) {
            String packageName = intent.getPackage();
            if (packageName == null && intent.getComponent() != null) {
                packageName = intent.getComponent().getPackageName();
            }
            if (packageName != null) {
                return packageName;
            }
        }
        return "";
    }
    static byte[] flattenBitmap(Bitmap bitmap) {
        // 把图片转换成byte[]
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            Log.w("Favorite", "Could not write icon");
            return null;
        }
    }
    @Override
    public String toString() {
        return "Item(id=" + this.id + " type=" + this.itemType + " container="
                + this.container + " screen=" + screen + " cellX=" + cellX
                + " cellY=" + cellY + " spanX=" + spanX + " spanY=" + spanY
                + " dropPos=" + dropPos + ")";
    }

}
