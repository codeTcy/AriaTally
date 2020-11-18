package com.tcy.mytally;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tcy.mytally.adapter.AccountAdapter;
import com.tcy.mytally.db.AccountBean;
import com.tcy.mytally.db.DBManger;
import com.tcy.mytally.util.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView historyLv;
    TextView timeTv;
    List<AccountBean> mDatas;
    AccountAdapter adapter;//沿用首页面的适配器

    //
    int year, month;
    int dialogSelectYearPos = -1;//选中的年份的位置
    int getDialogSelectMonthPos = -1;//选中的月份的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_iv_time);
        mDatas = new ArrayList<>();//初始化数据源

        //设置适配器
        adapter = new AccountAdapter(this, mDatas);//初始化adapter
        historyLv.setAdapter(adapter);//设置给ListView

        //初始化时间
        initTime();
        timeTv.setText(year + "年" + month + "月");
        loadData(year, month);

        //设置长按点击事件
        setLvClickListener();
    }

    /*设置ListView每一个Item的长按事件*/
    private void setLvClickListener() {
        historyLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AccountBean bean = mDatas.get(position);
                deleteItem(bean);
                return false;
            }
        });
    }

    /*删除item*/
    private void deleteItem(final AccountBean bean) {
        final int id = bean.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManger.deleteItemFromAccounttbById(id);
                        mDatas.remove(bean);//实时刷新，从数据源删除
                        adapter.notifyDataSetChanged();//通知更新
                    }
                });
        builder.create().show();

    }


    /*获取指定年份月份收支情况的列表*/
    private void loadData(int year, int month) {
        List<AccountBean> beanList = DBManger.getAccountListOneMonthFromAccounttb(year, month);
        mDatas.clear();
        mDatas.addAll(beanList);
        adapter.notifyDataSetChanged();

    }

    /*初始化时间*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.history_iv_rili:
                CalendarDialog dialog = new CalendarDialog(this, dialogSelectYearPos, getDialogSelectMonthPos);
                dialog.show();
                dialog.setDialogSize();
                dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                    @Override
                    public void onRefresh(int selPos, int year, int month) {
                        timeTv.setText(year + "年" + month + "月");
                        loadData(year, month);
                        dialogSelectYearPos = selPos;
                        getDialogSelectMonthPos = month;

                    }
                });
                break;
        }
    }
}
