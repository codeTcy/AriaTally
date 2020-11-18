package com.tcy.mytally;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tcy.mytally.adapter.AccountAdapter;
import com.tcy.mytally.db.AccountBean;
import com.tcy.mytally.db.DBManger;
import com.tcy.mytally.util.BudgetDialog;
import com.tcy.mytally.util.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView todayLV;//展示今日收支情况的listView

    //声明数据源
    List<AccountBean> mDatas;

    //适配器
    AccountAdapter adapter;

    //
    int year, month, day;

    //头布局
    View headerView;
    //以下为头布局相关控件
    TextView topOutTv, topInTv, topBudgetTv, topConditionTv;
    ImageView topShowIv;

    //以下为非头布局相关控件
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;

    //默认为睁着眼睛
    boolean ishow = true;

    //用sharePreference去记录,因为数据量少,且要永久存储
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化自带的view的方法
        initView();

        //添加ListView头布局
        addLVheaderView();

        //设置时间
        initTime();

        //初始化sharePerferences
        initPF();

        //初始化数据源，设置适配器,加载每一行数据到List当中
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);
        todayLV.setAdapter(adapter);

    }

    /*初始化共享数据*/
    private void initPF() {
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
    }

    /*初始化自带的view的方法*/
    private void initView() {
        todayLV = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLvLongClickListener();//设置ListView的长按事件
    }

    /*设置ListView的长按事件*/
    private void setLvLongClickListener() {
        todayLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return false;//点击了头布局
                }
                int pos = position - 1;//position为1的时候，对应List里面为0
                AccountBean clickBean = mDatas.get(pos);//获取正在被点击的这条信息
                //弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

    /*弹出是否删除某一条记录的对话框*/
    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录嘛？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //执行删除的操作
                        int click_id = clickBean.getId();
                        DBManger.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);//移除该对象,实时刷新
                        adapter.notifyDataSetChanged();//提示适配器更新数据
                        setTopTVshow();//头布局也要更新
                    }
                });
        builder.create().show();//显示对话框
    }

    /*添加ListView头布局*/
    private void addLVheaderView() {
        //将布局转化为View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        //设置头布局
        todayLV.addHeaderView(headerView);

        //查找头布局需要用到的控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topBudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConditionTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_lv_hide);

        //设置点击事件
        topBudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);

    }

    /*获取今日的具体时间*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();//得到日历对象
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /*
     * onResume() --->当activity获取焦点时会调用的方法
     * 把数据的加载放到onResume当中，因为当返回的时候，数据还会变化
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadDBdata();
        setTopTVshow();
    }

    /*设置头布局内容中文本内容的显示*/
    private void setTopTVshow() {
        //获取今日支出和收入总金额,显示在view当中
        float incomeOneDay = DBManger.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManger.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "今日支出 ￥" + outcomeOneDay + " 今日收入 ￥" + incomeOneDay;
        topConditionTv.setText(infoOneDay);

        //获取本月支出和收入总金额
        float incomeOneMonth = DBManger.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManger.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("￥ " + incomeOneMonth);
        topOutTv.setText("￥ " + outcomeOneMonth);

        //设置显示预算剩余
        float budgetMoney = preferences.getFloat("bmoney", 0.0f);//获取预算金额
        if (budgetMoney == 0) {
            topBudgetTv.setText("￥ 0");
        } else {
            float OutComeOneMonth = DBManger.getSumMoneyOneMonth(year, month, 0);
            float restMoney = budgetMoney - OutComeOneMonth;//预算剩余=预算-支出
            topBudgetTv.setText("￥ " + restMoney);
        }

    }

    private void loadDBdata() {
        List<AccountBean> list = DBManger.getAccountListFromAccounttb(year, month, day);
        mDatas.clear();//清空原来的
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }


    /*头布局相关的点击事件*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_iv_search:
                Intent intent0 = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent0);
                break;
            case R.id.main_btn_edit:
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_lv_hide:
                //切换TextView明文和密文
                toggleShow();
                break;
        }

        if (v == headerView) {
            //头布局被点击了
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }


    /*
     * 显示"设置预算"对话框
     * */
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void OnEnsure(float money) {
                //将预算金额写入到共享参数当中，进行存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney", money);
                editor.commit();

                //计算剩余金额
                float OutComeOneMonth = DBManger.getSumMoneyOneMonth(year, month, 0);
                float restMoney = money - OutComeOneMonth;//预算剩余=预算-支出
                topBudgetTv.setText("￥ " + restMoney);

            }
        });
    }

    /*
     * 点击头布局的眼睛时,如果原来时明文就加密,否则就显示
     * */
    private void toggleShow() {
        if (ishow) {//明文--->密文
            PasswordTransformationMethod instance = PasswordTransformationMethod.getInstance();//得到密文对象
            topInTv.setTransformationMethod(instance);//设置隐藏
            topOutTv.setTransformationMethod(instance);//设置隐藏
            topBudgetTv.setTransformationMethod(instance);//设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_hide);
            ishow = false;
        } else {
            //密文--->明文
            HideReturnsTransformationMethod instance = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(instance);//设置显示
            topOutTv.setTransformationMethod(instance);//设置显示
            topBudgetTv.setTransformationMethod(instance);//设置显示
            topShowIv.setImageResource(R.mipmap.ih_show);
            ishow = true;
        }
    }
}
