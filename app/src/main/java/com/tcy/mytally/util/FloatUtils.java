package com.tcy.mytally.util;

import java.math.BigDecimal;

public class FloatUtils {

    /*进行除法运算，保留4位小数*/
    public static float div(float v1, float v2) {
        float v3 = v1 / v2;
        BigDecimal b1 = new BigDecimal(v3);
        float value = b1.setScale(4, 4).floatValue();//保留4位，四舍五入
        return value;
    }

    /*将浮点类型转换为百分比显示形式*/
    public static String ratioToPercentage(float val) {
        float v = val * 100;
        BigDecimal b1 = new BigDecimal(v);
        float v1 = b1.setScale(2, 4).floatValue();//保留2位数据
        String percentage = v1 + "%";
        return percentage;
    }
}
