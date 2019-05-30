package com.developer.webservice.retrofit;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface RetroInterface {


    @FormUrlEncoded
    @POST
    Call<String> getPostAsString(@Url String url, @FieldMap HashMap<String, String> param);


    @Multipart
    @POST
    Call<String> getPostAsStringWithPart(@Url String url, @PartMap HashMap<String, RequestBody> param, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST
    Call<String> getPostAsStringWithFile(@Url String url, @PartMap HashMap<String, RequestBody> param, @Part MultipartBody.Part files);

    @Multipart
    @POST
    Call<String> getPartAsString(@Url String url, @Part List<MultipartBody.Part> files);


    @GET
    Call<String> getGetAsString(@Url String url);

}
