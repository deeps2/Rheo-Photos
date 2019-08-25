package com.example.rheophotos.views.search;

import com.example.rheophotos.model.SearchResponse;

import java.util.List;

//interface between SearchActivity and SearchPresenter
//implemented by SearchActivity
public interface SearchMvpView {

    void showOrHideCrossIcon(int visibility);

    void showProgressForNetworkRequest();

    void hideProgressBarForNetworkRequest();

    void showMessage(int drawableId, int stringId);

    void showToast(int stringId);

    void displayResultsOnUI(List<SearchResponse.Results> resultsList);
}
