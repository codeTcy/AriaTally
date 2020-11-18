package com.tcy.mytally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcy.mytally.R;
import com.tcy.mytally.db.AccountBean;

import java.util.Calendar;
import java.util.List;

public class AccountAdapter extends BaseAdapter {

    //上下文对象传进来
    Context context;
    //数据源传进来
    List<AccountBean> mDatas;
    //加载布局
    LayoutInflater inflater;

    //获得年月日
    int year, month, day;


    //通过构造方法来完成传递
    public AccountAdapter(Context context, List<AccountBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);

        Calendar calendar = Calendar.getInstance();//得到日历对象
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public AccountAdapter() {
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);//返回指定位置对象
    }

    @Override
    public long getItemId(int position) {
        return position;//返回指定位置
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mainlv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);//converView去绑定holder
        } else {
            holder = (ViewHolder) convertView.getTag();
            //这个holder就是将convertView里面的东西进行统一的管理
        }

        //得到指定位置的数据源
        AccountBean bean = mDatas.get(position);
        //然后进行设置
        holder.typeIv.setImageResource(bean.getsImageId());
        holder.typeTv.setText(bean.getTypename());
        holder.beizhuTv.setText(bean.getBeizhu());
        holder.moneyTv.setText("￥ "+bean.getMoney());//转为字符串类型
        if (bean.getYear() == year && bean.getMonth() == month && bean.getDay() == day) {
            String time = bean.getTime().split(" ")[1];
            holder.timeTv.setText("今天 " + time);
        } else {
            holder.timeTv.setText(bean.getTime());
        }

        return convertView;
    }


    //在getView当中要返回指定位置的对应的view
    //我们用ViewHolder，将对应的每个item都传递进来
    class ViewHolder {

        ImageView typeIv;
        TextView typeTv, beizhuTv, timeTv, moneyTv;

        //将他们都找到，然后传入
        public ViewHolder(View v) {
            typeIv = v.findViewById(R.id.item_mainlv_iv);
            typeTv = v.findViewById(R.id.item_mainlv_tv_title);
            beizhuTv = v.findViewById(R.id.item_mainlv_tv_beizhu);
            timeTv = v.findViewById(R.id.item_mainlv_tv_time);
            moneyTv = v.findViewById(R.id.item_mainlv_tv_money);

        }
    }
}
