package com.example.aplikasiku.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikasiku.ClaimsXAxisValueFormatter;
import com.example.aplikasiku.IndexAxisValueFormatter;
import com.example.aplikasiku.MyMarkerView;
import com.example.aplikasiku.R;
import com.example.aplikasiku.apihelper.RetrofitClient;
import com.example.aplikasiku.apiinterface.BaseApiService;
import com.example.aplikasiku.apiinterface.DataInterface;
import com.example.aplikasiku.fragment.ChartA;
import com.example.aplikasiku.fragment.ChartB;
import com.example.aplikasiku.fragment.ChartC;
import com.example.aplikasiku.fragment.ChartD;
import com.example.aplikasiku.fragment.ChartPusat;
import com.example.aplikasiku.model.ChartRateRes;
import com.example.aplikasiku.model.DataRateRealtime;
import com.example.aplikasiku.model.DataVolume;
import com.example.aplikasiku.model.RateRealtimeResponse;
import com.example.aplikasiku.model.RealtimeResponse;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aplikasiku.apiinterface.DataInterface.DateDataFormat;
import static com.example.aplikasiku.apiinterface.DataInterface.DateFormatChart;
import static com.example.aplikasiku.apiinterface.DataInterface.formatwaktu;
import static com.example.aplikasiku.apiinterface.DataInterface.myDateFormat;

public class RateRealtimeChart extends AppCompatActivity {
//    LineChart lineChart;
//    LineChart lineChartB;
    LineDataSet lineDataSetA = new LineDataSet(null,null);
    LineDataSet lineDataSetB = new LineDataSet(null,null);
    LineDataSet lineDataSetC = new LineDataSet(null,null);
    LineDataSet lineDataSetD = new LineDataSet(null,null);
    LineDataSet lineDataSetP = new LineDataSet(null,null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData lineData;
    float yvalue;
    String extra, tglIni;
    LimitLine upper, lower;
    Calendar mCalendar;
    List<DataRateRealtime> dataRateRealtimes;
    public ArrayList<String> listRateA;
    public ArrayList<String> listRateB;
    public ArrayList<String> listRateC;
    public ArrayList<String> listRateD;
    public ArrayList<String> listRateP;
    public ArrayList<String> listWaktu;
    ProgressDialog progressDialog;
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_realtime_chart);

//        lineChart = findViewById(R.id.chart);
////        lineChartB = findViewById(R.id.chartB);
//        lineChart.setNoDataText("Data Grafik Tidak Tersedia.");
//        lineChart.setNoDataTextColor(Color.rgb(3,169,244));

//        lineChartB.setNoDataText("Data Grafik Tidak Tersedia.");
//        lineChartB.setNoDataTextColor(Color.rgb(3,169,244));

        tabLayout = findViewById(R.id.tabLayoutKode);
        viewPager = findViewById(R.id.viewPagerKode);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        //custom color icon and text tab layout
        index= tabLayout.getSelectedTabPosition();
        ((TextView) tabLayout.getTabAt(index).getCustomView()).setTextColor(getResources().getColor(R.color.white));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView()).setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView()).setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Date c = Calendar.getInstance().getTime();
        mCalendar = Calendar.getInstance();
        tglIni = myDateFormat.format(c).toString();
