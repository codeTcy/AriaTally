package com.tcy.mytally.frag_chart;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.tcy.mytally.R;
import com.tcy.mytally.adapter.ChartItemAdapter;
import com.tcy.mytally.db.ChartItemBean;
import com.tcy.mytally.db.DBManger;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseChartFragment extends Fragment {

    ListView chartLv;

    //.field得到的
    int year;
    int month;

    //数据源
    List<ChartItemBean> mDatas;

    //.field得到
    private ChartItemAdapter adapter;

    //获取控件
    BarChart barChart;  //柱状图的控件
    TextView chartTv;   //如果没有收支情况，显示的TextView


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_chart, container, false);
        chartLv = view.findViewById(R.id.frag_chart_lv);
        //获取Activity传递的数据
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        //设置数据源
        mDatas = new ArrayList<>();
        //设置适配器
        adapter = new ChartItemAdapter(getContext(), mDatas);
        chartLv.setAdapter(adapter);

        //添加头布局
        addLvHeader();

        return view;
    }

    /*添加头布局*/
    private void addLvHeader() {
        //将布局转换成View对象
        View headerView = getLayoutInflater().inflate(R.layout.item_fragchart_top, null);
        //将View添加到ListView的头布局上
        chartLv.addHeaderView(headerView);
        //查找头布局所包含的控件
        barChart = headerView.findViewById(R.id.item_fragchar_chart);
        chartTv = headerView.findViewById(R.id.item_fragchar_chart_top_tv);
        //设定柱状图不显示描述
        barChart.getDescription().setEnabled(false);
        //设定柱状图的内边距
        barChart.setExtraOffsets(20, 20, 20, 20); // 设置于内边距,设置视图窗口大小
        //设置坐标轴
        setAxis(year, month);
        //设置坐标轴显示的数据
        setAxisData(year, month);
    }

    /*在子类中完成设置*/
    public abstract void setAxisData(int year, int month);

    /*设置柱状图坐标轴的显示   方法必须重写*/
    private void setAxis(int year, final int month) {
        //设置X轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴位置
        xAxis.setDrawGridLines(false);//设置显示网格线
        //设置x轴标签的个数
        xAxis.setLabelCount(31);//最多31天
        xAxis.setTextSize(12f);  //x轴标签的大小

        //设置x轴显示的值得格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int val = (int) value;
                if (val == 0) {//如果是第一个位置
                    return month + "-1";//1号
                }

                if (val == 14) {
                    return month + "-15";//15号
                }

                //根据不同的月份显示最后一条的位置
                if (month == 2) {
                    if (val == 27) {
                        return month + "-28";
                    }
                } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 12) {
                    if (val == 30) {
                        return month + "-31";
                    }
                } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                    if (val == 29) {
                        return month + "-30";
                    }
                }

                return "";//其他位置显示空
            }
        });
        xAxis.setYOffset(10);//设置标签对x轴的偏移量，垂直方向

        //y轴在子类的设置
        setYAxis(year, month);
    }

    /*设置y轴，因为最高的坐标不确定，所以在子类当中设置*/
    public abstract void setYAxis(int year, int month);


    /*在onResume里面进行重写加载数据*/
    public void loadData(int year, int month, int kind) {
        List<ChartItemBean> list = DBManger.getChartListFromAccounttb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();//提示更新
    }

    /*设置时间,主要是给子类进行改变的,要重写*/
    public void setDate(int year, int month) {
        this.year = year;
        this.month = month;

        //清空柱状图当中的数据
        barChart.clear();
        barChart.invalidate();//重新绘制
        setAxis(year, month);
        setAxisData(year, month);

    }

}
