package com.example.madcamp_proj2;

public interface AsyncTaskCallback {
    default void method1(String s){
        return;
    };
    default void method2(String s){
        return;
    };
}
