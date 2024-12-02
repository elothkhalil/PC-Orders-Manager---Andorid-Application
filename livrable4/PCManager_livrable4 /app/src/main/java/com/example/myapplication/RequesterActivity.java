package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.Map;

public class RequesterActivity extends AppCompatActivity {

    private Spinner componentSpinner;
    private ListView ordersListView;
    private Button placeOrderButton, logoutButton;
    private DatabaseReference mDatabase;
    private ArrayList<String> componentIds;
    private ArrayList<String> componentNames;
    private ArrayList<Order> orderList;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requester);

        // Initialisation de la référence à la base de données Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialisation des éléments d'interface utilisateur
        componentSpinner = findViewById(R.id.componentSpinner);
        ordersListView = findViewById(R.id.ordersListView);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Initialisation de la liste des commandes et de l'adaptateur personnalisé
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList, "Requester");
        ordersListView.setAdapter(orderAdapter);

        // Chargement des composants dans le Spinner
        loadComponents();

        // Chargement des commandes existantes
        loadOrders();

        // Gestion du clic sur le bouton "Place Order"
        placeOrderButton.setOnClickListener(v -> placeOrder());

        // Gestion du clic sur le bouton de déconnexion
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(RequesterActivity.this, MainActivity.class));
            finish();
        });

        // Gestion du clic long sur une commande pour la supprimer si elle est en attente
        ordersListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Order selectedOrder = orderList.get(position);
            if (selectedOrder != null && "En attente".equals(selectedOrder.getStatus())) {
                deleteOrder(selectedOrder);
            } else {
                Toast.makeText(RequesterActivity.this, "Cette commande ne peut pas être supprimée", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void loadComponents() {
        componentIds = new ArrayList<>();
        componentNames = new ArrayList<>();

        mDatabase.child("Components").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                componentIds.clear();
                componentNames.clear();

                if (!dataSnapshot.exists()) {
                    Toast.makeText(RequesterActivity.this, "No components found in Firebase", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);

                    if (name != null) {
                        componentIds.add(id);
                        componentNames.add(name);
                    }
                }

                if (componentNames.isEmpty()) {
                    Toast.makeText(RequesterActivity.this, "No components available to display", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RequesterActivity.this,
                            android.R.layout.simple_spinner_item, componentNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    componentSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RequesterActivity.this, "Failed to load components: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOrders() {
        String requesterId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("Orders").orderByChild("requesterId").equalTo(requesterId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Order order = dataSnapshot.getValue(Order.class);
                            if (order != null && order.getOrderId() == null) {
                                // Si l'orderId est manquant, on le récupère à partir de la clé du nœud
                                order.setOrderId(dataSnapshot.getKey());
                            }
                            if (order != null) {
                                orderList.add(order);
                            }
                        }
                        orderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RequesterActivity.this, "Erreur de chargement des commandes", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void placeOrder() {
        int selectedIndex = componentSpinner.getSelectedItemPosition();
        if (selectedIndex == -1) {
            Toast.makeText(this, "Please select a component", Toast.LENGTH_SHORT).show();
            return;
        }

        String componentId = componentIds.get(selectedIndex);
        String requesterId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (requesterId == null || requesterId.isEmpty()) {
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Génération d'un nouvel ID pour la commande
        String orderId = mDatabase.child("Orders").push().getKey();
        if (orderId == null) {
            Toast.makeText(this, "Error generating order ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Création de l'instance Order avec orderId
        Order order = new Order(orderId, requesterId, componentId, System.currentTimeMillis(), System.currentTimeMillis(), "En attente");

        // Enregistrement de l'instance Order dans Firebase en incluant l'ID de commande
        mDatabase.child("Orders").child(orderId).setValue(order)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                        loadOrders(); // Actualise la liste des commandes
                    } else {
                        Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteOrder(Order order) {
        if (order.getOrderId() != null) {
            mDatabase.child("Orders").child(order.getOrderId()).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RequesterActivity.this, "Commande supprimée", Toast.LENGTH_SHORT).show();
                            loadOrders(); // Actualise la liste des commandes
                        } else {
                            Toast.makeText(RequesterActivity.this, "Échec de la suppression de la commande", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(RequesterActivity.this, "Erreur : ID de la commande non valide", Toast.LENGTH_SHORT).show();
        }
    }
}
