package com.tcy.mytally.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tcy.mytally.R;
import com.tcy.mytally.adapter.CalendarAdapter;
import com.tcy.mytally.db.DBManger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener {

    ImageView errorIv;
    GridView gv;
    LinearLayout hsvLayout;

    List<TextView> hsvTextList;//TextView的List集合
    List<Integer> yearList;//年份的List集合

    int selectPos = -1;//表示正在被点击的年份的位置
    int selectMon = -1;//表示当前被选中月份
    private CalendarAdapter adapter;


    //构造函数，把外面的参数传进来
    public CalendarDialog(@NonNull Context context, int selectPos, int selectMon) {
        super(context);
        this.selectPos = selectPos;
        this.selectMon = selectMon;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        gv = findViewById(R.id.dialog_calendar_gv);
        errorIv = findViewById(R.id.dialog_calendar_iv);
        hsvLayout = findViewById(R.id.dialog_calendar_layout);
        errorIv.setOnClickListener(this);

        //向横向的ScrollView添加View的方法
        addViewToLayout();

        //初始化GridView
        initGridView();

        //设置GridView当中每一个Item的点击事件
        setGvListener();
    }


    //接口回调
    //因为要影响到前面的活动了
    public interface OnRefreshListener {
        //一个刷新界面的listener
        public void onRefresh(int selPos, int year, int month);
    }

    OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    //以上为接口回调


    /*设置GridView当中每一个Item的点击事件*/
    private void setGvListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;//被选中位置变化了
                adapter.notifyDataSetInvalidated();//提示adapter
                //获取到被选中的年份和月份
                int month = position + 1;
                int year = adapter.year;
                onRefreshListener.onRefresh(selectPos, year, month);
                cancel();
            }
        });

    }

    /*初始化GridView*/
    private void initGridView() {
        int selectedYear = yearList.get(selectPos);
        adapter = new CalendarAdapter(getContext(), selectedYear);
        if (selectMon == -1) {
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            adapter.selectPos = month - 1;

        } else {
            adapter.selectPos = selectMon - 1;
        }
        gv.setAdapter(adapter);


    }

    /*向横向的ScrollView添加View的方法*/
    private void addViewToLayout() {
        hsvTextList = new ArrayList<>();//将添加进入线性布局的TextView进行统一管理的集合

        yearList = DBManger.getYearListFromAccounttb();//获取数据库当中存储了多少个年份
        //如果数据库没有记录，就添加今年的记录
        if (yearList.size() == 0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }

        //遍历年份，有几年，就向ScrollView当中添加几个View
        for (int i = 0; i < yearList.size(); i++) {
            int year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_dialogcal_hsv, null);//将这个布局转为View对象
            hsvLayout.addView(view);//将View添加到布局当中
            TextView hsvTv = view.findViewById(R.id.item_dialogcal_hsv_tv);
            hsvTv.setText(year + "");
            hsvTextList.add(hsvTv);

        }

        if (selectPos == -1) {
            selectPos = hsvTextList.size() - 1;//设备被选中的是当前最近的年份
        }

        changeTvBg(selectPos);//将最后一个设置为选中状态

        setHSVClickListener();//设置每一个View的监听事件
    }

    /*给横向的ScrollView当中每一个TextView设置点击事件*/
    private void setHSVClickListener() {
        for (int i = 0; i < hsvTextList.size(); i++) {
            TextView view = hsvTextList.get(i);
            final int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTvBg(pos);
                    selectPos = pos;
                    //获取选中的年份，然后下面的GridView的数据源会发生变化
                    int year = yearList.get(selectPos);
                    adapter.setYear(year);
                }
            });

        }
    }

    /*传入被选中的位置，改变此位置上的背景和文字颜色*/
    private void changeTvBg(int selectPos) {
        for (int i = 0; i < hsvTextList.size(); i++) {
            TextView tv = hsvTextList.get(i);
            tv.setBackgroundResource(R.drawable.dialog_btn_bg);//白色背景
            tv.setTextColor(Color.BLACK);//默认为黑色文字

        }

        TextView view = hsvTextList.get(selectPos);
        view.setBackgroundResource(R.drawable.main_recordbtn_bg);
        view.setTextColor(Color.WHITE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_calendar_iv:
                cancel();
                break;
        }
    }

    /*设置Dialog的尺寸和屏幕的尺寸一致*/
    public void setDialogSize() {
        //获取当前窗口对象
        Window window = getWindow();
        //获取窗口对象的参数
        WindowManager.LayoutParams attributes = window.getAttributes();
        //获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        attributes.width = (int) (d.getWidth());//对话框窗口为屏幕窗口
        attributes.gravity = Gravity.TOP;//对话框在上方
        window.setBackgroundDrawableResource(android.R.color.transparent);//背景为透明
        window.setAttributes(attributes);
    }

}
