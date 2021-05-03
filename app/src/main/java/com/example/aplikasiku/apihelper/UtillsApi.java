package com.example.aplikasiku.apihelper;

import com.example.aplikasiku.apiinterface.BaseApiService;

public class UtillsApi {
    public static final String BASE_URL_API = "https://pantaukendaliair.com/api/";
    public static BaseApiService getApiService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
