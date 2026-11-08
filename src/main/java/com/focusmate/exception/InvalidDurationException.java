package com.focusmate.exception;
//这边包语句不能再加前缀

public class InvalidDurationException extends Exception {
    //表达"时长非法"这一业务错误，继承异常类
    //每一个自定义异常都要继承Exception类
    private final static String ERROR_MESSAGE_HEAD = "输入不合法 原因:";

    public InvalidDurationException(String reason){
        super(ERROR_MESSAGE_HEAD + reason);//对应的错误信息
    }
}
