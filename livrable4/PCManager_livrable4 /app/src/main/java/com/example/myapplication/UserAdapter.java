package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> users;

    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Inflater le layout personnalisé pour chaque élément utilisateur
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        }

        // Obtenez l'utilisateur actuel
        User user = getItem(position);

        // Configurez les vues du layout
        TextView userInfo = convertView.findViewById(R.id.userInfo);
        userInfo.setText(user.toString());

        // Bouton pour supprimer l'utilisateur
        Button deleteUserButton = convertView.findViewById(R.id.deleteUserButton);
        deleteUserButton.setOnClickListener(v -> {
            users.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Utilisateur supprimé", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }
}