//        getData();


    }

    private void setupTabIcons() {
        TextView tv_chart_a = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_chart_a.setText("Chart A");
        tv_chart_a.setTextSize(16);
//        tv_chart_a.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_koupon, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tv_chart_a);

        TextView tv_chart_b = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_chart_b.setText("Chart B");
        tv_chart_b.setTextSize(16);
//        tv_chart_b.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refferal, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tv_chart_b);

        TextView tv_chart_c = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_chart_c.setText("Chart C");
        tv_chart_c.setTextSize(16);
//        tv_chart_b.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refferal, 0, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tv_chart_c);

        TextView tv_chart_d = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_chart_d.setText("Chart D");
        tv_chart_d.setTextSize(16);
//        tv_chart_b.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refferal, 0, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tv_chart_d);

        TextView tv_chart_p = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tv_chart_p.setText("Chart Pusat");
        tv_chart_p.setTextSize(16);
//        tv_chart_b.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refferal, 0, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tv_chart_p);

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter= new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new ChartA(),"Chart A");
        viewPagerAdapter.addFrag(new ChartB(),"Chart B");
        viewPagerAdapter.addFrag(new ChartC(),"Chart C");
        viewPagerAdapter.addFrag(new ChartD(),"Chart D");
        viewPagerAdapter.addFrag(new ChartPusat(),"Chart Pusat");
        viewPager.setAdapter(viewPagerAdapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
//    public void getData(){
//        BaseApiService service = RetrofitClient.getClient1().create(BaseApiService.class);
//        Call<RateRealtimeResponse> call = service.getRealtimeRate();
//        ArrayList<Entry> DataValsA = new ArrayList<Entry>();
//        ArrayList<Entry> DataValsB = new ArrayList<Entry>();
//        ArrayList<Entry> DataValsC = new ArrayList<Entry>();
//        ArrayList<Entry> DataValsD = new ArrayList<Entry>();
//        ArrayList<Entry> DataValsP = new ArrayList<Entry>();
//        listRateA = new ArrayList<>();
//        listRateB = new ArrayList<>();
//        listRateC = new ArrayList<>();
//        listRateD = new ArrayList<>();
//        listRateP = new ArrayList<>();
//        listWaktu = new ArrayList<>();
//        call.enqueue(new Callback<RateRealtimeResponse>() {
//            @Override
//            public void onResponse(Call<RateRealtimeResponse> call, Response<RateRealtimeResponse> response) {
//                if (response.body().isSuccess()){
//                    dataRateRealtimes = response.body().getData();
//                    for (int i = 0; i < dataRateRealtimes.size(); i++){
//                        listRateA.add(dataRateRealtimes.get(i).getRateA());
//                        listRateB.add(dataRateRealtimes.get(i).getRateB());
//                        listRateC.add(dataRateRealtimes.get(i).getRateC());
//                        listRateD.add(dataRateRealtimes.get(i).getRateD());
//                        listRateP.add(dataRateRealtimes.get(i).getRateP());
//                        listWaktu.add(String.format(dataRateRealtimes.get(i).getWaktu(), myDateFormat));
//                    }
//                    Log.i("adaa22", response.body().getData().toString());
//                        for (int i = 0; i < dataRateRealtimes.size(); i++) {
//                            DataRateRealtime x = dataRateRealtimes.get(i);
//
//                            lineDataSetA.setLabel("Rate Air A");
//                            lineDataSetB.setLabel("Rate Air B");
//                            lineDataSetC.setLabel("Rate Air C");
//                            lineDataSetD.setLabel("Rate Air D");
//                            lineDataSetP.setLabel("Rate Air Pusat");
//                            upper = new LimitLine(10f, "Batas Atas");
//                            lower = new LimitLine(0f, "Batas Bawah");
//                            Float rateA = Float.parseFloat(x.getRateA());
//                            Float rateB = Float.parseFloat(x.getRateB());
//                            Float rateC = Float.parseFloat(x.getRateC());
//                            Float rateD = Float.parseFloat(x.getRateD());
//                            Float rateP = Float.parseFloat(x.getRateP());
//
//                            Date newDate = null;
//                            try {
//                                newDate = DateFormatChart.parse(String.valueOf(x.getWaktu()));
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//
//                            DataValsA.add(new Entry(newDate.getTime(), rateA));
//                            DataValsB.add(new Entry(newDate.getTime(), rateB));
//                            DataValsC.add(new Entry(newDate.getTime(), rateC));
//                            DataValsD.add(new Entry(newDate.getTime(), rateD));
//                            DataValsP.add(new Entry(newDate.getTime(), rateP));
//
//                        }
//                    }
//                else {
//
//                    DataValsA.add(null);
//                    DataValsB.add(null);
//                    DataValsC.add(null);
//                    DataValsD.add(null);
//                    DataValsP.add(null);
//                }
//                }
//
//
//            @Override
//            public void onFailure(Call<RateRealtimeResponse> call, Throwable t) {
//
//            }
//        });
//
//    }

//
//    public void pdfdownload(View view) {
//        new SweetAlertDialog(RateRealtimeChart.this, SweetAlertDialog.NORMAL_TYPE)
//                .setTitleText("Anda yakin untuk menyimpan data pemantauan Rate Air ?")
//                .setConfirmText("Simpan")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(final SweetAlertDialog sweetAlertDialog) {
//                        progressDialog = new ProgressDialog(RateRealtimeChart.this);
//                        progressDialog.setCancelable(false);
//                        progressDialog.setMessage("Memuat Data ...");
//                        progressDialog.show();
//                        Document document = new Document();
//                        PdfPTable table = new PdfPTable(new float[] { 5, 2,2,2,2,2 });
//                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                        table.getDefaultCell().setFixedHeight(20);
//                        table.addCell("Waktu");
//                        table.addCell("GedungA");
//                        table.addCell("GedungB");
//                        table.addCell("GedungC");
//                        table.addCell("GedungD");
//                        table.addCell("Pusat");
//                        table.setHeaderRows(1);
//                        PdfPCell[] cells = table.getRow(0).getCells();
//                        for (int j=0;j<cells.length;j++){
//                            BaseColor myColor = WebColors.getRGBColor("#87D2F3");
//                            cells[j].setBackgroundColor(myColor);
//                        }
//                        for (int i=0;i<listWaktu.size();i++){
//
//                            Log.i("dddd", listWaktu.get(i));
//
//                            table.addCell(listWaktu.get(i));
//                            table.addCell(listRateA.get(i));
//                            table.addCell(listRateB.get(i));
//                            table.addCell(listRateC.get(i));
//                            table.addCell(listRateD.get(i));
//                            table.addCell(listRateP.get(i));
//                        }
//                        try {
//                            File folder = new File(Environment.getExternalStorageDirectory()+ "/Fluid");
//                            if (!folder.exists())
//                                folder.mkdir();
//                            final String pdf = folder.toString() + "/Rate Air_" +tglIni+ ".pdf";
//                            PdfWriter.getInstance(document, new FileOutputStream(pdf));
//                        } catch (FileNotFoundException fileNotFoundException) {
//                            fileNotFoundException.printStackTrace();
//                        } catch (DocumentException e) {
//                            e.printStackTrace();
//                        }
//                        document.open();
//                        try {
//
//                            document.add(JudulText("Data Pemantauan Laju Penggunaan Air"));
//                            document.add(JudulText(tglIni));
//                            document.add(table);
//                        } catch (DocumentException e) {
//                            e.printStackTrace();
//                        }
//                        document.close();
//                        progressDialog.dismiss();
//
//                        sweetAlertDialog.dismissWithAnimation();
//                        Toast.makeText(RateRealtimeChart.this, "Data pemantauan Volume Air Tanggal "+tglIni+" berhasil disimpan , Silahkan Lihat di Penyimpanan internal /Fluid", Toast.LENGTH_LONG).show();
//                    }
//
//                })
//                .setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        sweetAlertDialog.dismissWithAnimation();
//                    }
//                }).show();
//
//
//    }
//    public Paragraph JudulText(String text){
//        Font mOrderDetailsTitleFont = new Font(Font.FontFamily.HELVETICA, 16.0f, Font.NORMAL, BaseColor.BLACK);
//        Chunk mOrderDetailsTitleChunk = new Chunk(text, mOrderDetailsTitleFont);
//        Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
//        mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
//        mOrderDetailsTitleParagraph.setSpacingAfter(7);
//        return mOrderDetailsTitleParagraph;
//    }

    private void getDataRateAll(String column, String table,List<ChartRateRes.Datum> list){
        BaseApiService service = RetrofitClient.getClient1().create(BaseApiService.class);
        Call<ChartRateRes> call = service.getRateChart(column,table);
        call.enqueue(new Callback<ChartRateRes>() {
            @Override
            public void onResponse(Call<ChartRateRes> call, Response<ChartRateRes> response) {
                if(response.code() == 200){
                    for(ChartRateRes.Datum data : response.body().getData()){
                        list.add(data);
                    }
                }
            }

            @Override
            public void onFailure(Call<ChartRateRes> call, Throwable t) {

            }
        });
    }
}