package com.example.financecompanionmobileapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Database db;
    private TextView tvBalance;
    private RecyclerView rvHistory;
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        db = new Database(this);
        tvBalance = findViewById(R.id.tvBalance);
        rvHistory = findViewById(R.id.rvHistoryMain);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        if (findViewById(R.id.main_layout) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        findViewById(R.id.btnGoProfile).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        findViewById(R.id.fab).setOnClickListener(v -> {
            startActivity(new Intent(this, AddTransactionActivity.class));
        });

        // Home button in bottom nav
        findViewById(R.id.btnHomeNav).setOnClickListener(v -> {
            rvHistory.smoothScrollToPosition(0);
        });
        
        // Tech Support button in bottom nav
        findViewById(R.id.btnTechSupportNav).setOnClickListener(v -> {
            startActivity(new Intent(this, TechSupportActivity.class));
        });

        SearchView searchView = findViewById(R.id.searchViewMain);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) adapter.filter(newText);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        double currentBalance = 0;
        List<Transaction> allTransactions = db.getAll();
        
        ArrayList<Entry> balanceEntries = new ArrayList<>();
        balanceEntries.add(new Entry(0, 0f));

        for (int i = 0; i < allTransactions.size(); i++) {
            Transaction t = allTransactions.get(i);
            if (t.getType().equals("Income")) {
                currentBalance += t.getAmount();
            } else {
                currentBalance -= t.getAmount();
            }
            balanceEntries.add(new Entry(i + 1, (float) currentBalance));
        }

        tvBalance.setText(String.format("₹%.2f", currentBalance));
        updateChart(balanceEntries);
        
        List<Transaction> displayList = new ArrayList<>(allTransactions);
        Collections.reverse(displayList);
        
        if (adapter == null) {
            adapter = new TransactionAdapter(displayList);
            rvHistory.setAdapter(adapter);
        } else {
            adapter.updateList(displayList);
        }
    }

    private void updateChart(ArrayList<Entry> entries) {
        LineChart chart = findViewById(R.id.flowChart);
        if (chart == null) return;

        LineDataSet dataSet = new LineDataSet(entries, "Net Balance Flow");
        dataSet.setColor(Color.parseColor("#4FC3F7"));
        dataSet.setCircleColor(Color.WHITE);
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleHoleColor(Color.parseColor("#001F3F"));
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(9f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#4FC3F7"));
        dataSet.setFillAlpha(50);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#33FFFFFF"));
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisLineColor(Color.WHITE);

        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.animateX(800);
        chart.invalidate();
    }
}