package com.example.recicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private UserManager userManager;
    private EditText usernameInput, emailInput, passwordInput;
    private Spinner userTypeSpinner;
    private Button registerButton, loginButton;
    private View loginContainer;
    private BottomNavigationView bottomNavigation;
    private long currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize managers
        userManager = new UserManager(this);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RECICLAPP");

        // Initialize UI elements
        initializeViews();
        setupUserTypeSpinner();
        setupBottomNavigation();
        setupButtons();
    }

    private void initializeViews() {
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
        loginContainer = findViewById(R.id.loginContainer);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void setupUserTypeSpinner() {
        String[] userTypes = {"Empresa", "Reciclador", "Persona", "Administrador"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            userTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_materials:
                            startActivity(new Intent(MainActivity.this, MaterialListActivity.class)
                                .putExtra("userId", currentUserId));
                            return true;
                        case R.id.navigation_tips:
                            startActivity(new Intent(MainActivity.this, RecyclingTipsActivity.class));
                            return true;
                        case R.id.navigation_transactions:
                            // TODO: Implement TransactionActivity
                            Toast.makeText(MainActivity.this, "Próximamente", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.navigation_profile:
                            // TODO: Implement ProfileActivity
                            Toast.makeText(MainActivity.this, "Próximamente", Toast.LENGTH_SHORT).show();
                            return true;
                    }
                    return false;
                }
            });
    }

    private void setupButtons() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void registerUser() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String userType = userTypeSpinner.getSelectedItem().toString();

        if (validateInput(username, email, password)) {
            if (!userManager.userExists(email)) {
                userManager.registerUser(username, email, password, userType);
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                loginUser(); // Automatically login after registration
            } else {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Implement proper login verification
        if (userManager.userExists(email)) {
            // For demo purposes, we're not checking the password
            showMainContent();
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String username, String email, String password) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showMainContent() {
        loginContainer.setVisibility(View.GONE);
        bottomNavigation.setVisibility(View.VISIBLE);
        
        // Start with the materials list
        startActivity(new Intent(this, MaterialListActivity.class)
            .putExtra("userId", currentUserId));
    }
}
