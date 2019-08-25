package com.example.rheophotos.views.splash;

//interface between SplashActivity and SplashPresenter
//implemented by SplashPresenter
public interface SplashMvpPresenter {

    void onAttached(SplashMvpView splashMvpView);

    void onDetached();

    void onCreated(boolean isRootActivityInTheTask);
}
