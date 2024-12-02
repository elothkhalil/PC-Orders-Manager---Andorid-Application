package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Requester;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RequesterAdapter extends ArrayAdapter<Requester> {
    private Context context;
    private List<Requester> requesterList;

    public RequesterAdapter(Context context, List<Requester> requesterList) {
        super(context, 0, requesterList);
        this.context = context;
        this.requesterList = requesterList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_requester, parent, false);
        }

        // Récupérer l'élément actuel
        Requester requester = requesterList.get(position);

        // Initialiser les vues
        TextView emailTextView = convertView.findViewById(R.id.emailTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        // Définir les valeurs des vues
        emailTextView.setText(requester.getEmail());

        // Ajouter une action au bouton "Supprimer"
        deleteButton.setOnClickListener(v -> {
            // Supprimer le requester de Firebase
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Users").child(requester.getId()).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Supprimer de la liste locale
                            requesterList.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Utilisateur supprimé", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Échec de la suppression", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return convertView;
    }
}
