package com.tcy.mytally.util;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.tcy.mytally.R;

public class KeyBoardUtil {

    private final Keyboard k1;//自定义键盘对象
    private KeyboardView keyboardView;
    private EditText editText;


    //........以下为接口回调
    //表示"完成"的接口
    //写一个接口回调，让它将数据显示出来
    //什么是接口回调？？？
    // 回调只是个概念，
    // 就是把你的接口对应的实现类的一个实例当成一个参数传递给一个函数调用，
    // 那个函数处理过程中会调用你的这个接口中的方法
    //
    //流程是：
    //1）定义接口： 定义一个接口、定义其中的抽象方法、抽象方法含有参数(被传递的数据)；
    //2）编写回调方法： 在定义接口的类中，编写用户回调的方法，要传递一个接口对象实例，让别的类去实现。（相当于为接口成员变量赋值）
    //3）为抽象方法赋值： 获取一个全局的接口成员变量，在某个事件中使用接口成员变量调用接口中的方法，并且为抽象方法中的参数赋值。（这一步可以在回调方法中实现）
    //
    //
    //在另一个页面，在合适的时机，创建此类的实例，调用类中的回调方法，为接口对象赋值this，即可实现回调。
    public interface onEnsureListener {
        public void onEnsure();//完成被点击了
    }

    onEnsureListener onEnsureListener;//接口初始化

    public void setOnEnsureListener(onEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;//让它将接口对象传进来
    }
    //键盘点击完成的时候去执行onEnsure()方法
    //........以上为接口回调


    public KeyBoardUtil(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL);//取消弹出系统键盘
        k1 = new Keyboard(this.editText.getContext(), R.xml.key);
        this.keyboardView.setKeyboard(k1);//显示的就是k1
        this.keyboardView.setEnabled(true);//设置为可以使用
        this.keyboardView.setPreviewEnabled(false);

        this.keyboardView.setOnKeyboardActionListener(listener);//设置键盘按钮被点击了的监听
    }

    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = editText.getText();//Editable 是可变的，String 是不可变的了
            int start = editText.getSelectionStart();
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE://点击了删除键
                    if (editable != null || editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);//删除一位
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL://点击了清零
                    editable.clear();
                    break;
                case Keyboard.KEYCODE_DONE://点击了完成
                    onEnsureListener.onEnsure();//通过接口回调的方法,当点击确定时,可以调用这个方法
                    break;
                default://其他的数字直接插入
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    //显示键盘
    public void showKeyBoard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    //隐藏键盘
    public void hideKeyBoard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }
}
