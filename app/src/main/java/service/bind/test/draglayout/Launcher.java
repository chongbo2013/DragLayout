package service.bind.test.draglayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import service.bind.test.draglayout.drag.DragController;
import service.bind.test.draglayout.widget.DragLayer;
import service.bind.test.draglayout.widget.HotseatLayout;
import service.bind.test.draglayout.widget.WorkSpace;

//https://github.com/NashLegend/Launcher/tree/master/src/com/android/launcher2
//参考Launcher2
public class Launcher extends Activity implements LauncherModel.CallBack {
    private static Launcher mActivity;

    public static Launcher get(){
        return mActivity;
    }

    HotseatLayout mBottomToolbar;
    DragLayer mDragLayer;
    DragController mDragController;
    WorkSpace mWorkSpace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=this;
        mDragController=new DragController(this);
        setContentView(R.layout.activity_main);
        mDragLayer= (DragLayer) findViewById(R.id.mDragLayer);
        mDragLayer.setup(mDragController);
        mWorkSpace= (WorkSpace) findViewById(R.id.mWorkspace);
        mBottomToolbar= (HotseatLayout) findViewById(R.id.toolbar);
    }

    public View getToolBar() {
        return mBottomToolbar;
    }
    public DragController getDragController(){
        return mDragController;
    }

    public ViewGroup getDragLayer() {
        return mDragLayer;
    }

    public WorkSpace getWorkspace(){
        return mWorkSpace;
    }
}
