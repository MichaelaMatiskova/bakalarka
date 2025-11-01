package com.tuke.bakalarka.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QrCodeApi {
    @GET("used/{id}")
    Call<Boolean> isUsed(@Path("id") String id);
}
