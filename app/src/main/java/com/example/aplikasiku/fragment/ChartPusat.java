package com.example.aplikasiku.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.aplikasiku.MyMarkerView;
import com.example.aplikasiku.R;
import com.example.aplikasiku.apihelper.RetrofitClient;
import com.example.aplikasiku.apiinterface.BaseApiService;
import com.example.aplikasiku.model.ChartRateRes;
import com.example.aplikasiku.model.DataRateRealtime;
import com.example.aplikasiku.model.raterealtime.DataItem;
import com.example.aplikasiku.model.raterealtime.RateRealtimeResponse;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aplikasiku.apiinterface.DataInterface.DateFormatChart;
import static com.example.aplikasiku.apiinterface.DataInterface.simpleDateFormat;

public class ChartPusat extends Fragment {
    LineChart lineChartA;
    List<DataRateRealtime> dataRateRealtimes = new ArrayList<>();
    LineDataSet lineDataSetA = new LineDataSet(null,null);
    LimitLine upper, lower;
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData lineData;
    ProgressDialog progressDialog;
    Calendar mCalendar;
    String tglIni;
    List<DataItem> dataList;
    LineChart lineChart;
    LineDataSet lineDataSet = new LineDataSet(null, null);
    String column, table;

    public ArrayList<String> listRate;
    public ArrayList<String> listWaktu;
    Button btnDownload;
    Handler handler = new Handler();
    Runnable refresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chart_pusat, container, false);
        lineChart = v.findViewById(R.id.chart);
        btnDownload = v.findViewById(R.id.btn_cetak);

        lineDataSet.setLabel("Rate Air Pusat");

        Date c = Calendar.getInstance().getTime();
        mCalendar = Calendar.getInstance();
        tglIni = simpleDateFormat.format(c).toString();
        column = "rateP";
        table = "rate_p";
        getData(column, table);
        refresh = new Runnable() {
            public void run() {
                // Do something
                getData(column, table);
                handler.postDelayed(refresh, 5000);
            }
        };
        handler.post(refresh);
        String url = "https://pantaukendaliair.com/chartPusat.php";
        WebView view = (WebView) v.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(url);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Document document = new Document();
                PdfPTable table = new PdfPTable(new float[] { 2, 1 });
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                table.getDefaultCell().setFixedHeight(20);
                table.addCell("Waktu");
                table.addCell("Rate");
                table.setHeaderRows(1);
                PdfPCell[] cells = table.getRow(0).getCells();
                for (int j=0;j<cells.length;j++){
                    BaseColor myColor = WebColors.getRGBColor("#87D2F3");
                    cells[j].setBackgroundColor(myColor);
                }
                for (int i=0;i<listRate.size();i++){
                    table.addCell(listWaktu.get(i));
                    table.addCell(listRate.get(i));
                    Log.i("hah", "rate "+listRate.toArray());
                }
                try {
                    File folder = new File(Environment.getExternalStorageDirectory()+ "/Fluid");
                    if (!folder.exists())
                        folder.mkdir();
                    final String pdf = folder.toString() + "/Rate Air Gedung Pusat "+tglIni+".pdf";
                    PdfWriter.getInstance(document, new FileOutputStream(pdf));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                document.open();
                try {

                    document.add(JudulText("Data Pemantauan Rate Air"));
                    document.add(JudulText("gedung Pusat"));
                    document.add(JudulText("Realtime "+tglIni));
                    document.add(table);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                document.close();
                Toast.makeText(getActivity().getApplicationContext(), "Data pemantauan Berhasil disimpan. Silahkan Lihat di Penyimpanan internal /Fluid", Toast.LENGTH_LONG).show();

            }
        });

        return v;
    }

    void getDataRate(){
        Map<String,String> map = new HashMap<>();
        map.put("column","rateA");
        map.put("table","rate_a");
        BaseApiService service = RetrofitClient.getClient1().create(BaseApiService.class);
        Call<ChartRateRes> call = service.getRateChart("rateA","rate_a");
        call.enqueue(new Callback<ChartRateRes>() {
            @Override
            public void onResponse(Call<ChartRateRes> call, Response<ChartRateRes> response) {
                ArrayList<Entry> DataValsA = new ArrayList<Entry>();
                List<ChartRateRes.Datum> datumList = new ArrayList<>();
                listRate = new ArrayList<>();
                listWaktu = new ArrayList<>();
                if(response.code() == 200){
                    for(ChartRateRes.Datum data : response.body().getData()){
                        Float rateA = Float.parseFloat(data.getRate());
//                        for (int i = 0; i < datumList.size(); i++){
//                            listRate.add(datumList.get(i).getRate());
//                            listWaktu.add(datumList.get(i).getWaktu());
//                        }
                        listRate.add(data.getRate());
                        listWaktu.add(data.getWaktu());
                        Date newDate = null;
                        try {
                            newDate = DateFormatChart.parse(String.valueOf(data.getWaktu()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DataValsA.add(new Entry(newDate.getTime(),rateA));
                        datumList.add(data);
                    }

                    upper = new LimitLine(10f, "Batas Atas");
                    lower = new LimitLine(0f, "Batas Bawah");

                    YAxis leftaxisy = lineChartA.getAxisLeft();
                    leftaxisy.removeAllLimitLines();

                    leftaxisy.enableGridDashedLine(10f,10f,0f);
                    //leftaxisy.setDrawZeroLine(true);
                    leftaxisy.setDrawLimitLinesBehindData(false);
                    leftaxisy.setLabelCount(response.body().getData().size(),true);
                    leftaxisy.setDrawGridLines(true);
                    leftaxisy.setDrawZeroLine(false);

                    XAxis xAxis = lineChartA.getXAxis();
                    xAxis.enableGridDashedLine(10f, 10f, 0f);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setGranularity(7f);
                    xAxis.setLabelRotationAngle(315f);
                    xAxis.setDrawGridLines(true);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setLabelCount(response.body().getData().size(),true);
                    xAxis.setDrawLimitLinesBehindData(true);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            Date date = new Date((long)value);
                            return DateFormatChart.format(date);
                        }
                    });

                    Log.i("asdsad",new Gson().toJson(datumList));
                    lineDataSetA.setValues(DataValsA);
                    lineDataSetA.setDrawIcons(false);
                    lineDataSetA.setCircleColor(Color.rgb(3,169,244));
                    lineDataSetA.setLineWidth(2f);
                    lineDataSetA.setCircleRadius(4f);
                    lineDataSetA.setDrawCircleHole(false);
                    lineDataSetA.setValueTextSize(0f);
                    lineDataSetA.setDrawFilled(false);
                    lineDataSetA.setFormLineWidth(1f);
                    lineDataSetA.setMode(LineDataSet.Mode.LINEAR);
                    lineDataSetA.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                    lineDataSetA.setFormSize(15.f);
                    lineDataSetA.setFillColor(Color.rgb(3,169,244));
                    lineDataSetA.setColor(Color.rgb(3,169,244));

                    lineData = new LineData(lineDataSetA);
                    lineChartA.setData(lineData);
                    lineChartA.setTouchEnabled(true);
                    lineChartA.setDragEnabled(true);
                    lineChartA.setScaleEnabled(true);
                    lineChartA.setPinchZoom(false);
                    lineChartA.setDrawGridBackground(false);
                    lineChartA.getDescription().setEnabled(false);
                    lineChartA.getAxisRight().setEnabled(false);
                    lineChartA.animateX(2000, Easing.EaseInOutBounce);
                    lineChartA.invalidate();
                }else{
                    Toast.makeText(getContext(), ""+response.code()+"//"+response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ChartRateRes> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getData(String kolom, String tabel){
        BaseApiService service = RetrofitClient.getClient1().create(BaseApiService.class);
        Call<RateRealtimeResponse> call = service.getRateRealtime(kolom, tabel);
        ArrayList<Entry> DataVals = new ArrayList<Entry>();
        call.enqueue(new Callback<RateRealtimeResponse>() {
            @Override
            public void onResponse(Call<RateRealtimeResponse> call, Response<RateRealtimeResponse> response) {
                listRate = new ArrayList<>();
                listWaktu = new ArrayList<>();

                if (response.body().isSuccess()){
                    if (response.body().getData() != null) {
                        dataList = response.body().getData();
                        for (int i = 0; i < dataList.size(); i++){
                            DataItem x = dataList.get(i);
                            Float air = Float.parseFloat(x.getRate());
                            lineDataSet.setLabel("Rate Gedung Pusat");
                            listRate.add(dataList.get(i).getRate());
                            listWaktu.add(String.format(dataList.get(i).getWaktu(), DateFormatChart));

                            Date newDate = null;
                            try {
                                newDate = DateFormatChart.parse(String.valueOf(dataList.get(i).getWaktu()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            DataVals.add(new Entry(newDate.getTime(), air));
                        }
                    }
                    else {

                    }
                }
                ShowChart(DataVals, "Rate Realtime Gedung Pusat");
            }

            @Override
            public void onFailure(Call<RateRealtimeResponse> call, Throwable t) {

            }
        });

    }
    private void ShowChart(ArrayList<Entry> DataVals, String nama){
//        MyMarkerView mv = new MyMarkerView(getActivity().getApplicationContext(), R.layout.my_marker_view);
//        lineChart.setMarkerView(mv);
        lineChart.setHorizontalScrollBarEnabled(true);
        lineChart.setScaleXEnabled(true);
        lineChart.getScrollBarSize();

        YAxis leftaxisy = lineChart.getAxisLeft();
        leftaxisy.removeAllLimitLines();

//        leftaxisy.setAxisMaximum(100f);
//        leftaxisy.setAxisMinimum(0f);

        leftaxisy.enableGridDashedLine(10f,10f,0f);
        leftaxisy.setDrawZeroLine(true);
        leftaxisy.setDrawLimitLinesBehindData(true);
        leftaxisy.setLabelCount(7,false);
        leftaxisy.setDrawGridLines(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceMin(20f);
        xAxis.setLabelRotationAngle(30);
        xAxis.setDrawGridLines(true);
        xAxis.setLabelCount(10, true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Date date = new Date((long)value);
                return DateFormatChart.format(date);
            }
        });

        lineDataSet.setValues(DataVals);
        lineDataSet.setDrawIcons(false);
        lineDataSet.setCircleColor(Color.rgb(3,169,244));
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(0.5f);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        lineDataSet.setFormSize(5.f);
        lineDataSet.setFillColor(Color.rgb(3,169,244));
        lineDataSet.setColor(Color.rgb(3,169,244));

        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData = new LineData(iLineDataSets);

        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateX(2000, Easing.EaseInOutBounce);
        lineChart.invalidate();
    }

    public void pdfdownload() {
        new SweetAlertDialog(getActivity().getApplicationContext(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Anda yakin untuk menyimpan data pemantauan Rate Air?")
                .setConfirmText("Simpan")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sweetAlertDialog) {
                        Document document = new Document();
                        PdfPTable table = new PdfPTable(new float[] { 2, 1 });
                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.getDefaultCell().setFixedHeight(20);
                        table.addCell("Waktu");
                        table.addCell("Rate");
                        table.setHeaderRows(1);
                        PdfPCell[] cells = table.getRow(0).getCells();
                        for (int j=0;j<cells.length;j++){
                            BaseColor myColor = WebColors.getRGBColor("#87D2F3");
                            cells[j].setBackgroundColor(myColor);
                        }
                        for (int i=0;i<listRate.size();i++){
                            table.addCell(listWaktu.get(i));
                            table.addCell(listRate.get(i));
                            Log.i("hah", "rate "+listRate.toArray());
                        }
                        try {
                            File folder = new File(Environment.getExternalStorageDirectory()+ "/Fluid");
                            if (!folder.exists())
                                folder.mkdir();
                            final String pdf = folder.toString() + "/Rate Air "+tglIni+".pdf";
                            PdfWriter.getInstance(document, new FileOutputStream(pdf));
                        } catch (FileNotFoundException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                        document.open();
                        try {

                            document.add(JudulText("Data Pemantauan Rate Air"));
                            document.add(JudulText("gedung A"));
                            document.add(JudulText("Realtime "+tglIni));
                            document.add(table);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                        document.close();

                        sweetAlertDialog.dismissWithAnimation();
                        Toast.makeText(getActivity().getApplicationContext(), "Data pemantauan Berhasil disimpan. Silahkan Lihat di Penyimpanan internal /Fluid", Toast.LENGTH_LONG).show();
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

}