package com.example.projectfix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterRiwayat extends RecyclerView.Adapter<AdapterRiwayat.Viewholder> {

    Context context;
    ArrayList<ItemRiwayat> items;

    public AdapterRiwayat(ArrayList<ItemRiwayat> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView tvNama, tvTanggal, tvJam, tvHarga;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvHarga = itemView.findViewById(R.id.tvHarga);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, tvNama.getText().toString(), Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_riwayat, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ItemRiwayat item = items.get(position);
        holder.tvNama.setText(item.getNamaKonser());
        holder.tvTanggal.setText(item.getTanggal());
        holder.tvJam.setText(item.getJam());
        holder.tvHarga.setText(item.getHarga());

        // Menggunakan Glide untuk memuat gambar
        Glide.with(context)
                .load(item.getImage()) // Pastikan item.getImage() mengembalikan URL atau path gambar
                .placeholder(R.drawable.loading) // Gambar placeholder saat memuat
                .error(R.drawable.error) // Gambar yang ditampilkan jika terjadi kesalahan
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}