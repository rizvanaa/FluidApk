package com.example.aplikasiku.apiinterface;

import com.example.aplikasiku.model.ChartRateRes;
import com.example.aplikasiku.model.ControllResponse;
import com.example.aplikasiku.model.KebocoranResponse;
import com.example.aplikasiku.model.LoginResponse;
import com.example.aplikasiku.model.RealtimeResponse;
import com.example.aplikasiku.model.StatusButtonResponse;
import com.example.aplikasiku.model.UpdatePasswordResponse;
import com.example.aplikasiku.model.VolumePerwaktuResponse;
import com.example.aplikasiku.model.VolumeResponse;
import com.example.aplikasiku.model.perwaktu.PerwaktuResponse;
import com.example.aplikasiku.model.raterealtime.RateRealtimeResponse;
import com.example.aplikasiku.model.volumerealtime.VolumeRealtimeResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface BaseApiService {
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse>loginRequest(@Field("username")String username,
                                    @Field("password")String password);
    @FormUrlEncoded
    @POST("update-password.php")
    Call<UpdatePasswordResponse>updatePassword(@Field("username")String username,
                                               @Field("password")String password,
                                               @Field("password_baru")String passwordbaru);

    @GET("realtime.php")
    Call<RealtimeResponse>getRealtime(@Query("gedung")String gedung);
//    @GET("rate-air.php")
//    Call<RateResponse>getRateAir(@Query("gedung")String gedung,
//                                       @Query("waktu1")String waktu1,
//                                       @Query("waktu2")String waktu2);
    @GET("rate-air.php")
    Call<VolumePerwaktuResponse>getVolumeAir(@Query("gedung")String gedung,
                                           @Query("waktu1")String waktu1,
                                           @Query("waktu2")String waktu2);
    @GET("bocor.php")
    Call<KebocoranResponse>getKebocoranAir(@Query("gedung")String gedung,
                                                 @Query("waktu1")String waktu1,
                                                 @Query("waktu2")String waktu2);
    @GET("status-button.php")
    Call<StatusButtonResponse>getStatusButton(@Query("gedung")String gedung);

    @FormUrlEncoded
    @POST("button-control.php")
    Call<ControllResponse>setStatusButton(@Field("status") String status,
                                          @Field("gedung") String idGedung);
    @GET("realtime-chart.php")
    Call<RateRealtimeResponse>getRealtimeRate();
//    @GET("volume.php")
//    Call<VolumeResponse>getVolumeChart();

    @GET("realtime-chart.php")
    Call<ChartRateRes> getRateChart(@Query("column")String column,
                                    @Query("table")String table);

    @GET("volume.php")
    Call<VolumeResponse> getVolumChart(@Query("id_gedung") String id_gedung);

//    @GET("rate-air.php")
//    Call<RatePerwaktuRes> getRatebyDate(@Query("column") String column,
//                                        @Query("table") String table,
//                                        @Query("waktu1") String waktu1,
//                                        @Query("waktu2") String waktu2);
    @GET("rate-air.php")
    Call<PerwaktuResponse> getRatebyDate(@Query("column") String column,
                                         @Query("table") String table,
                                         @Query("waktu1") String waktu1,
                                         @Query("waktu2") String waktu2);
    @GET("realtime-chart.php")
    Call<RateRealtimeResponse> getRateRealtime(@Query("column")String column,
                                            @Query("table")String table);

    @GET("volume.php")
    Call<VolumeRealtimeResponse> getVolumeRealtime(@Query("id_gedung") String id_gedung);

}
