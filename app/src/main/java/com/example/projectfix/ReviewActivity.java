package com.example.projectfix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewActivity extends AppCompatActivity {

    private RatingBar rbRating;
    private TextView tvRating, tvReview;
    private Button tblKembaliRating, tblTambahRating, tblHapusRating;
    private DatabaseReference database;
    private String userId = "ganteng"; // ID pengguna contoh
    public static final String DBURL = "https://indoconcert-c256b-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);

        // Inisialisasi View
        rbRating = findViewById(R.id.rbRating);
        tvRating = findViewById(R.id.tvRating);
        tvReview = findViewById(R.id.tvReview);
        tblKembaliRating = findViewById(R.id.tblKembaliRating);
        tblTambahRating = findViewById(R.id.tblTambahRating);
        tblHapusRating = findViewById(R.id.tblHapusRating);

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance(DBURL).getReference();

        // Load data review dari Firebase
        loadReviewData();

        // Navigasi kembali ke aktivitas sebelumnya
        tblKembaliRating.setOnClickListener(view -> finish());

        // Menambah atau mengedit review
        tblTambahRating.setOnClickListener(view -> openReviewPopup());

        // Menghapus review
        tblHapusRating.setOnClickListener(view -> deleteReview());

        // Listener untuk menyimpan rating seketika
        rbRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) { // Pastikan rating dipilih oleh user
                saveRating(rating);
            }
        });
    }

    // Load data rating dan review
    private void loadReviewData() {
        DatabaseReference ratingRef = database.child("reviews").child(userId);
        ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Ambil nilai rating (jika ada)
                    if (dataSnapshot.child("rating").exists()) {
                        float rating = dataSnapshot.child("rating").getValue(Float.class);
                        rbRating.setRating(rating);
                        tvRating.setText("Rating: " + rating);
                    } else {
                        tvRating.setText("Rating: Belum ada");
                        rbRating.setRating(0);
                    }

                    // Ambil nilai review dan atur visibilitas tombol hapus
                    if (dataSnapshot.child("review").exists()) {
                        String review = dataSnapshot.child("review").getValue(String.class);
                        tvReview.setText(review);
                        tblHapusRating.setVisibility(View.VISIBLE);
                    } else {
                        tvReview.setText("Anda belum melakukan review");
                        tblHapusRating.setVisibility(View.GONE);
                    }

                    tblTambahRating.setText("Edit");
                } else {
                    // Default jika tidak ada data
                    tvReview.setText("Anda belum melakukan review");
                    tvRating.setText("Rating: Belum ada");
                    rbRating.setRating(0);

                    tblTambahRating.setText("Tambah");
                    tblHapusRating.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReviewActivity.this, "Gagal memuat data review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Menyimpan rating seketika
    private void saveRating(float rating) {
        DatabaseReference ratingRef = database.child("reviews").child(userId);
        ratingRef.child("rating").setValue(rating)
                .addOnSuccessListener(aVoid -> {
                    tvRating.setText("Rating: " + rating);
                    Toast.makeText(ReviewActivity.this, "Rating berhasil disimpan", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(ReviewActivity.this, "Gagal menyimpan rating", Toast.LENGTH_SHORT).show());
    }

    // Menampilkan pop-up untuk menambahkan atau mengedit review
    private void openReviewPopup() {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popreview, null);

        EditText etReview = popupView.findViewById(R.id.etReview);
        Button tblKirimReview = popupView.findViewById(R.id.tblKirimReview);

        DatabaseReference ratingRef = database.child("reviews").child(userId);
        ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.child("review").exists()) {
                    String existingReview = dataSnapshot.child("review").getValue(String.class);
                    etReview.setText(existingReview);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReviewActivity.this, "Gagal memuat review", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .setCancelable(true)
                .create();

        tblKirimReview.setOnClickListener(v -> {
            String reviewText = etReview.getText().toString();
            ratingRef.child("review").setValue(reviewText)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ReviewActivity.this, "Review berhasil disimpan", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadReviewData();
                    })
                    .addOnFailureListener(e -> Toast.makeText(ReviewActivity.this, "Gagal menyimpan review", Toast.LENGTH_SHORT).show());
        });

        dialog.show();
    }

    // Menghapus review dari Firebase
    private void deleteReview() {
        DatabaseReference ratingRef = database.child("reviews").child(userId).child("review");
        ratingRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ReviewActivity.this, "Review berhasil dihapus", Toast.LENGTH_SHORT).show();
                    loadReviewData();
                })
                .addOnFailureListener(e -> Toast.makeText(ReviewActivity.this, "Gagal menghapus review", Toast.LENGTH_SHORT).show());
    }
}
