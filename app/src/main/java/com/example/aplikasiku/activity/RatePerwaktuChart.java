package com.example.aplikasiku.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.aplikasiku.MyMarkerView;
import com.example.aplikasiku.R;
import com.example.aplikasiku.adapter.RecyclerDataAdapter;
import com.example.aplikasiku.apihelper.RetrofitClient;
import com.example.aplikasiku.apiinterface.BaseApiService;
import com.example.aplikasiku.model.DataRate;
import com.example.aplikasiku.model.DataRateRealtime;
import com.example.aplikasiku.model.DataVolumeRatePerwaktu;
import com.example.aplikasiku.model.perwaktu.PerwaktuResponse;
import com.example.aplikasiku.model.perwaktu.RateItem;
import com.example.aplikasiku.model.RatePerwaktuRes;
import com.example.aplikasiku.model.RateRealtimeResponse;
import com.example.aplikasiku.model.RateResponse;
import com.example.aplikasiku.model.VolumePerwaktuItem;
import com.example.aplikasiku.model.VolumePerwaktuResponse;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aplikasiku.apiinterface.DataInterface.DateDataFormat;
import static com.example.aplikasiku.apiinterface.DataInterface.DateFormat;
import static com.example.aplikasiku.apiinterface.DataInterface.DateFormatChart;

