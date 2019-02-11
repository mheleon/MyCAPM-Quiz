package com.mheleon.mycapmquiz.controllers;

import com.mheleon.mycapmquiz.models.CapmQA;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CapmQAService {

    String API_ROUTE = "/api/pmp/beta16845";

    @GET(API_ROUTE)
    Call<RealmList<CapmQA>> getCapmQA();

}
