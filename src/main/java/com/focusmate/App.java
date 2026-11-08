package com.focusmate;

import com.focusmate.ui.ConsoleUI;

public class App {
    //装配各层、启动 UI
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();

        ui.run();

    }
}
