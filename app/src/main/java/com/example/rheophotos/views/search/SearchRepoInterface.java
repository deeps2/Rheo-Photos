package com.example.rheophotos.views.search;

import com.example.rheophotos.model.SearchResponse;

import java.util.List;

//interface between SearchPresenter and SearchRepository
//implemented by SearchPresenter
public interface SearchRepoInterface {

    void onRequestStarted();

    void onRequestFinished();

    void onResponseUnsuccessful(int offset);

    void onNoResultsFoundInVeryFirstCall();

    void onNetworkErrorOccurred(int offset);

    void onNonNetworkErrorOccurred(int offset);

    void onValidResultsReceived(List<SearchResponse.Results> resultsList);

}
