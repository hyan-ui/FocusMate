package com.focusmate.util;

import com.focusmate.exception.InvalidDurationException;

public class DurationValidator {
    //字符串->合法分钟数，非法抛异常
    //DurationValidator 由 Duration（时长/持续时间）+ Validator（验证器）组成，即时长验证器
    //功能："检查"

    private final static int MAX_TIME_VALUE = 120;
    private final static int MIN_TIME_VALUE = 1;

    public static int validate(String raw) throws InvalidDurationException {
        //raw "原始输入"
        if (raw == null){
            throw new InvalidDurationException("输入为空");
        }
        //这一步是要满足后置条件，不管外面擦没擦，自己擦了再说 : 防御性编程
        //不信任任何调用方,调用方只用负责传原始数据就行了，而validate考虑的就要多了 (比如负责把raw弄干净再判断) doge
        String cleaned = raw.trim();

        if (cleaned.isEmpty()){
            throw new InvalidDurationException("空字符");
        }
        //利用NumberFormatException异常来判断是否是数字
        int minute = 0;
        try{
            minute = Integer.parseInt(cleaned);
            /*
            * 简单介绍一下Integer
            * ta是一个int类型对应的“对象”包装，有一些方法
            * 可以是null，能用在集合中，可以直接赋值给int类(自动转换)
            * .parseInt(String)方法是一个将String类型转换为数字的方法，无法转换抛NumberFormatException异常
            * .toString(): int -> String
            * .compare(a,b) 比较大小
            * Integer.MAX_VALUE 最大数，三十二位整形约21亿
            * */

        }catch (NumberFormatException e) {
            throw new InvalidDurationException("输入不是数字！");
        }

        if (minute > MAX_TIME_VALUE || minute < MIN_TIME_VALUE){
            throw new InvalidDurationException("范围错误，时间值应在"+MIN_TIME_VALUE+"~"+MAX_TIME_VALUE+"之内");
        }

        return minute;
    }
}
