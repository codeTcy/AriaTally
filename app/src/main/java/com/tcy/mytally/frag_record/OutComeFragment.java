package com.tcy.mytally.frag_record;

import com.tcy.mytally.R;
import com.tcy.mytally.db.DBManger;
import com.tcy.mytally.db.TypeBean;

import java.util.List;

public class OutComeFragment extends BaseRecordFragment {

    //重写
    @Override
    public void loadDataToGv() {
        super.loadDataToGv();
        //获取数据库当中的数据源
        List<TypeBean> outList = DBManger.getTypeList(0);
        typeList.addAll(outList);
        adapter.notifyDataSetChanged();//adapter进行更新
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveToDB() {
        accountBean.setKind(0);
        DBManger.insertItemToAccounttb(accountBean);
    }
}
