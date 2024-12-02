package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {
    private String userRole; // Nouvelle variable pour stocker le rôle de l'utilisateur

    // Constructeur mis à jour
    public OrderAdapter(Context context, List<Order> orders, String userRole) {
        super(context, 0, orders);
        this.userRole = userRole; // Initialisez le rôle ici
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item, parent, false);
        }

        Order order = getItem(position);

        TextView componentId = convertView.findViewById(R.id.componentId);
        TextView status = convertView.findViewById(R.id.status);
        TextView createdAt = convertView.findViewById(R.id.createdAt);
        TextView modifiedAt = convertView.findViewById(R.id.modifiedAt);

        Button approveButton = convertView.findViewById(R.id.approveButton);
        Button rejectButton = convertView.findViewById(R.id.rejectButton);

        // Remplir les informations de la commande
        componentId.setText("Composant: " + order.getComponentId());
        status.setText("Statut: " + order.getStatus());
        createdAt.setText("Créé le: " + new java.util.Date(order.getCreatedAt()).toString());
        modifiedAt.setText("Modifié le: " + new java.util.Date(order.getModifiedAt()).toString());

        // Vérifiez le rôle pour afficher ou masquer les boutons
        if ("Assembler".equals(userRole)) {
            approveButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);

            // Ajoutez les actions pour ces boutons uniquement si le rôle est "Assembler"
            approveButton.setOnClickListener(v -> {
                order.setStatus("Approuvé");
                order.setModifiedAt(System.currentTimeMillis());
                FirebaseDatabase.getInstance().getReference("Orders").child(order.getOrderId()).setValue(order)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Commande approuvée", Toast.LENGTH_SHORT).show();
                            }
                        });
            });

            rejectButton.setOnClickListener(v -> {
                order.setStatus("Rejetée");
                order.setModifiedAt(System.currentTimeMillis());
                FirebaseDatabase.getInstance().getReference("Orders").child(order.getOrderId()).setValue(order)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Commande rejetée", Toast.LENGTH_SHORT).show();
                            }
                        });
            });
        } else {
            // Cachez les boutons si l'utilisateur n'est pas un assembleur
            approveButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
        }

        return convertView;
    }
}
