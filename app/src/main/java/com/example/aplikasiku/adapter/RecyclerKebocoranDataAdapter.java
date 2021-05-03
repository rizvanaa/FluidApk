package com.example.aplikasiku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasiku.R;
import com.example.aplikasiku.model.DataKebocoran;

import java.util.List;

public class RecyclerKebocoranDataAdapter extends RecyclerView.Adapter<RecyclerKebocoranDataAdapter.ViewHolder> {
    private List<DataKebocoran> listdata;
    private Context mcontext;

    public RecyclerKebocoranDataAdapter(List<DataKebocoran> listdata, Context context) {
        this.listdata = listdata;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public RecyclerKebocoranDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_bocor, parent, false);
        return new RecyclerKebocoranDataAdapter.ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerKebocoranDataAdapter.ViewHolder holder, final int position) {
        final String dataBocor = listdata.get(position).getStatus();
        final String Waktu = listdata.get(position).getWaktu();

        holder.data.setText(dataBocor);
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
            data = itemView.findViewById(R.id.txtdatabocor);
        }
    }
}
