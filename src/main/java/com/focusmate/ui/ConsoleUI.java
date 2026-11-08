package com.focusmate.ui;

import com.focusmate.exception.InvalidDurationException;
import com.focusmate.service.PomodoroService;
import com.focusmate.util.DurationValidator;

import java.util.Scanner;

public class ConsoleUI {
    //打印横幅、读取输入、显示倒计时
    private final Scanner input = new Scanner(System.in);
    ConsoleTimeReporter reporter = new ConsoleTimeReporter();
    boolean testSetting = false;

    public void run(){
        //Beginning ___...___
        printBanner();

        boolean flag_again = true;

        do {
            int minute = askDuration();
            PomodoroService service = new PomodoroService(minute);
            service.begin(reporter);
            reporter.report("🎉 时间到！完成一次专注！");

            if (!testSetting) {
                flag_again = isTrueAgain();
            }else{
                flag_again = isAgain();
            }

        }while(flag_again);

    }

    private int askDuration(){
        //将“询问”这个行为独立成方法
        //不合法就一直问...
        while (true) {
            System.out.println("请输入本次专注时长（范围:1~120min）:");
            String raw = input.nextLine();
            String cleaned = raw.trim();//去掉两边空格

            try {
                return DurationValidator.validate(cleaned);
                //返回合法值
            } catch (InvalidDurationException e) {
                //e是一个InvalidDurationException的对象，getMessage()方法可以返回对应errorMessage
                System.out.println("⚠ " + e.getMessage() + "，请重新输入。");
            }
        }

    }

    private void printBanner() {
        //神秘开头语
        System.out.println("==============================");
        System.out.println("   🍅 FocusMate 番茄钟 v0.0.1");
        System.out.println("==============================");
    }

    //递归验证awa
    private boolean again(String choice){
        switch (choice){
            case "y" -> {return true;}
            case "n" -> {return false;}
            default -> {
                System.out.println("输入不合法...再试一次");
                return isAgain();
            }
        }
    }

    private boolean isAgain(){
        System.out.println("你要再来一次吗？（输入y/n）:");
        String choice = input.nextLine().trim();
        return again(choice);
    }

    private boolean isTrueAgain(){
        while (true) {
            System.out.print("你要再来一次吗？（输入y/n）:");
            String choice = input.nextLine().trim(); // 去个空格

            if (choice.equals("y") || choice.equals("Y")) {
                return true;
            } else if (choice.equals("n") || choice.equals("N")) {
                return false;
            } else {
                System.out.println("输入不合法，只能输入 y 或 n，再试一次");
                // 循环会自动回到开头继续问
            }
        }
    }
}
