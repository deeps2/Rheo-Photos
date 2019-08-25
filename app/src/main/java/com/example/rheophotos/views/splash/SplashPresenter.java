package com.example.rheophotos.views.splash;

public class SplashPresenter implements SplashMvpPresenter {

    private SplashMvpView splashMVPView;

    @Override
    public void onAttached(SplashMvpView splashMvpViewArg) {
        this.splashMVPView = splashMvpViewArg;
    }

    @Override
    public void onDetached() {
        this.splashMVPView = null;
    }

    @Override
    public void onCreated(boolean isRootActivityInTheTask) {
        if (isRootActivityInTheTask)
            splashMVPView.openSearchActivityAndFinish();
        else
            splashMVPView.finishCurrentActivity();
    }


}
