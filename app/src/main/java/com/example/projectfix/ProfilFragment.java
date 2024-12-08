package com.example.projectfix;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectfix.databinding.FragmentProfilBinding;

public class ProfilFragment extends Fragment {

    private FragmentProfilBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfilBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Load saved data
        loadSavedData();

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfil.class);
                intent.putExtra("nama", binding.tvNama.getText().toString());
                intent.putExtra("email", binding.tvEmail .getText().toString());
                startActivityForResult(intent, 1);
            }
        });

        binding.btnRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke RiwayatFragment
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container_atas, RiwayatFragment.class, null);
                ft.addToBackStack(null);  // Menambahkan fragment ke back stack agar bisa kembali ke CariEvent
                ft.commit();
            }
        });

        binding.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke ReviewActivity
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadSavedData() {
        String savedUsername = getActivity().getSharedPreferences("ProfileData", getActivity().MODE_PRIVATE).getString("username", "Rasyad Ekardi");
        String savedEmail = getActivity().getSharedPreferences("ProfileData", getActivity().MODE_PRIVATE).getString("email", "rasyadpe@student.ub.ac.id");
        binding.tvNama.setText(savedUsername);
        binding.tvEmail.setText(savedEmail);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            String newNama = data.getStringExtra("nama");
            String newEmail = data.getStringExtra("email");
            if (newNama != null) binding.tvNama.setText(newNama);
            if (newEmail != null) binding.tvEmail.setText(newEmail);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear binding reference
    }
}