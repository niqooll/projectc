package com.example.projectfix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PopFilterActivity extends AppCompatActivity {

    private RecyclerView rvFilter; // RecyclerView untuk menampilkan daftar lokasi
    private Button tblKembaliFilter, tblSubmit; // Tombol Kembali dan Submit
    private FilterAdapter filterAdapter; // Adapter untuk RecyclerView
    private List<String> lokasiList = new ArrayList<>(); // Daftar lokasi
    private List<String> selectedLokasi = new ArrayList<>(); // Lokasi yang dipilih oleh user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popfilter);

        rvFilter = findViewById(R.id.rvFilter);
        tblKembaliFilter = findViewById(R.id.tblKembaliFilter);
        tblSubmit = findViewById(R.id.tblSubmit);

        // Load daftar lokasi
        loadLokasi();

        // Periksa apakah ada lokasi yang dikirim dari MainActivity
        selectedLokasi = getIntent().getStringArrayListExtra("selectedLokasi");
        if (selectedLokasi != null && !selectedLokasi.isEmpty()) {
            filterAdapter.setSelectedLokasi(selectedLokasi); // Set lokasi yang sudah dipilih
        }

        // Tombol Kembali ke MainActivity
        tblKembaliFilter.setOnClickListener(v -> finish());

        // Tombol Submit untuk mengirim lokasi terpilih ke MainActivity
        tblSubmit.setOnClickListener(v -> {
            selectedLokasi = filterAdapter.getSelectedLokasi(); // Ambil lokasi terpilih

            // Jika tidak ada lokasi yang dipilih, kirimkan "Semua Lokasi"
            if (selectedLokasi.isEmpty()) {
                selectedLokasi.add("Semua Lokasi");
            }

            // Kirim lokasi terpilih ke MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra("selectedLokasi", (ArrayList<String>) selectedLokasi);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }

    private void loadLokasi() {
        // Dummy Data dari Server (bisa diganti dengan data dinamis dari server)
        lokasiList.add("Surabaya");
        lokasiList.add("Malang");
        lokasiList.add("Semarang");
        lokasiList.add("Nusantara");
        lokasiList.add("Kendari");
        lokasiList.add("Pekanbaru");
        lokasiList.add("Bandung");
        lokasiList.add("Medan");
        lokasiList.add("Jakarta");
        lokasiList.add("Bogor");
        lokasiList.add("Banyuwangi");
        lokasiList.add("Kediri");

        // Setup RecyclerView dengan GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3); // 3 kolom
        rvFilter.setLayoutManager(gridLayoutManager);

        // Setup adapter
        filterAdapter = new FilterAdapter(this, lokasiList, selectedLokasi); // Kirim selectedLokasi
        rvFilter.setAdapter(filterAdapter);
    }
}