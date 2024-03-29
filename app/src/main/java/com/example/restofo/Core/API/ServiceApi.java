package com.example.restofo.Core.API;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
public interface ServiceApi {
    @Multipart
    @POST("process_photo")
    Call<ResponseBody> uploadPhoto(@Part MultipartBody.Part photo, @Part("custom_token") RequestBody customToken);
}
