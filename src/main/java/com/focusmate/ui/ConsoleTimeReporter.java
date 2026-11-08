package com.focusmate.ui;

import com.focusmate.service.TimeReporter;

public class ConsoleTimeReporter implements TimeReporter {
    @Override
    public void report(String text) {
        //实现接口方法都要用public修饰
        //因为接口默认就要求它是公开的，在实现的时候，权限只能一样大，或者更大
        System.out.print("\r"+text); // 打印时间
    }
}
