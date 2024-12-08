package com.example.projectfix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Homepage extends Fragment {

    RecyclerView rvHappy, rvMurah;
    Button tblSelHappy, tblSelMurah;

    AcaraAdapter happyAdapter, murahAdapter;
    List<Acara> eventList = new ArrayList<>();
    List<Acara> filteredHappyList = new ArrayList<>();
    List<Acara> filteredMurahList = new ArrayList<>();
    String URL = "http://172.20.10.3/IndoConcertAPI/tampil_data.php"; // Ganti sesuai URL server
    private TextView searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage, container, false);

        // Inisialisasi view
        rvHappy = view.findViewById(R.id.rvHappy);
        rvMurah = view.findViewById(R.id.rvMurah);
        tblSelHappy = view.findViewById(R.id.tblSelHappy);
        tblSelMurah = view.findViewById(R.id.tblSelMurah);

        // RecyclerView untuk kategori Happy
        rvHappy.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        happyAdapter = new AcaraAdapter(requireContext(), filteredHappyList);
        rvHappy.setAdapter(happyAdapter);

        // RecyclerView untuk kategori Murah
        rvMurah.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        murahAdapter = new AcaraAdapter(requireContext(), filteredMurahList);
        rvMurah.setAdapter(murahAdapter);

        // Inisialisasi tombol "Search"
        searchButton = view.findViewById(R.id.btCariEvent);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).displayFragment(new CariEvent(), "CariEvent");
                }
            }
        });


        // Muat data tanpa filter awalnya
        loadData();

        // Tombol Selengkapnya untuk kategori Happy
        tblSelHappy.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), HappySepActivity.class);
            startActivity(intent);
        });

        // Tombol Selengkapnya untuk kategori Murah
        tblSelMurah.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MurmerActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadData() {
        // Membuat custom Volley timeout
        int socketTimeout = 30000; // 30 detik
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        // Muat semua data tanpa filter
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        eventList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                Log.d("Response", obj.toString());

                                // Sesuaikan dengan nama field di PHP
                                String kategori = obj.getString("kategori");
                                String poster = obj.getString("poster"); // Menggunakan "poster" bukan "image_url"
                                String nama = obj.getString("nama"); // Menggunakan "nama" bukan "title"
                                String lokasi = obj.getString("lokasi"); // Menggunakan "lokasi" bukan "location"
                                int harga = obj.getInt("harga"); // Menggunakan "harga" bukan "price"

                                // Tambahkan ke eventList tanpa filter
                                eventList.add(new Acara(kategori, poster, nama, lokasi, harga));
                            }

                            // Menyaring kategori
                            for (Acara event : eventList) {
                                if (event.getKategori().equalsIgnoreCase("happy") && filteredHappyList.size() < 2) {
                                    filteredHappyList.add(event);
                                } else if (event.getKategori().equalsIgnoreCase("murah") && filteredMurahList.size() < 2) {
                                    filteredMurahList.add(event);
                                }
                            }

                            // Update adapter dengan data yang disaring
                            happyAdapter.notifyDataSetChanged();
                            murahAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        Toast.makeText(requireContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        request.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(request);


        
    }
}
