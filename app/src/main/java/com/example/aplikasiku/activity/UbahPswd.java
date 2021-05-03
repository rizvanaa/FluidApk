package com.example.aplikasiku.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplikasiku.R;
import com.example.aplikasiku.apihelper.RetrofitClient;
import com.example.aplikasiku.apiinterface.BaseApiService;
import com.example.aplikasiku.model.LoginResponse;
import com.example.aplikasiku.model.UpdatePasswordResponse;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahPswd extends AppCompatActivity {

    EditText oldPsw, newPsw, renewPsw;
    Button btnSave;
    SharedPreferences sharedPreferences;
    String username, op, np, rnp;
    ProgressDialog Loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubah_pswd);

        oldPsw = findViewById(R.id.old_password);
        newPsw = findViewById(R.id.new_password);
        renewPsw = findViewById(R.id.renew_password);
        btnSave = findViewById(R.id.btn_save);

        Loading = new ProgressDialog(UbahPswd.this);
        Loading.setCancelable(false);
        Loading.setMessage("Loading ...");

        sharedPreferences = getSharedPreferences("data_user", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        op = oldPsw.getText().toString();
        np = newPsw.getText().toString();
        rnp = renewPsw.getText().toString();

        Log.i("passs",op+np+rnp);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePsw();
            }
        });


    }
    public void updatePsw(){
        op = oldPsw.getText().toString();
        np = newPsw.getText().toString();
        rnp = renewPsw.getText().toString();

        if(!np.equals(rnp)){
            Toast.makeText(UbahPswd.this,"Password Not matching",Toast.LENGTH_LONG).show();
            renewPsw.setError("Password Not matching");
            return;
        }
        if(op.isEmpty() || np.isEmpty()|| rnp.isEmpty()){
            Toast.makeText(UbahPswd.this, "Data Harus Lengkap!", Toast.LENGTH_SHORT).show();
            Loading.dismiss();
        }
        else {
            new SweetAlertDialog(UbahPswd.this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("Anda yakin mengubah password anda?")
                    .setConfirmText("Ya")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(final SweetAlertDialog sweetAlertDialog) {
                            username = sharedPreferences.getString("username", null);
                            BaseApiService baseApiService = RetrofitClient.getClient1().create(BaseApiService.class);
                            Call<UpdatePasswordResponse> call = baseApiService.updatePassword(username,op,rnp);
                            call.enqueue(new Callback<UpdatePasswordResponse>() {
                                @Override
                                public void onResponse(Call<UpdatePasswordResponse> call, Response<UpdatePasswordResponse> response) {
                                    if (response.body().isSuccess()){
                                        sharedPreferences = getSharedPreferences("data_user", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("sudahLogin", false);
                                        editor.apply();
                                        Toast.makeText(UbahPswd.this, "Anda harus login terlebih dahulu!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(UbahPswd.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                    else {
                                        new SweetAlertDialog(UbahPswd.this, SweetAlertDialog.NORMAL_TYPE)
                                                .setTitleText("Password yang anda masukkan salah!")
                                                .setConfirmText("Kembali").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        })
                                                .show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<UpdatePasswordResponse> call, Throwable t) {
                                    new SweetAlertDialog(UbahPswd.this, SweetAlertDialog.NORMAL_TYPE)
                                            .setTitleText(t.getMessage())
                                            .setConfirmText("Kembali").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                            .show();
                                }
                            });


                            sweetAlertDialog.dismissWithAnimation();
                        }

                    })
                    .setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }).show();
        }
    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(UbahPswd.this, Akun.class);
        finish();
        startActivity(i);
        super.onBackPressed();
    }
}