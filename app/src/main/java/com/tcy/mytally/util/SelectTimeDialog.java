package com.tcy.mytally.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.tcy.mytally.R;

/**
 * 在记录页面弹出时间对话框
 */
public class SelectTimeDialog extends Dialog implements View.OnClickListener {

    EditText hourEt, minuteEt;
    Button ensureBtn, cancelBtn;
    DatePicker datePicker;

    //在onCreate方法中查找控件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);
        hourEt = findViewById(R.id.dialog_time_et_hour);
        minuteEt = findViewById(R.id.dialog_time_et_minute);
        ensureBtn = findViewById(R.id.dialog_time_btn_ensure);
        cancelBtn = findViewById(R.id.dialog_time_btn_cancel);
        datePicker = findViewById(R.id.dialog_time_dp);

        //添加监听事件
        ensureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        //隐藏头布局
        hideDatePickerHeader();

    }

    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }


    //接口回调
    public interface OnEnsureListener {
        public void onEnsure(String time, int year, int month, int day);
    }

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    //以上为接口回调

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_time_btn_cancel:
                cancel();
                break;
            case R.id.dialog_time_btn_ensure:
                int year = datePicker.getYear();//选中年份
                int month = datePicker.getMonth() + 1;//选中月份【要加1】
                int day = datePicker.getDayOfMonth();//选中日期
                String monthStr = String.valueOf(month);
                if (month < 10) {
                    monthStr = "0" + month;
                }
                String dayStr = String.valueOf(day);
                if (day < 10) {
                    dayStr = "0" + day;
                }
                //获取输入的小时和分钟
                String hourStr = hourEt.getText().toString();
                String minuteStr = minuteEt.getText().toString();
                int hour = 0;
                if (!TextUtils.isEmpty(hourStr)) {
                    hour = Integer.parseInt(hourStr);
                    hour = hour & 24;//防止超过24小时
                }

                int minute = 0;
                if (!TextUtils.isEmpty(minuteStr)) {
                    minute = Integer.parseInt(minuteStr);
                    minute = minute % 60;//防止超过60
                }

                hourStr = String.valueOf(hour);
                minuteStr = String.valueOf(minute);
                if (hour < 10) {
                    hourStr = "0" + hour;
                }

                if (minute < 10) {
                    minuteStr = "0" + minute;
                }

                String timeFormat = year + "年" + monthStr + "月" + dayStr + "日 " + hourStr + ":" + minuteStr;

                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure(timeFormat, year, month, day);
                }

                cancel();
                break;
        }
    }

    //隐藏DatePicker头布局
    private void hideDatePickerHeader() {
        //获得第一个布局,也即是头布局
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        //
        if (rootView == null) {
            return;
        }
        View headerView = rootView.getChildAt(0);
        if (headerView == null) {
            return;
        }
        //5.0 +
        int headerId = getContext().getResources().getIdentifier("day_picker_selector_layout", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);//隐藏并且不占位置
            ViewGroup.LayoutParams paramsRoot = rootView.getLayoutParams();//所在的布局参数
            paramsRoot.width = ViewGroup.LayoutParams.WRAP_CONTENT;//设置宽度
            rootView.setLayoutParams(paramsRoot);//传入我们所改变的参数

            //依次往上抬
            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animator.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParamsAnimator);

            //
            View view = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsView = view.getLayoutParams();
            layoutParamsView.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(layoutParamsView);
            return;
        }

        //6.0+
        headerId = getContext().getResources().getIdentifier("date_picker_header", "id", "android");
        //可以直接获取头，然后隐藏
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }

    }
}
