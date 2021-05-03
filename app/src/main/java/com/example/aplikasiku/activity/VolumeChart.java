package com.example.aplikasiku.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.aplikasiku.IndexAxisValueFormatter;
import com.example.aplikasiku.MyMarkerView;
import com.example.aplikasiku.R;
import com.example.aplikasiku.apihelper.RetrofitClient;
import com.example.aplikasiku.apiinterface.BaseApiService;
import com.example.aplikasiku.fragment.ChartA;
import com.example.aplikasiku.fragment.ChartB;
import com.example.aplikasiku.fragment.ChartC;
import com.example.aplikasiku.fragment.ChartD;
import com.example.aplikasiku.fragment.ChartPusat;
import com.example.aplikasiku.fragment.VolumChartA;
import com.example.aplikasiku.fragment.VolumChartB;
import com.example.aplikasiku.fragment.VolumChartC;
import com.example.aplikasiku.fragment.VolumChartD;
import com.example.aplikasiku.fragment.VolumChartPusat;
import com.example.aplikasiku.model.DataRate;
import com.example.aplikasiku.model.DataRateRealtime;
import com.example.aplikasiku.model.DataVolume;
import com.example.aplikasiku.model.RateRealtimeResponse;
import com.example.aplikasiku.model.VolumeResponse;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aplikasiku.apiinterface.DataInterface.DateDataFormat;
import static com.example.aplikasiku.apiinterface.DataInterface.DateFormatChart;
import static com.example.aplikasiku.apiinterface.DataInterface.myDateFormat;

public class VolumeChart extends AppCompatActivity {
    LineData lineData;
    float yvalue;
    String extra, tglIni;
    LimitLine upper, lower;
    Calendar mCalendar;
    List<DataVolume> dataList;
    ProgressDialog progressDialog;
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter2 viewPagerAdapter2;
    int index;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_chart);

        Date c = Calendar.getInstance().getTime();
        mCalendar = Calendar.getInstance();
        tglIni = myDateFormat.format(c).toString();

        tabLayout = findViewById(R.id.tabLayoutKode);
        viewPager = findViewById(R.id.viewPagerKode);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

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
        viewPagerAdapter2= new ViewPagerAdapter2(getSupportFragmentManager());
        viewPagerAdapter2.addFrag(new VolumChartA(),"Chart A");
        viewPagerAdapter2.addFrag(new VolumChartB(),"Chart B");
        viewPagerAdapter2.addFrag(new VolumChartC(),"Chart C");
        viewPagerAdapter2.addFrag(new VolumChartD(),"Chart D");
        viewPagerAdapter2.addFrag(new VolumChartPusat(),"Chart Pusat");
        viewPager.setAdapter(viewPagerAdapter2);

    }

    class ViewPagerAdapter2 extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter2(FragmentManager manager) {
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



//    public void pdfdownload(View view) {
//        new SweetAlertDialog(VolumeChart.this, SweetAlertDialog.NORMAL_TYPE)
//                .setTitleText("Anda yakin untuk menyimpan data pemantauan Volume Air ?")
//                .setConfirmText("Simpan")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(final SweetAlertDialog sweetAlertDialog) {
//                        progressDialog = new ProgressDialog(VolumeChart.this);
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
//                            table.addCell(listVolumeA.get(i));
//                            table.addCell(listVolumeB.get(i));
//                            table.addCell(listVolumeC.get(i));
//                            table.addCell(listVolumeD.get(i));
//                            table.addCell(listVolumeP.get(i));
//                        }
//                        try {
//                            File folder = new File(Environment.getExternalStorageDirectory()+ "/Fluid");
//                            if (!folder.exists())
//                                folder.mkdir();
//                            final String pdf = folder.toString() + "/Volume Air_" +tglIni+ ".pdf";
//                            PdfWriter.getInstance(document, new FileOutputStream(pdf));
//                        } catch (FileNotFoundException fileNotFoundException) {
//                            fileNotFoundException.printStackTrace();
//                        } catch (DocumentException e) {
//                            e.printStackTrace();
//                        }
//                        document.open();
//                        try {
//
//                            document.add(JudulText("Data Pemantauan Volume Penggunaan Air"));
//                            document.add(JudulText(tglIni));
//                            document.add(table);
//                        } catch (DocumentException e) {
//                            e.printStackTrace();
//                        }
//                        document.close();
//                        progressDialog.dismiss();
//
//                        sweetAlertDialog.dismissWithAnimation();
//                        Toast.makeText(VolumeChart.this, "Data pemantauan Volume Air Tanggal "+tglIni+" berhasil disimpan , Silahkan Lihat di Penyimpanan internal /Fluid", Toast.LENGTH_LONG).show();
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
    public Paragraph JudulText(String text){
        Font mOrderDetailsTitleFont = new Font(Font.FontFamily.HELVETICA, 16.0f, Font.NORMAL, BaseColor.BLACK);
        Chunk mOrderDetailsTitleChunk = new Chunk(text, mOrderDetailsTitleFont);
        Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
        mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
        mOrderDetailsTitleParagraph.setSpacingAfter(7);
        return mOrderDetailsTitleParagraph;
    }
}