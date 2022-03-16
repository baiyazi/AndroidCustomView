package com.weizu.jnitest;

public class JavaMethod {

    public int add(int a, int b){
        System.out.println("调用了JavaMethod类中的类方法。");
        return a + b;
    }

    public static void showInfo(){
        System.out.println("调用了JavaMethod类中的静态方法。");
    }
}
