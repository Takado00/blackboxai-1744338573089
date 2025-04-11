package com.example.recicapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclingTipsActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private Spinner materialTypeSpinner;
    private RecyclingTipsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycling_tips);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tips de Reciclaje");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.tipsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up material type spinner
        materialTypeSpinner = findViewById(R.id.materialTypeSpinner);
        String[] materialTypes = {"Todos", "Plástico", "Papel", "Vidrio", "Metal", "Otro"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            materialTypes
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialTypeSpinner.setAdapter(spinnerAdapter);

        // Handle spinner selection
        materialTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = position == 0 ? null : materialTypes[position];
                loadTips(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadTips(null);
            }
        });

        // Initial load of all tips
        loadTips(null);
    }

    private void loadTips(String materialType) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        if (materialType == null) {
            cursor = db.query("recycling_tips", null, null, null, null, null, "dateAdded DESC");
        } else {
            cursor = db.query("recycling_tips", null, "materialType = ?",
                    new String[]{materialType}, null, null, "dateAdded DESC");
        }

        if (adapter == null) {
            adapter = new RecyclingTipsAdapter(this, cursor);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.swapCursor(cursor);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close();
        }
        dbHelper.close();
    }
}
