package com.example.rheophotos.views.search;

//interface between SearchActivity and SearchPresenter
//implemented by SearchPresenter
public interface SearchMvpPresenter {

    void onAttached(SearchMvpView searchMvpView);

    void onDetached();

    void onTextChanged(CharSequence charSequence);

    void fetchResults();

    boolean isRequestRunning();

    int getOffset();

    int getTotalEstimatedMatches();

    void setNewSearchString(String searchQuery);

    void initiateNewSearch();

}
