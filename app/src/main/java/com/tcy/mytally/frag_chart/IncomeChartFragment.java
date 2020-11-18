package com.tcy.mytally.frag_chart;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tcy.mytally.R;
import com.tcy.mytally.adapter.ChartItemAdapter;
import com.tcy.mytally.db.BarChartItemBean;
import com.tcy.mytally.db.ChartItemBean;
import com.tcy.mytally.db.DBManger;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeChartFragment extends BaseChartFragment {

    int kind = 1;

    @Override
    public void onResume() {
        super.onResume();
        loadData(year, month, kind);

    }

    @Override
    public void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();

        //获取这个月每天的支出总金额
        List<BarChartItemBean> list = DBManger.getSumMoneyOneDayInMonth(year, month, kind);
        if (list.size() == 0) {
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        } else {
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);

            //设置有多少根柱子
            List<BarEntry> barEntries = new ArrayList<>();//表示每一根柱子的集合
            for (int i = 0; i < 31; i++) {
                //初始化每一根柱子添加到柱状图当中
                BarEntry barEntry = new BarEntry(i, 0.0f);//i表示第几个，0.0f表示默认高度
                barEntries.add(barEntry);
            }

            for (int i = 0; i < list.size(); i++) {
                BarChartItemBean itemBean = list.get(i);
                int day = itemBean.getDay();//获取是哪一天的
                //根据天数，获取x轴的位置
                int xIndex = day - 1;
                BarEntry barEntry = barEntries.get(xIndex);//得到刚刚那个对象，对这个对象再设置其他属性
                barEntry.setY(itemBean.getSumMoney());

            }

            BarDataSet barDataSetStyle = new BarDataSet(barEntries, "");
            barDataSetStyle.setValueTextColor(Color.BLACK); // 值的颜色
            barDataSetStyle.setValueTextSize(8f); // 值的大小
            barDataSetStyle.setColor(Color.parseColor("#1890ff")); // 柱子的颜色

            // 设置柱子上数据显示的格式
            barDataSetStyle.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    // 此处的value默认保存一位小数
                    if (value == 0) {
                        return "";
                    }
                    return value + "";
                }
            });
            sets.add(barDataSetStyle);

            //设置柱子的宽度
            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f); // 设置柱子的宽度
            barChart.setData(barData);
        }
    }

    @Override
    public void setYAxis(int year, int month) {
        //获取本月收入最高的一天为多少,将他设定为y轴的最大值
        float maxMoney = DBManger.getMaxMoneyOneDayInMonth(year, month, kind);
        float max = (float) Math.ceil(maxMoney);//我们给他向上取整
        //设置y轴
        // 设置y轴，y轴有两条，分别为左和右
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);  // 设置y轴的最大值
        yAxis_right.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxis_right.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);  // 不显示右边的y轴

        //设置不显示图例
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

    }

    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year, month, kind);
    }
}
