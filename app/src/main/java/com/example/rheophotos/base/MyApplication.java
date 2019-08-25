package com.example.rheophotos.base;

import android.app.Application;

import com.example.rheophotos.repository.local.SearchLocal;

public class MyApplication extends Application {

    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        sInstance = this;
        super.onCreate();
        SearchLocal.getInstance().getTotalRowsFromDB();
    }

    public static synchronized MyApplication getInstance() {
        return sInstance;
    }
}
