package com.tuke.bakalarka.api;

import com.tuke.bakalarka.model.Competitor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CompetitorApi {
    @POST("api/competitors/register")
    Call<Competitor> register(@Body Competitor competitor);
    @GET("api/competitors/findBy/{id}")
    Call<Competitor> findById(@Path("id") String id);

}
