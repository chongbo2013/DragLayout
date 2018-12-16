package service.bind.test.draglayout;

import com.free.launcher.wallpaperstore.mxdownload.xutils.DbManager;
import com.free.launcher.wallpaperstore.mxdownload.xutils.Xutils;

public class DbUtils {
    static   DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("launcher.db")
            .setDbVersion(2).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                }

            });;
    public static DbManager db = Xutils.getDb(daoConfig);
}
