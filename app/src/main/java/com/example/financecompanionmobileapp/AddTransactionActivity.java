package com.example.financecompanionmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddTransactionActivity extends AppCompatActivity {
    private EditText etTitle, etAmount;
    private RadioGroup rgType;
    private RadioButton rbIncome, rbExpense;
    private Database db;
    private int transactionId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        // Home button in bottom nav
        findViewById(R.id.btnHomeNavAdd).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        rgType = findViewById(R.id.rgType);
        rbIncome = findViewById(R.id.rbIncome);
        rbExpense = findViewById(R.id.rbExpense);
        Button btnSave = findViewById(R.id.btnSave);
        TextView tvHeader = findViewById(R.id.tvHeader);

        db = new Database(this);

        if (getIntent().hasExtra("id")) {
            transactionId = getIntent().getIntExtra("id", -1);
            etTitle.setText(getIntent().getStringExtra("title"));
            etAmount.setText(String.valueOf(getIntent().getDoubleExtra("amount", 0)));
            String type = getIntent().getStringExtra("type");
            if ("Income".equals(type)) rbIncome.setChecked(true);
            else rbExpense.setChecked(true);
            
            tvHeader.setText("Edit Transaction");
            btnSave.setText("Update Transaction");
        }

        btnSave.setOnClickListener(v -> saveTransaction());
    }

    private void saveTransaction() {
        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String type = rbIncome.isChecked() ? "Income" : "Expense";

        if (title.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        if (transactionId == -1) {
            db.addTransaction(title, amount, type);
            Toast.makeText(this, "Transaction Added", Toast.LENGTH_SHORT).show();
        } else {
            db.updateTransaction(transactionId, title, amount, type);
            Toast.makeText(this, "Transaction Updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}