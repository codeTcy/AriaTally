package com.tcy.mytally;

import android.app.Application;

import com.tcy.mytally.db.DBManger;
import com.tcy.mytally.db.TypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示全局应用的类
 */
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DBManger.initDB(getApplicationContext());
    }

}
