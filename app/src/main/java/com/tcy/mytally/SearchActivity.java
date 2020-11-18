package com.tcy.mytally;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcy.mytally.adapter.AccountAdapter;
import com.tcy.mytally.db.AccountBean;
import com.tcy.mytally.db.DBManger;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    //
    ListView searchLv;
    EditText searchEt;
    TextView emptyTv;
    //在布局里面加了onClick的就不用找了

    List<AccountBean> mDatas;//数据源
    AccountAdapter accountAdapter;//用主界面写的适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();//初始化控件
        mDatas = new ArrayList<>();
        accountAdapter = new AccountAdapter(this, mDatas);
        searchLv.setAdapter(accountAdapter);
        searchLv.setEmptyView(emptyTv);//设置无数据时，显示的控件
    }

    private void initView() {
        searchLv = findViewById(R.id.search_lv);
        searchEt = findViewById(R.id.search_et);
        emptyTv = findViewById(R.id.search_tv_empty);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_search://执行搜索的操作
                String msg = searchEt.getText().toString().trim();
                //判断输入内容是否为空，如果是空，则提示不能搜索
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this, "输入内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //开始搜索
                mDatas.clear();//防止多点搜索导致不断添加重复内容
                List<AccountBean> beanList = DBManger.getAccountListByRemarkFromAccounttb(msg);
                mDatas.addAll(beanList);
                accountAdapter.notifyDataSetChanged();//提示适配器进行更新
                break;
        }
    }
}
