package com.example.recicapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddMaterialActivity extends AppCompatActivity {
    private MaterialManager materialManager;
    private NotificationManager notificationManager;
    private EditText nameInput;
    private EditText descriptionInput;
    private EditText locationInput;
    private Button submitButton;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);

        // Get userId from intent
        userId = getIntent().getLongExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize managers
        materialManager = new MaterialManager(this);
        notificationManager = new NotificationManager(this);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Agregar Material");

        // Initialize UI elements
        nameInput = findViewById(R.id.materialNameInput);
        descriptionInput = findViewById(R.id.materialDescriptionInput);
        locationInput = findViewById(R.id.materialLocationInput);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMaterial();
            }
        });
    }

    private void submitMaterial() {
        String name = nameInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String location = locationInput.getText().toString().trim();

        // Validate input
        if (name.isEmpty() || description.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add material to database
        long materialId = materialManager.addMaterial(name, description, userId, location);

        if (materialId != -1) {
            // Send notification about new material
            notificationManager.sendNewMaterialNotification(name, location);
            
            Toast.makeText(this, "Material agregado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al agregar material", Toast.LENGTH_SHORT).show();
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
}
