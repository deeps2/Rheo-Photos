package com.example.rheophotos.views.splash;

//interface between SplashActivity and SplashPresenter
//implemented by SplashActivity
public interface SplashMvpView {

    void openSearchActivityAndFinish();

    void finishCurrentActivity();
}
