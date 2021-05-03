package com.example.aplikasiku.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplikasiku.R;
import com.example.aplikasiku.apihelper.RetrofitClient;
import com.example.aplikasiku.apihelper.UtillsApi;
import com.example.aplikasiku.apiinterface.BaseApiService;
import com.example.aplikasiku.model.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Activity {
    private String id, username, password, nama;
    EditText etUsername, etPassword;
    Button btnLogin;
    ProgressDialog Loading;
    Context mContext;
    BaseApiService mApiService;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sharedPreferences = getSharedPreferences("data_user", Context.MODE_PRIVATE);
        mContext = this;
        mApiService = UtillsApi.getApiService(); // meng-init yang ada di package apihelper
        initComponents();
        id = sharedPreferences.getString("id", null);
        username = sharedPreferences.getString("username", null);
        nama = sharedPreferences.getString("nama", null);
        password = sharedPreferences.getString("password", null);
        boolean sudahLogin = sharedPreferences.getBoolean("sudahLogin", false);


        Loading = new ProgressDialog(Login.this);
        Loading.setCancelable(false);
        Loading.setMessage("Loading ...");

    }

    private void initComponents() {

        etUsername = (EditText) findViewById(R.id.txt_username);
        etPassword = (EditText) findViewById(R.id.txt_password);

        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestLogin();
            }
        });
    }

    private void requestLogin(){
        Loading.show();
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(Login.this, "Data Harus Lengkap!", Toast.LENGTH_SHORT).show();
            Loading.dismiss();
        }else {
            BaseApiService service = RetrofitClient.getClient1().create(BaseApiService.class);
            Call<LoginResponse> call = service.loginRequest(username, password);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.body().isSuccess()) {
                        Loading.dismiss();
                        sharedPreferences = getSharedPreferences("data_user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("sudahLogin", true);
                        editor.putString("nama", response.body().getData().getNama());
                        editor.putString("username", response.body().getData().getUsername());
                        editor.putString("id", response.body().getData().getId());
                        editor.apply();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(Login.this, "Login Succes", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Loading.dismiss();
                        Loading.cancel();
                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    Loading.dismiss();
                    Loading.cancel();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(Login.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                    Loading.dismiss();
                    Loading.cancel();
                }

            });
            Loading.cancel();
            Loading.dismiss();
        }

    }
}
