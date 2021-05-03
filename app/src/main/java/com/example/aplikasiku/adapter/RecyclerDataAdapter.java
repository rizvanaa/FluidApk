package com.example.aplikasiku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasiku.R;
import com.example.aplikasiku.model.DataRate;
import com.example.aplikasiku.model.perwaktu.RateItem;
import com.example.aplikasiku.model.VolumePerwaktuItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.example.aplikasiku.apiinterface.DataInterface.DateDataFormat;
import static com.example.aplikasiku.apiinterface.DataInterface.DateFormat;
import static com.example.aplikasiku.apiinterface.DataInterface.formatwaktu;


public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.ViewHolder> {
    private List<RateItem> listdata;
    private Context context;

    public RecyclerDataAdapter(List<RateItem> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_rate, parent, false);
        return new RecyclerDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerDataAdapter.ViewHolder holder, final int position) {
        final String dataRate = listdata.get(position).getRate();
        final String Waktu = listdata.get(position).getWaktu();

        holder.data.setText(dataRate + " liter");
        holder.waktu.setText(Waktu);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView waktu, data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            waktu = itemView.findViewById(R.id.txtwaktu);
            data = itemView.findViewById(R.id.txtdatarate);
        }
    }
}