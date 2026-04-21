package com.example.financecompanionmobileapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TechSupportActivity extends AppCompatActivity {
    private EditText etName, etEmail, etIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_support);

        findViewById(R.id.btnBackTech).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnHomeNavSupport).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        etName = findViewById(R.id.etSupportName);
        etEmail = findViewById(R.id.etSupportEmail);
        etIssue = findViewById(R.id.etSupportIssue);
        Button btnSend = findViewById(R.id.btnSendSupport);

        btnSend.setOnClickListener(v -> sendEmail());
    }

    private void sendEmail() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String issue = etIssue.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || issue.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String subject = "Tech Support Request from " + name;
        String message = "Name: " + name + "\nEmail: " + email + "\n\nIssue:\n" + issue;
        String recipient = "anirudhp2121@gmail.com";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
