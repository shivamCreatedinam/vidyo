package com.createdinam.vidyo.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetPostFromWeb {

    @GET("service/")
    Call<List<PostModel>> getPost();
}
