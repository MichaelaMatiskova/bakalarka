package com.tuke.bakalarka.api;

import com.tuke.bakalarka.model.Competitor;
import com.tuke.bakalarka.model.Score;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ScoreApi {
    @POST("api/score/add")
    Call<Score> addScore(@Body Score score);
}
