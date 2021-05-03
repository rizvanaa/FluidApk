package com.example.aplikasiku.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.aplikasiku.activity.Debit;
import com.example.aplikasiku.activity.Kebocoran;
import com.example.aplikasiku.R;
import com.example.aplikasiku.activity.Rate;

public class Pemantauan extends AppCompatActivity {
    CardView cvRate, cvDebit, cvKebocoran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemantauan);

        cvRate = findViewById(R.id.Rate);
        cvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(Pemantauan.this, Rate.class);
                startActivity(I);
                finish();
            }
        });

        cvDebit = findViewById(R.id.Debit);
        cvDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(Pemantauan.this, Debit.class);
                startActivity(I);
                finish();
            }
        });

        cvKebocoran =  findViewById(R.id.Kebocoran);
        cvKebocoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(Pemantauan.this, Kebocoran.class);
                startActivity(I);
                finish();
            }
        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("PEMANTAUAN");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(Pemantauan.this, MainActivity.class);
        finish();
        startActivity(i);
        super.onBackPressed();
    }
}