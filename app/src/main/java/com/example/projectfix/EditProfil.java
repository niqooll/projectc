package com.example.projectfix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfil extends AppCompatActivity {

    private EditText etNama, etEmail;
    private Button btnSimpan;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        // Inisialisasi view
        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBack = findViewById(R.id.btnBack);

        // Ambil data dari Intent
        Intent intent = getIntent();
        String currentNama = intent.getStringExtra("nama");
        String currentEmail = intent.getStringExtra("email");

        // Set data ke EditText
        if (currentNama != null) {
            etNama.setText(currentNama);
        }
        if (currentEmail != null) {
            etEmail.setText(currentEmail);
        }

        // Set listener untuk tombol Simpan
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNama = etNama.getText().toString().trim();
                String newEmail = etEmail.getText().toString().trim();

                // Validasi input
                if (newNama.isEmpty() || newEmail.isEmpty()) {
                    Toast.makeText(EditProfil.this, "Nama dan email harus diisi!", Toast.LENGTH_SHORT).show();
                } else if (!validateEmail(newEmail)) {
                    Toast.makeText(EditProfil.this, "Format email tidak valid!", Toast.LENGTH_SHORT).show();
                } else {
                    // Simpan data ke SharedPreferences
                    getSharedPreferences("ProfileData", MODE_PRIVATE)
                            .edit()
                            .putString("username", newNama)
                            .putString("email", newEmail)
                            .apply();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("nama", newNama);
                    resultIntent.putExtra("email", newEmail);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Kembali ke ProfilFragment
                }
            }

            private boolean validateEmail(String newEmail) {
                String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
                return newEmail.matches(emailPattern);
            }
        });

        // Set listener untuk tombol Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Kembali ke ProfilFragment tanpa mengirim data
            }
        });
    }
}