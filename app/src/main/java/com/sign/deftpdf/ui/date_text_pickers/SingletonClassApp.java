package com.sign.deftpdf.ui.date_text_pickers;


import java.io.Serializable;

public class SingletonClassApp implements Serializable {

    private static volatile SingletonClassApp sSoleInstance;
    public Integer set_format=1;

       //private constructor.


    private SingletonClassApp(){
        //Prevent form the reflection api.
        if (sSoleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }




    public static SingletonClassApp getInstance() {
        if (sSoleInstance == null) { //if there is no instance available... create new one
            synchronized (SingletonClassApp.class) {
                if (sSoleInstance == null) sSoleInstance = new SingletonClassApp();
            }
        }
        return sSoleInstance;
    }

    //Make singleton from serialize and deserialize operation.
    protected SingletonClassApp readResolve() {
        return getInstance();
    }
}
