package service.bind.test.draglayout;

import android.app.Application;

import com.free.launcher.wallpaperstore.mxdownload.xutils.Xutils;

public class LauncherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Xutils.Ext.init(this);
        Xutils.Ext.setDebug(false);

    }
}
