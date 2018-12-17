package service.bind.test.draglayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import service.bind.test.draglayout.bean.ItemInfo;
import service.bind.test.draglayout.drag.DragController;
import service.bind.test.draglayout.widget.CellLayout;
import service.bind.test.draglayout.widget.DragLayer;
import service.bind.test.draglayout.widget.HotseatLayout;
import service.bind.test.draglayout.widget.IconView;
import service.bind.test.draglayout.widget.WorkSpace;

//https://github.com/NashLegend/Launcher/tree/master/src/com/android/launcher2
//参考Launcher2
public class Launcher extends Activity implements LauncherModel.CallBack ,View.OnLongClickListener {
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

        testIcons();
    }

    private void testIcons() {
        CellLayout cellLayout=new CellLayout(this);
         mWorkSpace.addView(cellLayout,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++){
                IconView iconView=IconView.xml(cellLayout);
                ItemInfo mInfo=new ItemInfo();
                mInfo.cellX=i;
                mInfo.cellY=j;
                mInfo.spanX=1;
                mInfo.spanY=1;
                iconView.setItemInfo(mInfo);
                iconView.setOnLongClickListener(this);
                cellLayout.addView(iconView);
            }
        }
    }

    public View getToolBar() {
        return mBottomToolbar;
    }
    public DragController getDragController(){
        return mDragController;
    }

    public DragLayer getDragLayer() {
        return mDragLayer;
    }

    public WorkSpace getWorkspace(){
        return mWorkSpace;
    }

    @Override
    public boolean onLongClick(View v) {
        if(v instanceof IconView) {
            IconView iconView= (IconView) v;
            Launcher.get().getDragController().startDrag(mWorkSpace,iconView);
        }
        return true;
    }
}
