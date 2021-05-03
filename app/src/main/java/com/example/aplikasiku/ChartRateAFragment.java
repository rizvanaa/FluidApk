package com.example.aplikasiku;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aplikasiku.apihelper.RetrofitClient;
import com.example.aplikasiku.apiinterface.BaseApiService;
import com.example.aplikasiku.model.DataRateRealtime;
import com.example.aplikasiku.model.RateRealtimeResponse;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aplikasiku.apiinterface.DataInterface.DateFormatChart;
import static com.example.aplikasiku.apiinterface.DataInterface.myDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartRateAFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartRateAFragment extends Fragment {
    LineChart lineChart, lineChartB;
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
    public ArrayList<String> listRate;
    public ArrayList<String> listWaktu;
    ProgressDialog progressDialog;

    public ChartRateAFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartRateAFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartRateAFragment newInstance(String param1, String param2) {
        ChartRateAFragment fragment = new ChartRateAFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lineChart.setNoDataText("Data Grafik Tidak Tersedia.");
        lineChart.setNoDataTextColor(Color.rgb(3,169,244));

        lineChartB.setNoDataText("Data Grafik Tidak Tersedia.");
        lineChartB.setNoDataTextColor(Color.rgb(3,169,244));


        Date c = Calendar.getInstance().getTime();
        mCalendar = Calendar.getInstance();
        tglIni = myDateFormat.format(c).toString();
        //getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_rate_a, container, false);

        lineChart = view.findViewById(R.id.chart);
//        lineChartB = view.findViewById(R.id.chartB);
        // Inflate the layout for this fragment
        return view;
    }


}