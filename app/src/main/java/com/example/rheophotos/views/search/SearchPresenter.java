package com.example.rheophotos.views.search;

import android.text.TextUtils;
import android.view.View;

import com.example.rheophotos.R;
import com.example.rheophotos.model.SearchResponse;
import com.example.rheophotos.repository.SearchRepository;

import java.util.List;

public class SearchPresenter implements SearchMvpPresenter, SearchRepoInterface {
    private SearchMvpView searchMvpView;
    private SearchRepository searchRepository;

    @Override
    public void onAttached(SearchMvpView searchMvpView) {
        this.searchMvpView = searchMvpView;
        searchRepository = new SearchRepository(this);
    }

    @Override
    public void onDetached() {
        this.searchMvpView = null;
        searchRepository.cancelRequest();
    }

    @Override
    public void onTextChanged(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence.toString())) {
            searchMvpView.showOrHideCrossIcon(View.GONE);
        } else {
            searchMvpView.showOrHideCrossIcon(View.VISIBLE);
        }
    }

    @Override
    public void fetchResults() {
        searchRepository.fetchResults();
    }

    @Override
    public void onValidResultsReceived(List<SearchResponse.Results> resultsList) {
        if (searchMvpView != null)
            searchMvpView.displayResultsOnUI(resultsList);
    }

    @Override
    public boolean isRequestRunning() {
        return searchRepository.isRequestRunning();
    }

    @Override
    public int getOffset() {
        return searchRepository.getOffset();
    }

    @Override
    public int getTotalEstimatedMatches() {
        return searchRepository.getTotalEstimatedMatches();
    }

    @Override
    public void setNewSearchString(String searchQuery) {
        searchRepository.setSearchQueryString(searchQuery);
    }

    @Override
    public void initiateNewSearch() {
        searchRepository.clearVariables();
        fetchResults();
    }

    @Override
    public void onRequestStarted() {
        searchMvpView.showProgressForNetworkRequest();
    }

    @Override
    public void onRequestFinished() {
        if (searchMvpView != null)
            searchMvpView.hideProgressBarForNetworkRequest();
    }

    @Override
    public void onNoResultsFoundInVeryFirstCall() {
        if (searchMvpView != null)
            searchMvpView.showMessage(R.drawable.no_results, R.string.no_results_found);
    }

    @Override
    public void onResponseUnsuccessful(int offset) {
        showErrorMessage(offset, false);
    }

    @Override
    public void onNonNetworkErrorOccurred(int offset) {
        showErrorMessage(offset, false);
    }

    @Override
    public void onNetworkErrorOccurred(int offset) {
        showErrorMessage(offset, true);
    }

    private void showErrorMessage(int offset, boolean isNetworkError) {
        if (searchMvpView == null)
            return;

        int drawableID = R.drawable.generic_error;
        int stringID = R.string.generic_error_msg;
        if (isNetworkError) {
            drawableID = R.drawable.no_internet;
            stringID = R.string.no_internet_msg;
        }

        if (offset == 0)
            searchMvpView.showMessage(drawableID, stringID);
        else
            searchMvpView.showToast(stringID);
    }


}
