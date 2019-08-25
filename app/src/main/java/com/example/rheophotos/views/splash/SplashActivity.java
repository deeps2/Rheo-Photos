package com.example.rheophotos.views.splash;

import android.os.Bundle;

import com.example.rheophotos.R;
import com.example.rheophotos.base.BaseActivity;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    private SplashMvpPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashPresenter = new SplashPresenter();
        splashPresenter.onAttached(this);
        splashPresenter.onCreated(isTaskRoot());
    }

    @Override
    public void openSearchActivityAndFinish() {
        openSearchActivity();
        finish();
        splashPresenter.onDetached();
    }

    @Override
    public void finishCurrentActivity() {
        finish();
        splashPresenter.onDetached();
    }

}