public class RatePerwaktuChart extends AppCompatActivity {
    LineChart lineChart;
    BarChart barChart;
    //    LineDataSet lineDataSet = new LineDataSet(null,null);
    //    BarDataSet barDataSet = new BarDataSet(null, "tes");
    //    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    //    LineData lineData;
    float yvalue;
    String gedung, waktu1, waktu2, tglIni, nama, kolom, tabel;
    LimitLine upper, lower;
    Calendar mCalendar;
    public ArrayList < String > listRate;
    public ArrayList < String > listWaktu;
    public ArrayList < String > lisXTime;
    List < RateItem > dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_perwaktu_chart);
        barChart = findViewById(R.id.chart);
        barChart.setNoDataText("Data Grafik Tidak Tersedia.");
        barChart.setNoDataTextColor(Color.rgb(3, 169, 244));
        Bundle bundle = getIntent().getExtras();
        tabel = bundle.getString("table");
        kolom = bundle.getString("column");
        waktu1 = bundle.getString("waktu1");
        waktu2 = bundle.getString("waktu2");
        Log.i("hui", tabel + kolom + waktu1 + waktu2);
        Date c = Calendar.getInstance().getTime();
        mCalendar = Calendar.getInstance();
        tglIni = DateDataFormat.format(c).toString();
        getDataBar(kolom, tabel, waktu1, waktu2);
    }
    public void getDataBar(String tabel, String kolom, String waktu1, String waktu2) {
        BaseApiService service = RetrofitClient.getClient1().create(BaseApiService.class);
        Call < PerwaktuResponse > call = service.getRatebyDate(tabel, kolom, waktu1, waktu2);
        ArrayList < BarEntry > DataVals = new ArrayList < > ();
        call.enqueue(new Callback < PerwaktuResponse > () {
            @Override
            public void onResponse(Call < PerwaktuResponse > call, Response < PerwaktuResponse > response) {
                listRate = new ArrayList < > ();
                listWaktu = new ArrayList < > ();
                lisXTime = new ArrayList < > ();

                if (response.body().isSuccess()) {
                    if (response.body().getData() != null) {
                        dataList = (List < RateItem > ) response.body().getData().getRate();
                        for (int i = 0; i < dataList.size(); i++) {
                            RateItem x = dataList.get(i);
                            Float air = Float.parseFloat((x.getRate()).replace(",", ""));
                            Date newDate = null;
                            lisXTime.add(String.valueOf(dataList.get(i).getWaktu()));
                            try {
                                newDate = DateFormat.parse(String.valueOf(dataList.get(i).getWaktu()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            for (int j = 0; j < dataList.size(); j++) {
                                listRate.add(dataList.get(j).getRate());
                                listWaktu.add(dataList.get(j).getWaktu());
                            }
                            DataVals.add(new BarEntry(newDate.getTime(), air));
                        }
                    } else {

                    }
                }
                ShowChartBar(DataVals, lisXTime);
            }

            @Override
            public void onFailure(Call < PerwaktuResponse > call, Throwable t) {

            }
        });
    }
    private void ShowChartBar(ArrayList < BarEntry > DataVals, ArrayList < String > listTime) {
        ArrayList < IBarDataSet > iBarDataSets = new ArrayList < > ();
        String label = null;
        if (kolom.equals("rateP")) {
            label = "Volume Gedung Pusat";
        } else if (kolom.equals("rateA")) {
            label = "Volume Gedung A";
        } else if (kolom.equals("rateB")) {
            label = "Volume Gedung B";
        } else if (kolom.equals("rateC")) {
            label = "Volume Gedung C";
        } else if (kolom.equals("rateD")) {
            label = "Volume Gedung D";
        }
        BarDataSet barDataSet = new BarDataSet(DataVals, label);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(10f);
        barData.setHighlightEnabled(true);
        barData.setDrawValues(true);
        barDataSet.setValues(DataVals);
        barDataSet.getEntryCount();
        barDataSet.setBarBorderWidth(20f);
        barDataSet.setBarBorderColor(Color.rgb(3, 169, 244));
        barDataSet.setVisible(true);
        barDataSet.setDrawIcons(false);
        barDataSet.setValueTextSize(9f);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormLineDashEffect(new DashPathEffect(new float[] {
                10f, 5f
        }, 0f));
        barDataSet.setFormSize(10f);
        barDataSet.setColor(Color.rgb(3, 169, 244));

        YAxis leftaxisy = barChart.getAxisLeft();
        leftaxisy.removeAllLimitLines();

        //        leftaxisy.setAxisMaximum(100f);
        //        leftaxisy.setAxisMinimum(0f);
        leftaxisy.enableGridDashedLine(10f, 10f, 0f);
        leftaxisy.setDrawZeroLine(true);
        leftaxisy.setDrawLimitLinesBehindData(true);
        leftaxisy.setLabelCount(10, true);
        leftaxisy.setDrawGridLines(true);
        leftaxisy.setCenterAxisLabels(true);
        leftaxisy.setSpaceBottom(0);
        leftaxisy.setXOffset(3f);
        leftaxisy.setDrawTopYLabelEntry(true);
        leftaxisy.setSpaceMax(3f);
        leftaxisy.isForceLabelsEnabled();

        XAxis xAxis = barChart.getXAxis();
        //       xAxis.setGranularity(1f);
        xAxis.enableGridDashedLine(1f, 1f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        //       xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(10,false);
        Log.i("gugug", "size" + listWaktu.size());
        xAxis.isCenterAxisLabelsEnabled();
        xAxis.isForceLabelsEnabled();
        xAxis.setLabelRotationAngle(30f);
        xAxis.setSpaceMax(2f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.i("ururuu", "getFormattedValue: "+listTime.get((int) value));
                return listTime.get((int) value);

            }
        });
        iBarDataSets.clear();
        iBarDataSets.add(barDataSet);
        barChart.clear();
        barChart.setData(barData);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.animateY(2000, Easing.EaseInOutBounce);
        barChart.invalidate();
    }
    //    public void getData(String tabel, String kolom, String waktu1, String waktu2){
    //        BaseApiService service = RetrofitClient.getClient1().create(BaseApiService.class);
    //        Call<PerwaktuResponse> call = service.getRatebyDate(tabel, kolom, waktu1,waktu2);
    //        ArrayList<BarEntry> DataVals = new ArrayList<BarEntry>();
    //        call.enqueue(new Callback<PerwaktuResponse>() {
    //            @Override
    //            public void onResponse(Call<PerwaktuResponse> call, Response<PerwaktuResponse> response) {
    //                listRate = new ArrayList<>();
    //                listWaktu = new ArrayList<>();
    //
    //                if (response.body().isSuccess()){
    //                    if (response.body().getData() != null) {
    //                        dataList = (List<RateItem>) response.body().getData().getRate();

//                        for (int i = 0; i < dataList.size(); i++){
    //                            RateItem x = dataList.get(i);
    //                            Float air = Float.parseFloat(x.getRate());
    //                            if (kolom.equals("rateP")){
    //                                barDataSet.setLabel("Volume Gedung Pusat");
    //                            }
    //                            else if (kolom.equals("rateA")){
    //                                barDataSet.setLabel("Volume Gedung A");
    //                            }
    //                            else if (kolom.equals("rateB")){
    //                                barDataSet.setLabel("Volume Gedung B");
    //                            }
    //                            else if (kolom.equals("rateC")){
    //                                barDataSet.setLabel("Volume Gedung C");
    //                            }
    //                            else if (kolom.equals("rateD")){
    //                                barDataSet.setLabel("Volume Gedung D");
    //                            }
    //
    //                            Date newDate = null;
    //                            try {
    //                                newDate = DateFormat.parse(String.valueOf(dataList.get(i).getWaktu()));
    //                            } catch (ParseException e) {
    //                                e.printStackTrace();
    //                            }
    //
    //                            DataVals.add(new BarEntry(newDate.getTime(), air));
    //                        }
    //                    }
    //                    else {
    //
    //                    }
    //                }
    //                ShowChart(DataVals, nama);
    //            }
    //
    //            @Override
    //            public void onFailure(Call<PerwaktuResponse> call, Throwable t) {
    //
    //            }
    //        });
    //
    //    }
    //    private void ShowChart(ArrayList<BarEntry> DataVals, String nama){
    //        MyMarkerView mv = new MyMarkerView(getApplicationContext(), R.layout.my_marker_view);
    //        barChart.setMarkerView(mv);
    //
    //        YAxis leftaxisy = barChart.getAxisLeft();
    //        leftaxisy.removeAllLimitLines();
    //
    ////        leftaxisy.setAxisMaximum(100f);
    ////        leftaxisy.setAxisMinimum(0f);
    //
    //        leftaxisy.enableGridDashedLine(10f,10f,0f);
    //        leftaxisy.setDrawZeroLine(true);
    //        leftaxisy.setDrawLimitLinesBehindData(true);
    //        leftaxisy.setLabelCount(7,false);
    //        leftaxisy.setDrawGridLines(true);
    //
    //        XAxis xAxis = barChart.getXAxis();
    //        xAxis.setGranularity(1f);
    //        xAxis.enableGridDashedLine(10f, 10f, 0f);
    //        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    //        xAxis.setDrawGridLines(true);
    //        xAxis.setGranularityEnabled(true);
    //        xAxis.setLabelCount(7,true);
    //        xAxis.setValueFormatter(new ValueFormatter() {
    //            @Override
    //            public String getFormattedValue(float value) {
    //                Date date = new Date((long)value);
    //                return DateFormat.format(date);
    //            }
    //        });
    //        int[] color = new int[]{3, 169, 244};
    //
    //        barDataSet.setValues(DataVals);
    //        barDataSet.setDrawIcons(false);
    ////        barDataSet.setCircleColor(Color.rgb(3,169,244));
    ////        barDataSet.setLineWidth(2f);
    ////        barDataSet.setCircleRadius(4f);
    ////        barDataSet.setDrawCircleHole(false);
    //        barDataSet.setValueTextSize(0f);
    ////        barDataSet.setDrawFilled(false);
    //        barDataSet.setFormLineWidth(1f);
    ////        barDataSet.setMode(LineDataSet.Mode.LINEAR);
    //        barDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
    //        barDataSet.setFormSize(15.f);
    ////        barDataSet.setFillColor(Color.rgb(3,169,244));
    //        barDataSet.setColor(Color.rgb(3,169,244));
    //
    //        iBarDataSets.clear();
    //        iBarDataSets.add(barDataSet);

//        barData = new BarData(iBarDataSets);
    //
    //        barChart.clear();
    //        barChart.setData(barData);
    //        barChart.setTouchEnabled(true);
    //        barChart.setDragEnabled(true);
    //        barChart.setScaleEnabled(true);
    //        barChart.setPinchZoom(false);
    //        barChart.setDrawGridBackground(false);
    //        barChart.getDescription().setEnabled(false);
    //        barChart.getAxisRight().setEnabled(false);
    //        barChart.animateX(2000, Easing.EaseInOutBounce);
    //        barChart.invalidate();
    //    }

}