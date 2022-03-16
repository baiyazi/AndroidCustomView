#include "com_weizu_jnitest_JNITest.h"

#include <android/log.h>
#define LOG_TAG "System.out"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// 在这个方法中进行C调java的反射
extern "C" jstring Java_com_weizu_jnitest_JNITest_helloJni(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    // 1. 字节码
    jclass jclazz = (*env).FindClass("com/weizu/jnitest/JavaMethod");
    // 2. 方法add
    jmethodID jmethodId = (*env).GetStaticMethodID(jclazz, "showInfo", "()V");
    // 静态方法不需要实例化类
    // jobject jobjectInstance = (*env).AllocObject(jclazz);
    // 3. 调用方法
    (*env).CallStaticVoidMethod(jclazz, jmethodId);
    LOGE("在C语言中调用了Java的静态方法。\n");
    return env->NewStringUTF(hello.c_str());
}