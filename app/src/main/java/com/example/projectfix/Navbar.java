package com.example.projectfix;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class Navbar extends Fragment implements View.OnClickListener {

    private ImageView btnHome, btnEvent, btnTiket, btnProfil;
    private ImageView selectedButton;
    private OnNavbarItemSelectedListener listener;

    // Deklarasi interface untuk komunikasi dengan Activity
    public interface OnNavbarItemSelectedListener {
        void onNavbarItemSelected(int itemId);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnNavbarItemSelectedListener) {
            listener = (OnNavbarItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " harus mengimplementasikan OnNavbarItemSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navbar, container, false);

        // Inisialisasi tombol navigasi
        btnHome = view.findViewById(R.id.home);
        btnTiket = view.findViewById(R.id.tiket);
        btnProfil = view.findViewById(R.id.profil);

        // Set listener untuk setiap tombol
        btnHome.setOnClickListener(this);
        btnTiket.setOnClickListener(this);
        btnProfil.setOnClickListener(this);

        // Tombol awal yang dipilih (Home)
        setButtonSelected(btnHome);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onNavbarItemSelected(view.getId());
        }
        setButtonSelected((ImageView) view);
    }

    private void setButtonSelected(ImageView button) {
        // Reset warna tombol sebelumnya
        if (selectedButton != null) {
            selectedButton.clearColorFilter();
        }

        // Atur warna tombol yang dipilih
        int activeColor = ContextCompat.getColor(requireContext(), R.color.active_color);
        button.setColorFilter(activeColor, PorterDuff.Mode.SRC_IN);

        selectedButton = button; // Simpan tombol yang dipilih
    }

    public void setSelectedItem(int itemId) {
        // Tentukan tombol berdasarkan itemId
        ImageView buttonToSelect = null;

        if (itemId == R.id.home) {
            buttonToSelect = btnHome;
        } else if (itemId == R.id.tiket) {
            buttonToSelect = btnTiket;
        } else if (itemId == R.id.profil) {
            buttonToSelect = btnProfil;
        }

        if (buttonToSelect != null) {
            setButtonSelected(buttonToSelect);
        }
    }
}
