package service.bind.test.draglayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {
    private static MainActivity mActivity;

    public static MainActivity get(){
        return mActivity;
    }

    BottomToolbar mBottomToolbar;
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
        mBottomToolbar= (BottomToolbar) findViewById(R.id.toolbar);
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
