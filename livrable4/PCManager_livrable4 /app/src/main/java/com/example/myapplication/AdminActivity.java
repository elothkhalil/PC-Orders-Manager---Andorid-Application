package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AdminActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button manageUsersButton, logoutButton, resetDatabaseButton, resetStockButton, clearDatabaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI elements
        manageUsersButton = findViewById(R.id.manageUsersButton);
        logoutButton = findViewById(R.id.logoutButton);
        resetDatabaseButton = findViewById(R.id.resetDatabaseButton);
        resetStockButton = findViewById(R.id.resetStockButton);
        clearDatabaseButton = findViewById(R.id.clearDatabaseButton);

        // Handle manage users button click
        manageUsersButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ManageRequestersActivity.class);
            startActivity(intent);
        });

        // Handle logout button click
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, MainActivity.class));
            finish();
        });

        // Handle clear database button click
        clearDatabaseButton.setOnClickListener(v -> clearDatabase());

        // Handle reset database button click
        resetDatabaseButton.setOnClickListener(v -> resetDatabase());

        // Handle reset stock button click
        resetStockButton.setOnClickListener(v -> resetStock());
    }

    private void clearDatabase() {
        mDatabase.child("Requesters").removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Requesters cleared", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to clear Requesters", Toast.LENGTH_SHORT).show();
                    }
                });
        mDatabase.child("Orders").removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Orders cleared", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to clear Orders", Toast.LENGTH_SHORT).show();
                    }
                });
        mDatabase.child("Components").removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Components cleared", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to clear Components", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetDatabase() {
        String requestersJson = readJSONFromAssets("requesters.json");
        String componentsJson = readJSONFromAssets("components.json");
        try {
            // Reset Requesters
            JSONArray requestersArray = new JSONArray(requestersJson);
            for (int i = 0; i < requestersArray.length(); i++) {
                JSONObject requester = requestersArray.getJSONObject(i);
                String id = requester.getString("id");
                String firstName = requester.getString("firstName");
                String lastName = requester.getString("lastName");
                String email = requester.getString("email");

                Requester newRequester = new Requester(id, firstName, lastName, email);
                mDatabase.child("Requesters").child(id).setValue(newRequester);
            }

            // Reset Components
            JSONArray componentsArray = new JSONArray(componentsJson);
            for (int i = 0; i < componentsArray.length(); i++) {
                JSONObject component = componentsArray.getJSONObject(i);
                String id = component.getString("id");
                String name = component.getString("name");
                int quantity = component.getInt("quantity");

                Component newComponent = new Component(id, name, quantity);
                mDatabase.child("Components").child(id).setValue(newComponent);
            }

            Toast.makeText(this, "Database reset successfully", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to reset database", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetStock() {
        mDatabase.child("Orders").removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Orders cleared for stock reset", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to clear Orders", Toast.LENGTH_SHORT).show();
                    }
                });
        mDatabase.child("Components").removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loadComponentsFromFile();
                        Toast.makeText(this, "Stock reset initiated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to clear Components", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadComponentsFromFile() {
        String componentsJson = readJSONFromAssets("components.json");
        try {
            JSONArray componentsArray = new JSONArray(componentsJson);
            for (int i = 0; i < componentsArray.length(); i++) {
                JSONObject component = componentsArray.getJSONObject(i);
                String id = component.getString("id");
                String name = component.getString("name");
                int quantity = component.getInt("quantity");

                Component newComponent = new Component(id, name, quantity);
                mDatabase.child("Components").child(id).setValue(newComponent);
            }

            Toast.makeText(this, "Components loaded from file", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load components from file", Toast.LENGTH_SHORT).show();
        }
    }

    private String readJSONFromAssets(String fileName) {
        String json = null;
        try {
            InputStream inputStream = getAssets().open(fileName);  // Read the file from the assets folder
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
