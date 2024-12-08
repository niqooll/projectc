package com.example.projectfix;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.LokasiViewHolder> {

    private Context context;
    private List<String> lokasiList; // Daftar lokasi yang ditampilkan
    private List<String> selectedLokasi; // Daftar lokasi yang dipilih

    public FilterAdapter(Context context, List<String> lokasiList, List<String> selectedLokasi) {
        this.context = context;
        this.lokasiList = lokasiList;
        this.selectedLokasi = selectedLokasi; // Terima selectedLokasi dari PopFilterActivity
    }

    @Override
    public LokasiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lokasi, parent, false);
        return new LokasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LokasiViewHolder holder, int position) {
        String lokasi = lokasiList.get(position);
        holder.lokasiText.setText(lokasi);

        // Periksa apakah lokasi dipilih dan sesuaikan tampilan
        if (selectedLokasi.contains(lokasi)) {
            holder.lokasiText.setBackgroundResource(R.drawable.ovalbg_selected); // Warna biru
            holder.lokasiText.setTextColor(Color.WHITE);
        } else {
            holder.lokasiText.setBackgroundResource(R.drawable.ovalbg); // Warna semula
            holder.lokasiText.setTextColor(Color.BLACK);
        }

        // Set listener untuk toggle pemilihan
        holder.lokasiText.setOnClickListener(v -> {
            if (selectedLokasi.contains(lokasi)) {
                selectedLokasi.remove(lokasi); // Hapus jika sudah dipilih
                holder.lokasiText.setBackgroundResource(R.drawable.ovalbg);
                holder.lokasiText.setTextColor(Color.BLACK);
            } else {
                selectedLokasi.add(lokasi); // Tambahkan jika belum dipilih
                holder.lokasiText.setBackgroundResource(R.drawable.ovalbg_selected);
                holder.lokasiText.setTextColor(Color.WHITE);
            }
            notifyDataSetChanged();
        });
    }



    @Override
    public int getItemCount() {
        return lokasiList.size();
    }

    // Method untuk mendapatkan daftar lokasi yang dipilih
    public List<String> getSelectedLokasi() {
        return selectedLokasi;
    }

    class LokasiViewHolder extends RecyclerView.ViewHolder {

        TextView lokasiText;

        public LokasiViewHolder(View itemView) {
            super(itemView);
            lokasiText = itemView.findViewById(R.id.tvLokasi);
        }
    }

    public void setSelectedLokasi(List<String> selectedLokasi) {
        this.selectedLokasi.clear();
        this.selectedLokasi.addAll(selectedLokasi);
        notifyDataSetChanged();
    }
}

