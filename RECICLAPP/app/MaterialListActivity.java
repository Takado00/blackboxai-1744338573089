package com.example.recicapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.Toast;

public class MaterialListActivity extends AppCompatActivity {
    private MaterialManager materialManager;
    private RecyclerView recyclerView;
    private MaterialAdapter adapter;
    private FloatingActionButton addMaterialFab;
    private long userId;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_list);

        // Get user information from intent
        userId = getIntent().getLongExtra("userId", -1);
        userType = getIntent().getStringExtra("userType");

        // Initialize MaterialManager
        materialManager = new MaterialManager(this);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.materialsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Floating Action Button
        addMaterialFab = findViewById(R.id.addMaterialFab);
        addMaterialFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start activity to add new material
                Intent intent = new Intent(MaterialListActivity.this, AddMaterialActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        // Show/hide FAB based on user type
        if (userType.equals("Empresa") || userType.equals("Reciclador")) {
            addMaterialFab.setVisibility(View.VISIBLE);
        } else {
            addMaterialFab.setVisibility(View.GONE);
        }

        // Load materials
        loadMaterials();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.material_list_menu, menu);
        
        // Configure search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMaterials(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadMaterials();
                }
                return true;
            }
        });

        return true;
    }

    private void loadMaterials() {
        Cursor cursor = materialManager.getAllMaterials();
        updateAdapter(cursor);
    }

    private void searchMaterials(String query) {
        Cursor cursor = materialManager.searchMaterials(query);
        updateAdapter(cursor);
    }

    private void updateAdapter(Cursor cursor) {
        if (adapter == null) {
            adapter = new MaterialAdapter(this, cursor, new MaterialAdapter.OnItemClickListener() {
                @Override
                public void onContactClick(long materialId, String materialName) {
                    // Handle contact button click
                    startTransactionProcess(materialId);
                }

                @Override
                public void onDetailsClick(long materialId) {
                    // Handle details button click
                    showMaterialDetails(materialId);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.swapCursor(cursor);
        }
    }

    private void startTransactionProcess(long materialId) {
        // Start transaction activity
        Intent intent = new Intent(this, TransactionActivity.class);
        intent.putExtra("materialId", materialId);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void showMaterialDetails(long materialId) {
        // Start material details activity
        Intent intent = new Intent(this, MaterialDetailsActivity.class);
        intent.putExtra("materialId", materialId);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMaterials(); // Refresh materials list
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close();
        }
    }
}
