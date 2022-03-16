package com.weizu.mykotlindemospace;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyClass {

    class A{
        protected String info;
        public A(String info){
            this.info = info;
        }

        @Override
        public String toString() {
            return "A: " + this.info;
        }
    }

    class B extends A{
        public B(String info){
            super(info);
        }

        @Override
        public String toString() {
            return "B: " + info;
        }
    }

    class C extends B{
        public C(String info) {
            super(info);
        }

        @Override
        public String toString() {
            return "C: " + info;
        }
    }

    @Test
    public void Test() {
        // 通配符 <? super T>
        ArrayList<? super B> list = new ArrayList<>();
        list.add(new C("3"));

        for (Object o : list) {
            System.out.println(o);
        }
    }

    @Test
    public void Test2() {
        // 通配符 <? extends T>
        ArrayList<? extends B> list = null;

        ArrayList<C> as = new ArrayList<>();
        as.add(new C("1"));
        as.add(new C("2"));
        list = as;

        for (Object a : list) {
            System.out.println(a);
        }
    }
}