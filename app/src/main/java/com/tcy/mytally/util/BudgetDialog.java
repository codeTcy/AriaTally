package com.tcy.mytally.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tcy.mytally.R;

public class BudgetDialog extends Dialog implements View.OnClickListener {

    ImageView cancelIv;
    Button ensureBtn;
    EditText moneyEt;

    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);
        cancelIv = findViewById(R.id.dialog_budget_error);
        ensureBtn = findViewById(R.id.dialog_budget_btn_ensure);
        moneyEt = findViewById(R.id.dialog_budget_et);

        cancelIv.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    //接口回调
    public interface OnEnsureListener {
        public void OnEnsure(float money);//把这个数值写到sharePreference里面去
    }

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }
    //以上为接口回调

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_budget_error:
                cancel();//让对话框消失
                break;
            case R.id.dialog_budget_btn_ensure:
                //获取输入数据数值
                String money = moneyEt.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    Toast.makeText(getContext(), "输入数据不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                float data = Float.parseFloat(money);
                if (data <= 0) {
                    Toast.makeText(getContext(), "预算金额必须大于0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onEnsureListener != null) {
                    onEnsureListener.OnEnsure(data);
                }
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
        attributes.gravity = Gravity.BOTTOM;//对话框在下方
        window.setBackgroundDrawableResource(android.R.color.transparent);//背景为透明
        window.setAttributes(attributes);
        handler.sendEmptyMessageDelayed(1, 100);

    }

    //延迟弹出软键盘，写在handler里面
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //自动弹出软件盘的方法
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        }
    };
}
