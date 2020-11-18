package com.tcy.mytally.frag_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcy.mytally.R;
import com.tcy.mytally.db.TypeBean;

import java.lang.reflect.Type;
import java.util.List;

public class TypeBeanAdapter extends BaseAdapter {

    Context context;//传入上下文
    List<TypeBean> mDatas;//传入数据源
    int selectPost = 0;//选中的位置

    public TypeBeanAdapter(Context context, List<TypeBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();//返回集合的长度
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //此适配器不考虑复用问题，因为所有的item都显示在界面上，不会因为滑动而消失
    //没有多余的convertView，所以不用复写

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent, false);
        //查找布局当中的控件
        ImageView iv = convertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = convertView.findViewById(R.id.item_recordfrag_tv);
        //获取指定位置的数据源
        TypeBean typeBean = mDatas.get(position);
        tv.setText(typeBean.getTypeName());
        //判断当前位置是否为选中位置，如果是选中位置就设置为带颜色的图片
        if (selectPost == position) {
            iv.setImageResource(typeBean.getSimageId());
        } else {
            iv.setImageResource(typeBean.getImageId());
        }
        return convertView;
    }
}
