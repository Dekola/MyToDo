package com.kola.mytodo.baas;

/**
 * Created by ribads on 1/6/18.
 */

public class SingletonClass {

    static SingletonClass singletonClass;

    private SingletonClass(){

    }

    static public SingletonClass getInstance(){
        if (singletonClass == null){
            singletonClass = new SingletonClass();
        }
        return singletonClass;
    }

}


class Test {

    public static void main(){
        SingletonClass singletonClass = SingletonClass.getInstance();
    }

}