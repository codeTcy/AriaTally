package com.tcy.mytally.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tcy.mytally.R;

import java.util.ArrayList;
import java.util.List;

/*
 * 历史账单界面，点击日历表，弹出对话框当中的GridView对应的适配器
 **/
public class CalendarAdapter extends BaseAdapter {

    //把以下东西全部传进来
    Context context;
    public int year;
    //数据源可以自己去加载
    List<String> mDatas;

    //选中位置
    public int selectPos = -1;


    public void setYear(int year) {
        this.year = year;
        mDatas.clear();
        loadDatas(year);
        notifyDataSetChanged();//提示adapter更新

    }

    public CalendarAdapter(Context context, int year) {
        this.context = context;
        this.year = year;
        mDatas = new ArrayList<>();
        loadDatas(year);
    }

    private void loadDatas(int year) {
        for (int i = 1; i < 13; i++) {
            String data = year + "/" + i;
            mDatas.add(data);
        }
    }

    public CalendarAdapter() {
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_dialogcal_gv, parent, false);//没有复用，直接写一下
        TextView tv = convertView.findViewById(R.id.item_dialogcal_gv_tv);//拿到我们的TextView
        tv.setText(mDatas.get(position));//演示的内容就是存储的东西
        tv.setBackgroundResource(R.color.grey_f3f3f3);//没有选中时，背景颜色为f3f3f3
        tv.setTextColor(Color.BLACK);//文字为黑色
        if (position == selectPos) {//如果为选中时，要变颜色
            tv.setBackgroundResource(R.color.pink_fb7299);
            tv.setTextColor(Color.WHITE);
        }
        return convertView;
    }

}
