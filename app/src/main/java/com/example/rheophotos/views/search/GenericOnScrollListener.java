package com.example.rheophotos.views.search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

public class GenericOnScrollListener extends RecyclerView.OnScrollListener {

    private WeakReference<OnScrollListener> onScrollListener;

    GenericOnScrollListener(OnScrollListener onScrollListenerArg) {
        this.onScrollListener = new WeakReference<>(onScrollListenerArg);
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (onScrollListener.get() != null)
            onScrollListener.get().onScrolled(recyclerView, dx, dy);
    }

    public interface OnScrollListener {
        void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy);
    }
}
