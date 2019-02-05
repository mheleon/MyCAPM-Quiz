package com.mheleon.mycapmquiz;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CapmQAService {

    String API_ROUTE = "/api/capm/capmqa";

    @GET(API_ROUTE)
    Call<List<CapmQA>> getCapmQA();

}
