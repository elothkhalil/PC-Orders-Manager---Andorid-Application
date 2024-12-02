package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

public class AssemblerActivity extends AppCompatActivity {

    private ListView ordersListView;
    private Button logoutButton;
    private DatabaseReference mDatabase;
    private ArrayList<Order> orderList;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembler);

        // Initialiser la base de données et les composants UI
        mDatabase = FirebaseDatabase.getInstance().getReference("Orders");
        ordersListView = findViewById(R.id.ordersListView);
        logoutButton = findViewById(R.id.logoutButton);

        // Initialiser la liste des commandes et l'adaptateur
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList,"Assembler");
        ordersListView.setAdapter(orderAdapter);

        // Charger les commandes en attente
        loadPendingOrders();

        // Gérer le clic sur le bouton de déconnexion
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Déconnexion de Firebase
            Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AssemblerActivity.this, MainActivity.class); // Redirection vers MainActivity
            startActivity(intent);
            finish(); // Fermer l'activity actuelle
        });
    }

    private void loadPendingOrders() {
        mDatabase.orderByChild("status").equalTo("En attente").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if (order != null) {
                        order.setOrderId(dataSnapshot.getKey());
                        orderList.add(order);
                    }
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AssemblerActivity.this, "Erreur de chargement des commandes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
