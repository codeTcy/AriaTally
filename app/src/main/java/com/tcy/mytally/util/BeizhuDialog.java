package com.tcy.mytally.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.tcy.mytally.R;

public class BeizhuDialog extends Dialog implements View.OnClickListener {

    EditText et;
    Button cancelBtn, ensureBtn;
    onEnsureListener onEnsureListener;

    public BeizhuDialog(@NonNull Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_beizhu);//与布局相连接
        et = findViewById(R.id.dialog_beizhu_et);
        cancelBtn = findViewById(R.id.dialog_beizhu_btn_cancel);
        ensureBtn = findViewById(R.id.dialog_beizhu_btn_ensure);
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }


    /*以下为接口回调*/
    public interface onEnsureListener {
        public void onEnsure();
    }

    //设定回调接口的方法
    public void setOnEnsureListener(BeizhuDialog.onEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }
    /*以上为接口回调*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_beizhu_btn_cancel:
                cancel();
                break;
            case R.id.dialog_beizhu_btn_ensure:
                //不要写在里面，不然传数据太多了,还要传上下文
                //写个接口扔到外面去写
                //接口回调
                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }

    //获取输入数据的方法
    public String getEditText() {
        return et.getText().toString().trim();
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
