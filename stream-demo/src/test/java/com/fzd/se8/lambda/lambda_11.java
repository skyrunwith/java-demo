package com.fzd.se8.lambda;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/22
 */
public class lambda_11 {

    interface I {
        default void f(){};
        abstract void fAbstract();
        static void fStatic(){}
    }

    interface J {
        default void f(){};
        abstract void fAbstract();
        static void fStatic(){}
    }

    class S {
        void f(){}
    }

    class A extends S implements I, J{

        @Override
        public void f() {

        }

        @Override
        public void fAbstract() {

        }
    }
}
