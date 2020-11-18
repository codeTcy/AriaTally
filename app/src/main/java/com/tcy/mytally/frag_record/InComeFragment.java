package com.tcy.mytally.frag_record;

import com.tcy.mytally.R;
import com.tcy.mytally.db.DBManger;
import com.tcy.mytally.db.TypeBean;

import java.util.ArrayList;
import java.util.List;

public class InComeFragment extends BaseRecordFragment {

    //重写
    @Override
    public void loadDataToGv() {
        super.loadDataToGv();
        //获取数据库当中的数据源
        List<TypeBean> inList = DBManger.getTypeList(1);
        typeList.addAll(inList);
        adapter.notifyDataSetChanged();//adapter进行更新
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    public void saveToDB() {
        //把accountBean剩下没有给值的元素设置一下
        accountBean.setKind(1);
        DBManger.insertItemToAccounttb(accountBean);
    }



}
