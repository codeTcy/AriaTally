package com.tcy.mytally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcy.mytally.R;
import com.tcy.mytally.db.ChartItemBean;
import com.tcy.mytally.util.FloatUtils;

import java.util.List;

/**
 * 账单详情页面,ListView的适配器
 */
public class ChartItemAdapter extends BaseAdapter {

    //传入
    Context context;
    List<ChartItemBean> mDatas;
    LayoutInflater inflater;

    public ChartItemAdapter(Context context, List<ChartItemBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }

    public ChartItemAdapter() {

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
        ViewHolder viewHolder = null;

        //判断有没有复用的
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_fragchart_lv, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        获取显示内容
        ChartItemBean bean = mDatas.get(position);
        viewHolder.iv.setImageResource(bean.getsImageId());
        viewHolder.typeTv.setText(bean.getType());
        float ratio = bean.getRatio();
        String pert = FloatUtils.ratioToPercentage(ratio);
        viewHolder.ratioTv.setText(pert);

        viewHolder.totalTv.setText("￥ "+bean.getTotalMoney());


        return convertView;
    }

    class ViewHolder {
        TextView typeTv, ratioTv, totalTv;
        ImageView iv;

        //把我们布局生成的View传递进来
        public ViewHolder(View v) {
            typeTv = v.findViewById(R.id.item_fragchart_tv_type);
            ratioTv = v.findViewById(R.id.item_fragchart_tv_percentage);
            totalTv = v.findViewById(R.id.item_fragchart_tv_sum);
            iv = v.findViewById(R.id.item_fragchart_iv);
        }
    }
}
