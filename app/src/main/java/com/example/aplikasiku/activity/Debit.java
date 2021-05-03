package com.example.aplikasiku.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.aplikasiku.R;
import com.example.aplikasiku.adapter.RecyclerDataAdapter;
import com.example.aplikasiku.apihelper.RetrofitClient;
import com.example.aplikasiku.apiinterface.BaseApiService;
import com.example.aplikasiku.model.DataRate;
import com.example.aplikasiku.model.DataVolumeRatePerwaktu;
import com.example.aplikasiku.model.perwaktu.RateItem;
import com.example.aplikasiku.model.RateResponse;
import com.example.aplikasiku.model.RealtimeResponse;
import com.example.aplikasiku.model.VolumePerwaktuItem;
import com.example.aplikasiku.model.VolumePerwaktuResponse;
import com.example.aplikasiku.model.perwaktu.PerwaktuResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.opencsv.CSVWriter;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.FileHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aplikasiku.apiinterface.DataInterface.DateDataFormat;

public class Debit extends AppCompatActivity  {
    @BindView(R.id.tglawal)
            CardView cvTglAwal;
            @BindView(R.id.tvtglawal)
                    TextView tvTglAwal;
    @BindView(R.id.tglakhir)
    CardView cvTglAkhir;
    @BindView(R.id.tvtglakhir)
    TextView tvTglAkhir;
    Calendar mCalendar;
    ImageView btnHome;
    Button btnPilih, btnDownload;
    CardView spnGedung, inputTanggal1, inputTanggal2, cvTotal;
    TextView tvTgl1, tvTgl2, titleRate, tvTotal;
    TextView tvNull;
    Spinner listGedung;
//    private String[] judul = {"Rate Air (m³/s)"};
    private String[] judul = {"Volume Air (liter)"};
    LayoutInflater layoutInflater;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView, rvContainer;
    List<RateItem> dataList;
    public ArrayList<String> listRate;
    public ArrayList<String> listWaktu;
    String waktu1, waktu2, gedung, rate, kolom, tabel;
    ProgressDialog progressDialog;
    private int width, height;
    List<String[]> da;
    FloatingActionButton fabChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("PEMANTAUAN PER WAKTU");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Pemantauan.class));
                finish();
            }
        });
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        spnGedung =findViewById(R.id.spn_gedung);
        inputTanggal1 = findViewById(R.id.tglawal);
        inputTanggal2 = findViewById(R.id.tglakhir);
        tvTgl1 = findViewById(R.id.tvtglawal);
        tvTgl2 = findViewById(R.id.tvtglakhir);
        btnHome = findViewById(R.id.btn_home);
        btnPilih = findViewById(R.id.btn_pilih);
        listGedung = findViewById(R.id.lst_gedung);
        tvNull = findViewById(R.id.tvNull);
        titleRate = findViewById(R.id.title_rate);
        rvContainer = findViewById(R.id.rv_datarate);
        btnDownload = findViewById(R.id.btn_cetak);
        fabChart = findViewById(R.id.btnchart);
        tvTotal = findViewById(R.id.tvtotal);
        cvTotal = findViewById(R.id.cv_total);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Debit.this, MainActivity.class);
                startActivity(i);
            }
        });

        ButterKnife.bind(this);
        Date c = Calendar.getInstance().getTime();
        mCalendar = Calendar.getInstance();
        String tglIni = DateDataFormat.format(c).toString();
        Log.i("tgl", "sekarang tanggal : "+tglIni);
        String[] listGdg = new String[]{"--Pilih Gedung--","Gedung Pusat", "Gedung A", "Gedung B", "Gedung C", "Gedung D"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.list_item_spn, listGdg);
        arrayAdapter.setDropDownViewResource(R.layout.list_item_spn);
        listGedung.setAdapter(arrayAdapter);

        waktu1 = "2021-01-01";
        waktu2 = tglIni;
        gedung = "Gedung Pusat";
        kolom = "rateP";
        tabel = "rate_p";
        showTable(kolom, tabel, waktu1, waktu2);
        cvTglAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Debit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mCalendar.set(Calendar.YEAR, year);
                        mCalendar.set(Calendar.MONTH, month);
                        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        tvTglAwal.setText(DateDataFormat.format(mCalendar.getTime()));
                    }
                },
                        mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        cvTglAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Debit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mCalendar.set(Calendar.YEAR, year);
                        mCalendar.set(Calendar.MONTH, month);
                        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        tvTglAkhir.setText(DateDataFormat.format(mCalendar.getTime()));
                    }
                },
                        mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waktu1 = tvTglAwal.getText().toString();
                waktu2 = tvTglAkhir.getText().toString();
                gedung = listGedung.getSelectedItem().toString();
                if (gedung.equals("Gedung Pusat")){
                    kolom = "rateP";
                    tabel = "rate_p";
                    showTable(kolom, tabel, waktu1, waktu2);
                    titleRate.setText("Volume Air (Gedung Pusat)");
                }
                else if (gedung.equals("Gedung A")){
                    kolom = "rateA";
                    tabel = "rate_a";
                    showTable(kolom, tabel, waktu1, waktu2);
                    titleRate.setText("Volume Air (Gedung A)");

                }
                else if (gedung.equals("Gedung B")){
                    kolom = "rateB";
                    tabel = "rate_b";
                    showTable(kolom, tabel, waktu1, waktu2);
                    titleRate.setText("Volume Air (Gedung B)");

                }
                else if (gedung.equals("Gedung C")){
                    kolom = "rateC";
                    tabel = "rate_c";
                    showTable(kolom, tabel, waktu1, waktu2);
                    titleRate.setText("Volume Air (Gedung C)");

                }
                else if (gedung.equals("Gedung D")){
                    kolom = "rateD";
                    tabel = "rate_d";
                    showTable(kolom, tabel, waktu1, waktu2);
                    titleRate.setText("Volume Air (Gedung D)");

                }
                else {
                    Toast.makeText(Debit.this, "Silakan pilih gedung terlebih dahulu!", Toast.LENGTH_SHORT).show();
                }
                Log.i("aaaa", gedung+":"+waktu1 + "/" + waktu2);
            }
        });

        fabChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Debit.this, RatePerwaktuChart.class);
                intent.putExtra("table", tabel);
                intent.putExtra("column", kolom);
                intent.putExtra("waktu1", waktu1);
                intent.putExtra("waktu2", waktu2);
                startActivity(intent);
            }
        });

    }
    public void showTable(String kolom, String tabel, String waktu1, String waktu2){
        progressDialog = new ProgressDialog(Debit.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memuat Data ...");
        progressDialog.show();
        BaseApiService service = RetrofitClient.getClient1().create(BaseApiService.class);
        Call<PerwaktuResponse> call = service.getRatebyDate(kolom, tabel, waktu1, waktu2);
        call.enqueue(new Callback<PerwaktuResponse>() {
            @Override
            public void onResponse(Call<PerwaktuResponse> call, Response<PerwaktuResponse> response) {
                listRate = new ArrayList<>();
                listWaktu = new ArrayList<>();

                if (response.body().isSuccess()){
                    String total = response.body().getData().getTotal();
                    Log.i("asasa", response.body().getMessage());
                    tvTotal.setText("Total : "+total+" Liter");
                    if (response.body().getData() != null) {
                        dataList = (List<RateItem>) response.body().getData().getRate();
                        recyclerView = findViewById(R.id.rv_datarate);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Debit.this));
                        RecyclerDataAdapter adapter = new RecyclerDataAdapter(dataList, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        tvNull.setVisibility(View.GONE);

                        for (int i = 0; i < dataList.size(); i++){
                            listRate.add(dataList.get(i).getRate());
                            listWaktu.add(dataList.get(i).getWaktu());
                        }

                        Log.i("cekdata", listRate.toArray().toString());

                    }
                    else {
                        tvTotal.setText(response.body().getMessage().toString());
                        recyclerView = findViewById(R.id.rv_datarate);
                        tvNull.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PerwaktuResponse> call, Throwable t) {
                tvTotal.setText(t.getMessage().toString());
                recyclerView = findViewById(R.id.rv_datarate);
                tvNull.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                progressDialog.dismiss();
            }
        });

        progressDialog.dismiss();
    }

    public void pdfdownload(View view) {
        new SweetAlertDialog(Debit.this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Anda yakin untuk menyimpan data pemantauan Volume Air "+gedung+" Tanggal "+waktu1+" sampai "+waktu2+" ?")
                .setConfirmText("Simpan")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sweetAlertDialog) {
                        progressDialog = new ProgressDialog(Debit.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Memuat Data ...");
                        progressDialog.show();
                        Document document = new Document();
                        PdfPTable table = new PdfPTable(new float[] { 2, 1 });
                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.getDefaultCell().setFixedHeight(20);
                        table.addCell("Waktu");
                        table.addCell("Volume");
                        table.setHeaderRows(1);
                        PdfPCell[] cells = table.getRow(0).getCells();
                        for (int j=0;j<cells.length;j++){
                            BaseColor myColor = WebColors.getRGBColor("#87D2F3");
                            cells[j].setBackgroundColor(myColor);
                        }
                        for (int i=0;i<listRate.size();i++){
                            table.addCell(listWaktu.get(i));
                            table.addCell(listRate.get(i));
                        }
                        try {
                            File folder = new File(Environment.getExternalStorageDirectory()+ "/Fluid");
                            if (!folder.exists())
                                folder.mkdir();
                            final String pdf = folder.toString() + "/Volume Air_" +gedung+ "_" +waktu1+ "_" +waktu2+ ".pdf";
                            PdfWriter.getInstance(document, new FileOutputStream(pdf));
                        } catch (FileNotFoundException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                        document.open();
                        try {

                            document.add(JudulText("Data Pemantauan Volume Air"));
                            document.add(JudulText(gedung));
                            document.add(JudulText(waktu1+ " - " +waktu2));
                            document.add(table);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                        document.close();
                        progressDialog.dismiss();

                        sweetAlertDialog.dismissWithAnimation();
                        Toast.makeText(Debit.this, "Data pemantauan Volume Air "+gedung+" Tanggal "+waktu1+" sampai "+waktu2+" Silahkan Lihat di Penyimpanan internal /Fluid", Toast.LENGTH_LONG).show();
                    }

                })
                .setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();


    }

    public Paragraph JudulText(String text){
        Font mOrderDetailsTitleFont = new Font(Font.FontFamily.HELVETICA, 16.0f, Font.NORMAL, BaseColor.BLACK);
        Chunk mOrderDetailsTitleChunk = new Chunk(text, mOrderDetailsTitleFont);
        Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
        mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
        mOrderDetailsTitleParagraph.setSpacingAfter(7);
        return mOrderDetailsTitleParagraph;
    }
//    public void showTable(String gedung, String waktu1, String waktu2){
//        progressDialog = new ProgressDialog(Debit.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Memuat Data ...");
//        progressDialog.show();
//        BaseApiService service = RetrofitClient.getClient1().create(BaseApiService.class);
//        Call<VolumePerwaktuResponse> call = service.getVolumeAir(gedung, waktu1, waktu2);
//        call.enqueue(new Callback<VolumePerwaktuResponse>() {
//            @Override
//            public void onResponse(Call<VolumePerwaktuResponse> call, Response<VolumePerwaktuResponse> response) {
//                listRate = new ArrayList<>();
//                listWaktu = new ArrayList<>();
//
//                if (response.body().isSuccess()){
//                    String total = response.body().getData().getTotal();
//                    Log.i("asasa", response.body().getMessage());
//                    tvTotal.setText("Total : "+total+" Liter");
//                    if (response.body().getData() != null) {
//                        dataList = (List<RateItem>) response.body().getData().getRate();
//                        recyclerView = findViewById(R.id.rv_datarate);
//                        recyclerView.setVisibility(View.VISIBLE);
//                        recyclerView.setHasFixedSize(true);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(Debit.this));
//                        RecyclerDataAdapter adapter = new RecyclerDataAdapter(dataList, getApplicationContext());
//                        recyclerView.setAdapter(adapter);
//                        tvNull.setVisibility(View.GONE);
//
//                        for (int i = 0; i < dataList.size(); i++){
//                            listRate.add(dataList.get(i).getRate());
//                            listWaktu.add(dataList.get(i).getWaktu());
//                        }
//
//                        Log.i("cekdata", listRate.toArray().toString());
//
//                    }
//                    else {
//                        tvTotal.setText(response.body().getMessage().toString());
//                        recyclerView = findViewById(R.id.rv_datarate);
//                        tvNull.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//                    }
//                }
//
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<VolumePerwaktuResponse> call, Throwable t) {
//                tvTotal.setText(t.getMessage().toString());
//                recyclerView = findViewById(R.id.rv_datarate);
//                tvNull.setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.GONE);
//
//                progressDialog.dismiss();
//            }
//        });
//
//        progressDialog.dismiss();
//    }
//
//    public void pdfdownload(View view) {
//        new SweetAlertDialog(Debit.this, SweetAlertDialog.NORMAL_TYPE)
//                .setTitleText("Anda yakin untuk menyimpan data pemantauan Volume Air "+gedung+" Tanggal "+waktu1+" sampai "+waktu2+" ?")
//                .setConfirmText("Simpan")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(final SweetAlertDialog sweetAlertDialog) {
//                        progressDialog = new ProgressDialog(Debit.this);
//                        progressDialog.setCancelable(false);
//                        progressDialog.setMessage("Memuat Data ...");
//                        progressDialog.show();
//                        Document document = new Document();
//                        PdfPTable table = new PdfPTable(new float[] { 2, 1 });
//                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                        table.getDefaultCell().setFixedHeight(20);
//                        table.addCell("Waktu");
//                        table.addCell("Volume");
//                        table.setHeaderRows(1);
//                        PdfPCell[] cells = table.getRow(0).getCells();
//                        for (int j=0;j<cells.length;j++){
//                            BaseColor myColor = WebColors.getRGBColor("#87D2F3");
//                            cells[j].setBackgroundColor(myColor);
//                        }
//                        for (int i=0;i<listRate.size();i++){
//                            table.addCell(listWaktu.get(i));
//                            table.addCell(listRate.get(i));
//                        }
//                        try {
//                            File folder = new File(Environment.getExternalStorageDirectory()+ "/Fluid");
//                            if (!folder.exists())
//                                folder.mkdir();
//                            final String pdf = folder.toString() + "/Volume Air_" +gedung+ "_" +waktu1+ "_" +waktu2+ ".pdf";
//                            PdfWriter.getInstance(document, new FileOutputStream(pdf));
//                        } catch (FileNotFoundException fileNotFoundException) {
//                            fileNotFoundException.printStackTrace();
//                        } catch (DocumentException e) {
//                            e.printStackTrace();
//                        }
//                        document.open();
//                        try {
//
//                            document.add(JudulText("Data Pemantauan Volume Air"));
//                            document.add(JudulText(gedung));
//                            document.add(JudulText(waktu1+ " - " +waktu2));
//                            document.add(table);
//                        } catch (DocumentException e) {
//                            e.printStackTrace();
//                        }
//                        document.close();
//                        progressDialog.dismiss();
//
//                        sweetAlertDialog.dismissWithAnimation();
//                        Toast.makeText(Debit.this, "Data pemantauan Volume Air "+gedung+" Tanggal "+waktu1+" sampai "+waktu2+" Silahkan Lihat di Penyimpanan internal /Fluid", Toast.LENGTH_LONG).show();
//                    }
//
//                })
//        .setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                sweetAlertDialog.dismissWithAnimation();
//            }
//        }).show();
//
//
//    }
//
//    public Paragraph JudulText(String text){
//        Font mOrderDetailsTitleFont = new Font(Font.FontFamily.HELVETICA, 16.0f, Font.NORMAL, BaseColor.BLACK);
//        Chunk mOrderDetailsTitleChunk = new Chunk(text, mOrderDetailsTitleFont);
//        Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
//        mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
//        mOrderDetailsTitleParagraph.setSpacingAfter(7);
//        return mOrderDetailsTitleParagraph;
//    }

    public void downloadCsv(View view){
        da = new ArrayList<String[]>();
        da.add(new String[]{"Data Monitoring Rate Air"});
        da.add(new String[]{""});
        da.add(new String[]{"Waktu","Rate Air"});

        for (int j = 0; j < listRate.size(); j++){
            da.add(new String[]{listWaktu.get(j), listRate.get(j)});
        }

        File folder = new File(Environment.getExternalStorageDirectory()+ "/Fluid");
        if (!folder.exists())
            folder.mkdir();
        final String csv = folder.toString() + "/Fluid.csv";

        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(csv));
            writer.writeAll(da); // data is adding to csv
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(Debit.this, Pemantauan.class);
        finish();
        startActivity(i);
        super.onBackPressed();
    }
}