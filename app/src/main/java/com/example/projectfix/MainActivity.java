package com.example.projectfix;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements Navbar.OnNavbarItemSelectedListener {

    private FragmentManager fm;
    private Navbar navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi FragmentManager
        fm = getSupportFragmentManager();

        // Tambahkan fragment Navbar ke container_bawah
        if (savedInstanceState == null) { // Tambahkan hanya saat pertama kali
            FragmentTransaction ft = fm.beginTransaction();
            navbar = new Navbar();
            ft.add(R.id.container_bawah, navbar, "NavbarFragment");
            ft.commit();

            // Tampilkan fragment awal di container_atas
            displayFragment(new Homepage(), "HomepageFragment");
        } else {
            // Dapatkan referensi ke Navbar jika sudah ada
            navbar = (Navbar) fm.findFragmentByTag("NavbarFragment");
        }
    }

    // Method untuk menampilkan fragment di container_atas
    public void displayFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_atas, fragment, tag);
        ft.commit();
    }


    // Method dari interface OnNavbarItemSelectedListener untuk menangani pilihan dari Navbar
    @Override
    public void onNavbarItemSelected(int itemId) {
        Fragment selectedFragment = null;
        String fragmentTag = null;

        // Tentukan fragment berdasarkan itemId
        if (itemId == R.id.home) {
            selectedFragment = new Homepage();
            fragmentTag = "HomepageFragment";
        } else if (itemId == R.id.tiket) {
            selectedFragment = new TicketFragment();
            fragmentTag = "TicketFragment";
        } else if (itemId == R.id.profil) {
            selectedFragment = new ProfilFragment();
            fragmentTag = "ProfilFragment";
        }

        // Tampilkan fragment jika valid
        if (selectedFragment != null && fragmentTag != null) {
            displayFragment(selectedFragment, fragmentTag);

            // Sinkronisasi navbar
            if (navbar != null) {
                navbar.setSelectedItem(itemId);
            }
        }
    }

    // Method untuk mengatur visibilitas Bottom Navigation
    public void setBottomNavigationVisibility(boolean isVisible) {
        View bottomNav = findViewById(R.id.container_bawah);
        if (bottomNav != null) {
            bottomNav.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    // Method untuk memilih tombol navbar secara programatik
    public void setNavbarSelected(int itemId) {
        if (navbar != null) {
            navbar.setSelectedItem(itemId);
        }
    }

    // Method untuk menangani tombol Back
    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
