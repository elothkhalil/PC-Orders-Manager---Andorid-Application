package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class StoreKeeperActivity extends AppCompatActivity {

    private EditText componentNameInput, componentQuantityInput;
    private Button addComponentButton, logoutButton;
    private ListView componentListView;
    private ArrayList<Component> componentList;
    private ComponentAdapter componentAdapter; // Custom adapter for ListView
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_keeper);

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("Components");

        // Initialize UI elements
        componentNameInput = findViewById(R.id.componentNameInput);
        componentQuantityInput = findViewById(R.id.componentQuantityInput);
        addComponentButton = findViewById(R.id.addComponentButton);
        logoutButton = findViewById(R.id.logoutButton);
        componentListView = findViewById(R.id.componentListView);

        // List and adapter for components
        componentList = new ArrayList<>();
        componentAdapter = new ComponentAdapter(this, componentList);
        componentListView.setAdapter(componentAdapter);

        // Add component button click listener
        addComponentButton.setOnClickListener(v -> addComponent());

        // Logout button click listener
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(StoreKeeperActivity.this, MainActivity.class));
            finish();
        });

        // Load components from Firebase
        loadComponents();
    }

    private void addComponent() {
        String name = componentNameInput.getText().toString().trim();
        String quantityStr = componentQuantityInput.getText().toString().trim();

        if (name.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new component and add it to Firebase
        String id = mDatabase.push().getKey();
        Component component = new Component(id, name, quantity);
        if (id != null) {
            mDatabase.child(id).setValue(component)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Component added", Toast.LENGTH_SHORT).show();
                            componentNameInput.setText("");
                            componentQuantityInput.setText("");
                        } else {
                            Toast.makeText(this, "Failed to add component", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loadComponents() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                componentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Component component = snapshot.getValue(Component.class);
                    if (component != null) {
                        componentList.add(component);
                    }
                }
                componentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StoreKeeperActivity.this, "Failed to load components", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
