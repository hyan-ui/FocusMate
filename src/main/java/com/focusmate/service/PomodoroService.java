package com.focusmate.service;

public class PomodoroService {
    private int totalSecond;
    //给定分钟数，执行倒计时
    public PomodoroService(int minuteTime){
        this.totalSecond = minuteTime*60;
    }


    public void begin(TimeReporter reporter){
        //这里讲一下“接口实现”
        /*这里声明了一个TimeReporter接口的【引用变量】reporter
        * 在main中，它会实际指向实现接口的【上一个最近对象】，并调用对方已实现的[接口方法]
        * 在这里，它只取决于传入的对象
        *
        * 如果想实现用该接口变量调用指向对应对象的“非接口特有方法”，需要向下转型为对应的类类型
        * 这种转型是 从“某个标准” -> "标准对应的执行者"，是契约与实现契约者之间的关系
        * 并不是严格意义上的“继承”，但依然是一种【上下级】关系，所以可以实现
        * 进行向下转型记得用instanceof验证
        * */
        while (totalSecond > 0) {
            int second;
            int minute;
            String timeText;

            minute = totalSecond / 60;
            second = totalSecond % 60;
            timeText = String.format("%d:%02d",minute,second);
            reporter.report(timeText);

            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                break;
            }
            //很懒的，负责“睡觉”的代码 doge
            totalSecond--;//时间减1s
        }
    }
}
