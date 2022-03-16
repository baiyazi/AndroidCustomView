package com.weizu.jnitest;

public class JNITest {

    // 加载
    static {
        System.loadLibrary("native-lib");
    }

    // Java_com_weizu_jnitest_MainActivity_helloJni
    public native String helloJni();
}
