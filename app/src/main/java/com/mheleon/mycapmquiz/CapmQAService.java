package com.mheleon.mycapmquiz;

import java.util.List;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CapmQAService {

    String API_ROUTE = "/api/capm/capmqa";

    @GET(API_ROUTE)
    Call<RealmList<CapmQA>> getCapmQA();

}
