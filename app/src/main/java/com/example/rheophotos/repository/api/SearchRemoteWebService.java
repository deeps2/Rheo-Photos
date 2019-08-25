package com.example.rheophotos.repository.api;

import com.example.rheophotos.model.SearchResponse;
import com.example.rheophotos.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchRemoteWebService {
    @GET("search/?count=" + Constants.SEARCH_RESULT_COUNT)
    Call<SearchResponse> searchList(@Query("q") String queryString, @Query("offset") int offset);
}
