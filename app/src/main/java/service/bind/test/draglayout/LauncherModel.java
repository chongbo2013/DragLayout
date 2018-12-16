package service.bind.test.draglayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.free.launcher.wallpaperstore.mxdownload.xutils.DbManager;

import java.util.List;

import service.bind.test.draglayout.bean.ItemInfo;

public class LauncherModel extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public interface CallBack {

    }

    public synchronized static void delete(ItemInfo appInfo) {
        if (appInfo == null)
            return;
        try {
            DbManager mDbutils = DbUtils.db;
            mDbutils.delete(appInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static List<ItemInfo> finlAll() {
        DbManager mDbutils = DbUtils.db;
        try {

            List<ItemInfo> appInfos = mDbutils.selector(ItemInfo.class).orderBy("screen").findAll();
            return appInfos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized static void save(ItemInfo appInfo) {
        if (appInfo == null)
            return;
        DbManager mDbutils = DbUtils.db;
        try {
            ItemInfo tempAppInfo = mDbutils.selector(ItemInfo.class).where("id", "=", appInfo.id).findFirst();
            if (tempAppInfo != null) {
                appInfo.id = tempAppInfo.id;
                mDbutils.update(appInfo);
            } else {
                mDbutils.saveBindingId(appInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
