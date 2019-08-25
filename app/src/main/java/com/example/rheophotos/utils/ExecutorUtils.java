package com.example.rheophotos.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorUtils {

    private static ExecutorUtils sInstance;
    private final Executor diskIO;
    private final Executor mainThread;

    private ExecutorUtils(Executor diskIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }

    public static synchronized ExecutorUtils getInstance() {
        if (sInstance == null)
            sInstance = new ExecutorUtils(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
