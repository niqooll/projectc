package com.example.projectfix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HappySepActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AcaraAdapter eventAdapter;
    private List<Acara> eventList;
    private List<Acara> filteredEventList;
    private static final String URL = "http://172.20.10.3/IndoConcertAPI/tampil_data.php"; // Ganti sesuai URL server
    private static final int FILTER_REQUEST_CODE = 1; // Kode permintaan filter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.happysep);

        recyclerView = findViewById(R.id.rvHappysep);
        eventList = new ArrayList<>();
        filteredEventList = new ArrayList<>();

        // Set RecyclerView dengan GridLayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 kolom horizontal
        recyclerView.setHasFixedSize(true);

        // Adapter
        eventAdapter = new AcaraAdapter(this, filteredEventList);
        recyclerView.setAdapter(eventAdapter);

        // Load Data
        loadData(); // Muat data tanpa filter pada awalnya

        // Tombol Filter
        findViewById(R.id.ivFilter).setOnClickListener(v -> {
            if (filteredEventList.isEmpty() || filteredEventList.size() == eventList.size()) {
                Intent intent = new Intent(HappySepActivity.this, PopFilterActivity.class);
                ArrayList<String> selectedLokasi = new ArrayList<>();
                intent.putStringArrayListExtra("selectedLokasi", selectedLokasi); // Kirim lokasi terpilih
                startActivityForResult(intent, FILTER_REQUEST_CODE);
            } else {
                // Kirim lokasi yang sudah dipilih dari filteredEventList
                ArrayList<String> selectedLokasi = new ArrayList<>();
                for (Acara event : filteredEventList) {
                    if (!selectedLokasi.contains(event.getLokasi())) {
                        selectedLokasi.add(event.getLokasi());
                    }
                }

                Intent intent = new Intent(HappySepActivity.this, PopFilterActivity.class);
                intent.putStringArrayListExtra("selectedLokasi", selectedLokasi); // Kirim lokasi terpilih
                startActivityForResult(intent, FILTER_REQUEST_CODE);
            }

        });

        findViewById(R.id.tblKembali).setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void loadData() {
        // Muat semua data tanpa filter
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        eventList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                String kategori = obj.getString("kategori");
                                if (kategori.equals("happy")) { // Filter hanya kategori "happy"
                                    String poster = obj.getString("poster");
                                    String nama = obj.getString("nama");
                                    String lokasi = obj.getString("lokasi");
                                    int harga = obj.getInt("harga");

                                    eventList.add(new Acara(kategori, poster, nama, lokasi, harga));
                                }
                            }
                            // Menampilkan semua data di awal
                            filteredEventList.addAll(eventList);
                            eventAdapter.notifyDataSetChanged();  // Update tampilan
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HappySepActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<String> selectedLokasi = data.getStringArrayListExtra("selectedLokasi");

            if (selectedLokasi != null && !selectedLokasi.isEmpty()) {
                String lokasiFilter = selectedLokasi.get(0);
                filteredEventList.clear();

                // Jika "Semua Lokasi" dipilih, tampilkan semua event
                if (selectedLokasi.contains("Semua Lokasi")) {
                    filteredEventList.addAll(eventList);
                } else {
                    // Filter event berdasarkan lokasi yang dipilih
                    for (Acara event : eventList) {
                        if (selectedLokasi.contains(event.getLokasi())) {
                            filteredEventList.add(event);
                        }
                    }
                }

                // Jika tidak ada event yang cocok, tampilkan pesan
                if (filteredEventList.isEmpty()) {
                    Toast.makeText(this, "Konser tidak tersedia di kota " + lokasiFilter, Toast.LENGTH_SHORT).show();
                }

                eventAdapter.notifyDataSetChanged();
            }
        }
    }
}