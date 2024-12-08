package com.example.projectfix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.example.projectfix.databinding.FragmentRiwayatBinding;

public class RiwayatFragment extends Fragment {
    private AdapterRiwayat adapter;
    private ArrayList<ItemRiwayat> items = new ArrayList<>();
    private FragmentRiwayatBinding binding;
    public static final String DBURL = "https://indoconcert-c256b-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private ImageView btnKembali;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRiwayatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        this.db = FirebaseDatabase.getInstance(DBURL);
        this.dbRef = db.getReference("konser");

        // Setup RecyclerView
        binding.rvRiwayat.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdapterRiwayat(items, getActivity());
        binding.rvRiwayat.setAdapter(adapter);

        // Load data from database
        loadDataFromDatabase();

        btnKembali = binding.btnKembali; // Mengambil referensi tombol kembali
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke fragment sebelumnya
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void loadDataFromDatabase() {

        // Get data from database
        this.dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemRiwayat item = snapshot.getValue(ItemRiwayat.class);
                    if (item != null) { // Pastikan item tidak null
                        items.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}