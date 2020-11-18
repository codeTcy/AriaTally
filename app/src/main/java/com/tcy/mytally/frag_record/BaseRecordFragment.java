package com.tcy.mytally.frag_record;


import android.accounts.Account;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcy.mytally.R;
import com.tcy.mytally.db.AccountBean;
import com.tcy.mytally.db.DBManger;
import com.tcy.mytally.db.TypeBean;
import com.tcy.mytally.util.BeizhuDialog;
import com.tcy.mytally.util.KeyBoardUtil;
import com.tcy.mytally.util.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录页面当中的支出模块
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv, beizhuTv, timeTv;
    GridView typeGv;
    List<TypeBean> typeList;
    TypeBeanAdapter adapter;
    AccountBean accountBean; ///将需要插入到记账当中的数据保存成对象的形式


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean(); //创建对象
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_out_come, container, false);
        initView(view);
        setInitTime();
        //给GridView填充数据的方法
        loadDataToGv();
        //给GridView每一项点击事件
        setGvListener();
        return view;
    }

    /*获取当前时间，显示在timeTv上*/
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM年dd日 HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    /*给GridView每一项的点击事件*/
    private void setGvListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPost = position;//把位置传给adapter
                adapter.notifyDataSetInvalidated();//提示绘制方式变化了,adapter去更新

                //header上面的文字和图片随之变化
                TypeBean typeBean = typeList.get(position);
                String typeName = typeBean.getTypeName();
                typeTv.setText(typeName);
                accountBean.setTypename(typeName);//要传出去的对象
                int simageId = typeBean.getSimageId();
                typeIv.setImageResource(simageId);
                accountBean.setsImageId(simageId);//要传出去的对象
            }
        });
    }

    /*给GridView填充数据的方法*/
    public void loadDataToGv() {
        typeList = new ArrayList<>();//数据源有了
        //去写适配器
        adapter = new TypeBeanAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);

        /*根据kind的值不同，做不同动作，后面要重写*/
    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        typeGv = view.findViewById(R.id.frag_record_gv);

        //让自定义软键盘显示出来
        KeyBoardUtil boardUtil = new KeyBoardUtil(keyboardView, moneyEt);
        boardUtil.showKeyBoard();

        //设置接口,监听确定按钮被点击了
        boardUtil.setOnEnsureListener(new KeyBoardUtil.onEnsureListener() {
            @Override
            public void onEnsure() {
                //获取输入钱数
                String money = moneyEt.getText().toString();
                if (TextUtils.isEmpty(money) || "0".equals(money)) {
                    getActivity().finish();
                    return;
                }
                float realMoney = Float.parseFloat(money);
                accountBean.setMoney(realMoney);
                //获取记录的信息，保存在数据库当中
                saveToDB();
                //返回上一级页面
                getActivity().finish();
            }
        });

        //给beizhu设置点击事件
        beizhuTv.setOnClickListener(this);

        //给time设置点击事件
        timeTv.setOnClickListener(this);

    }

    /*让子类一定要重写这个方法*/
    /*而且抽象方法一定要在抽象类中*/
    public abstract void saveToDB();


    //重写onClick方法
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
        }
    }

    /*弹出时间对话框*/
    public void showTimeDialog() {
        SelectTimeDialog timeDialog = new SelectTimeDialog(getContext());
        timeDialog.show();
        //设定确定按钮被点击了的监听器
        timeDialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });

    }

    /*弹出备注对话框*/
    public void showBZDialog() {
        final BeizhuDialog dialog = new BeizhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BeizhuDialog.onEnsureListener() {
            @Override
            public void onEnsure() {
                String text = dialog.getEditText();
                if (!TextUtils.isEmpty(text)) {
                    beizhuTv.setText(text);
                    accountBean.setBeizhu(text);
                }
                dialog.cancel();//执行完之后取消对话框
            }
        });
    }


}
