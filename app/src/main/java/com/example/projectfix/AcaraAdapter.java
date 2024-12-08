package com.example.projectfix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AcaraAdapter extends RecyclerView.Adapter<AcaraAdapter.EventViewHolder> {

    private Context context;
    private List<Acara> eventList;

    public AcaraAdapter(Context context, List<Acara> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_layout, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Acara event = eventList.get(position);

        holder.tvNamaEvent.setText(event.getNama());
        holder.tvLokasiEvent.setText(event.getLokasi());
        holder.tvHargaEvent.setText(String.valueOf(event.getHarga()));

        // Load gambar dari URL menggunakan Glide
        Glide.with(context)
                .load(event.getPoster()) // URL poster dari JSON
                .placeholder(R.drawable.logo_hd) // Placeholder jika gagal
                .into(holder.ivEvent);
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView ivEvent;
        TextView tvNamaEvent, tvLokasiEvent, tvHargaEvent;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            ivEvent = itemView.findViewById(R.id.ivEvent);
            tvNamaEvent = itemView.findViewById(R.id.tvNamaEvent);
            tvLokasiEvent = itemView.findViewById(R.id.tvLokasiEvent);
            tvHargaEvent = itemView.findViewById(R.id.tvHargaEvent);
        }
    }

}
