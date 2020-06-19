package com.createdinam.vidyo.model;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GetPostFromWeb {

    @GET("service/")
    Call<List<PostModel>> getPost();

    @POST("subscribe")
    Call<ResponseBody> notificationSetup(
            @Field("user_email") String user_email,
            @Field("device_token") String device_token,
            @Field("subscribed") String subscribed,
            @Field("device_name") String device_name,
            @Field("os_version") String os_version
    );

}
