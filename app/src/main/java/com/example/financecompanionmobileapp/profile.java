package com.example.financecompanionmobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class profile extends AppCompatActivity {
    private ImageView imgProfile;
    private EditText etName, etEmail, etPhone;
    private SharedPreferences sp;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgProfile = findViewById(R.id.imgProfile);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        sp = getSharedPreferences("UserProfile", MODE_PRIVATE);
        loadProfile();

        findViewById(R.id.btnChangePhoto).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });

        findViewById(R.id.btnSaveProfile).setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", etName.getText().toString());
        editor.putString("email", etEmail.getText().toString());
        editor.putString("phone", etPhone.getText().toString());
        if (imageUri != null) {
            editor.putString("photoUri", imageUri.toString());
        }
        editor.apply();
        Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
        finish(); // Go back to main screen
    }

    private void loadProfile() {
        etName.setText(sp.getString("name", ""));
        etEmail.setText(sp.getString("email", ""));
        etPhone.setText(sp.getString("phone", ""));
        String uriStr = sp.getString("photoUri", null);
        if (uriStr != null) {
            imgProfile.setImageURI(Uri.parse(uriStr));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imgProfile.setImageURI(imageUri);
        }
    }
}
