package com.tcy.mytally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcy.mytally.adapter.ChartVpAdapter;
import com.tcy.mytally.db.DBManger;
import com.tcy.mytally.frag_chart.IncomeChartFragment;
import com.tcy.mytally.frag_chart.OutcomeChartFragment;
import com.tcy.mytally.frag_record.InComeFragment;
import com.tcy.mytally.frag_record.OutComeFragment;
import com.tcy.mytally.util.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {

    Button inBtn, outBtn;
    TextView timeTv, inTv, outTv;
    ViewPager chartVp;

    //.field自动生成的
    private int year;
    private int month;

    //点击位置的保存
    int selectPos = -1, selectMonth = -1;

    //存放2个fragment
    List<Fragment> chartFragmentList;

    private ChartVpAdapter adapter;
    private IncomeChartFragment incomeChartFragment;
    private OutcomeChartFragment outcomeChartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);

        //初始化控件
        initView();

        //初始化时间
        initTime();

        //统计某年某月的收支情况数据
        initStatistic(year, month);

        //初始化Fragment
        initFrag();

        //让pager和按钮联动
        setVpSelectListener();

    }

    /*让pager和按钮联动*/
    private void setVpSelectListener() {
        //SimpleOnPageChangeListener想要重写就重新，不想重写就不用重写
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            //onPageSelected看谁被选中了
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    /*初始化Fragment*/
    private void initFrag() {
        chartFragmentList = new ArrayList<>();//初始化
        //添加fragment对象
        incomeChartFragment = new IncomeChartFragment();
        outcomeChartFragment = new OutcomeChartFragment();
        //添加数据到Fragment当中
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);
        //将fragment添加到数据源中
        chartFragmentList.add(outcomeChartFragment);
        chartFragmentList.add(incomeChartFragment);
        //使用适配器
        adapter = new ChartVpAdapter(getSupportFragmentManager(), chartFragmentList);
        chartVp.setAdapter(adapter);//将fragment加载到viewPager当中
    }

    /*统计某年某月的收支情况数据*/
    private void initStatistic(int year, int month) {
        float inMoneyOneMonth = DBManger.getSumMoneyOneMonth(year, month, 1);//收入总钱数
        float outMoneyOneMonth = DBManger.getSumMoneyOneMonth(year, month, 0);//支出总钱数
        int inCount = DBManger.getCountItemOneMonth(year, month, 1);//收入多少笔
        int outCount = DBManger.getCountItemOneMonth(year, month, 0);//支出多少笔
        timeTv.setText(year + "年" + month + "月账单");
        inTv.setText("共" + inCount + "笔收入, ￥ " + inMoneyOneMonth);
        outTv.setText("共" + outCount + "笔支出, ￥ " + outMoneyOneMonth);
    }

    /*初始化时间的方法*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;

    }

    /*初始化控件的方法*/
    private void initView() {
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);
        timeTv = findViewById(R.id.chart_tv_date);
        inTv = findViewById(R.id.chart_tv_in);
        outTv = findViewById(R.id.chart_tv_out);
        chartVp = findViewById(R.id.chart_vp);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_rili:
                showCalendarDialog();
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);//切换Pager
                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);//切换Pager
                break;
        }
    }

    /*显示日历的对话框*/
    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectPos, selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos = selPos;
                MonthChartActivity.this.selectMonth = month;
                initStatistic(year, month);
                incomeChartFragment.setDate(year, month);
                outcomeChartFragment.setDate(year, month);
            }
        });
    }

    /*设置按钮样式的改变 支出----0  收入----1*/
    private void setButtonStyle(int kind) {
        if (kind == 0) {
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        } else if (kind == 1) {
            inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }
}
