package com.example.projectfix;

import static com.example.projectfix.PemesananFragment.DBURL;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TicketFragment extends Fragment {

    private RecyclerView recyclerView;
    private TicketAdapter adapter;
    private List<TicketOrder> orderList = new ArrayList<>();
    private DatabaseReference dbRef;
    private ImageView btBack;
    private LinearLayout noTicketsLayout;  // Layout untuk "Tidak ada tiket"
    private Button exploreNowButton;  // Tombol untuk menjelajahi sekarang

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);

        recyclerView = view.findViewById(R.id.rvOrderList);
        noTicketsLayout = view.findViewById(R.id.noTicketsLayout); // Layout untuk "Tidak ada tiket"
        exploreNowButton = view.findViewById(R.id.exploreNowButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbRef = FirebaseDatabase.getInstance(DBURL).getReference("orders");

        adapter = new TicketAdapter(getContext(), orderList, dbRef);
        recyclerView.setAdapter(adapter);
        fetchOrders();

        exploreNowButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).displayFragment(new CariEvent(), "CariEvent");

            }
        });

        return view;
    }

    private void fetchOrders() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                new Thread(() -> {
                    orderList.clear();
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        TicketOrder order = orderSnapshot.getValue(TicketOrder.class);
                        if (order != null) {
                            orderList.add(order);
                        }
                    }

                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            if (orderList.isEmpty()) {
                                noTicketsLayout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                noTicketsLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OrderList", "Error fetching orders: " + error.getMessage());
            }
        });
    }
}

