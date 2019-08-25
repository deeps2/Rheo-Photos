package com.example.rheophotos.repository;

import androidx.annotation.NonNull;

import com.example.rheophotos.model.SearchResponse;
import com.example.rheophotos.repository.api.RestClient;
import com.example.rheophotos.repository.api.SearchRemoteWebService;
import com.example.rheophotos.repository.local.RheoRoomDBHelper;
import com.example.rheophotos.repository.local.SearchLocal;
import com.example.rheophotos.repository.local.SearchTable;
import com.example.rheophotos.utils.AppUtils;
import com.example.rheophotos.utils.Constants;
import com.example.rheophotos.utils.ExecutorUtils;
import com.example.rheophotos.views.search.SearchRepoInterface;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Repository class
//This will fetch the data either from API or locally
public class SearchRepository implements Callback<SearchResponse> {
    private int offset = 0;
    private int totalEstimatedMatches = 1;
    private boolean requestRunning = false;
    private Call<SearchResponse> call;
    private String searchQueryString = "";
    private SearchRepoInterface searchRepoInterface;
    private SearchRemoteWebService searchRemoteWebService;

    public SearchRepository(SearchRepoInterface searchRepoInterface) {
        this.searchRepoInterface = searchRepoInterface;
    }

    public void clearVariables() {
        offset = 0;
        totalEstimatedMatches = 1;
        requestRunning = false;
        if (call != null)
            call.cancel();
    }

    public void setSearchQueryString(String searchQueryString) {
        this.searchQueryString = searchQueryString;
    }

    public boolean isRequestRunning() {
        return requestRunning;
    }

    public int getOffset() {
        return offset;
    }

    public int getTotalEstimatedMatches() {
        return totalEstimatedMatches;
    }

    public void cancelRequest() {
        if (call != null)
            call.cancel();
    }

    public void fetchResults() {
        ExecutorUtils.getInstance().diskIO().execute(() -> {
            requestRunning = true;

            List<SearchTable> searchTableList = RheoRoomDBHelper.getInstance().searchDao().getResult(searchQueryString.toLowerCase(), offset);
            if (searchTableList == null || searchTableList.isEmpty()) {
                //no result found in table, fetch from API
                ExecutorUtils.getInstance().mainThread().execute(this::makeAPIRequest);

            } else {
                //result found in table
                //only 1 response will be present in DB for a particular searchQueryString and offset
                SearchResponse searchResponse = (SearchResponse) AppUtils.getObject(searchTableList.get(0).searchResult, SearchResponse.class);
                if (searchResponse == null) {
                    requestRunning = false;
                    return;
                }

                //update last access time
                RheoRoomDBHelper.getInstance().searchDao().updateLastAccessTime(System.currentTimeMillis(), searchTableList.get(0).id);

                //pass the result set to main thread
                ExecutorUtils.getInstance().mainThread().execute(() -> {
                    requestRunning = false;
                    onValidResultsReceived(searchResponse);
                });
            }
        });
    }

    private void makeAPIRequest() {
        call = getWebService().searchList(searchQueryString, offset);
        searchRepoInterface.onRequestStarted();
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
        onNetworkRequestFinished();

        if (!response.isSuccessful()) {
            searchRepoInterface.onResponseUnsuccessful(offset);
            return;
        }

        SearchResponse searchResponse = response.body();
        if (isResponseEmpty(searchResponse)) {
            notifyPresenterNoResponse();
            return;
        }

        int currentOffset = offset;
        onValidResultsReceived(searchResponse);
        makeChangesInDatabase(currentOffset, searchResponse);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull Throwable t) {
        onNetworkRequestFinished();
        if (call.isCanceled()) {
            //do nothing as call might be canceled because activity is destroyed
        } else if (t instanceof IOException && Constants.ERROR_NO_NET.equals(t.getMessage())) {
            searchRepoInterface.onNetworkErrorOccurred(offset);
        } else {
            searchRepoInterface.onNonNetworkErrorOccurred(offset);
        }
    }

    private boolean isResponseEmpty(SearchResponse searchResponse) {
        return (searchResponse == null || searchResponse.getResultsList() == null || searchResponse.getResultsList().isEmpty());
    }

    private void notifyPresenterNoResponse() {
        if (offset == 0)
            searchRepoInterface.onNoResultsFoundInVeryFirstCall();
    }

    private void onNetworkRequestFinished() {
        requestRunning = false;
        searchRepoInterface.onRequestFinished();
    }

    private void onValidResultsReceived(SearchResponse searchResponse) {
        offset = searchResponse.getNextOffset();
        totalEstimatedMatches = searchResponse.getTotalEstimatedMatches();
        searchRepoInterface.onValidResultsReceived(searchResponse.getResultsList());
    }

    //insert new record in DB and delete oldest record if MAX_ROWS size is reached
    private void makeChangesInDatabase(int currentOffset, SearchResponse searchResponse) {
        ExecutorUtils.getInstance().diskIO().execute(() -> {
            SearchTable searchTable = SearchTable.getObject(searchQueryString.toLowerCase(),
                    currentOffset,
                    AppUtils.getSerialisedStringFromObject(searchResponse),
                    System.currentTimeMillis());

            if (SearchLocal.getInstance().getTotalRows() == SearchTable.MAX_ROWS) {
                RheoRoomDBHelper.getInstance().searchDao().deleteOldestRecord();
                SearchLocal.getInstance().decrementTotalRows();
            }

            RheoRoomDBHelper.getInstance().searchDao().insert(searchTable);
            SearchLocal.getInstance().incrementTotalRows();
        });
    }

    private SearchRemoteWebService getWebService() {
        if (searchRemoteWebService == null)
            searchRemoteWebService = RestClient.getInstance().getRestClient().create(SearchRemoteWebService.class);
        return searchRemoteWebService;
    }
}
