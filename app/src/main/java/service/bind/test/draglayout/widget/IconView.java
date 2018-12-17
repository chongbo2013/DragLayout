package service.bind.test.draglayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import service.bind.test.draglayout.R;
import service.bind.test.draglayout.bean.ItemInfo;
import service.bind.test.draglayout.drag.DragSource;

/**
 * Created by ferris.xu on 2016/9/5.
 */
public class IconView extends LinearLayout implements DragSource ,IWidget{
    public IconView(Context context) {
        super(context);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public ItemInfo itemInfo;
    public void setItemInfo(ItemInfo itemInfo){
        this.itemInfo=itemInfo;
    }
    public static IconView xml(ViewGroup parent){
        return (IconView) LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_layout,parent,false);
    }


    @Override
    public void onDropCompleted(View targetView) {

    }


    @Override
    public ItemInfo getInfo() {
        return itemInfo;
    }

    @Override
    public void setInfo(ItemInfo info) {
        this.itemInfo=info;
    }
}
