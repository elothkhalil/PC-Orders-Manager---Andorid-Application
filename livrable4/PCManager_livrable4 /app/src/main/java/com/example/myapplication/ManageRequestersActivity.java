package com.example.myapplication;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Requester;
import com.example.myapplication.RequesterAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageRequestersActivity extends AppCompatActivity {

    private ListView listView;
    private List<Requester> requesterList;
    private RequesterAdapter adapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requesters);

        listView = findViewById(R.id.requestersListView);
        requesterList = new ArrayList<>();
        adapter = new RequesterAdapter(this, requesterList);
        listView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        loadRequesters();
    }

    private void loadRequesters() {
        mDatabase.child("Users").orderByChild("role").equalTo("requester")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        requesterList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Requester requester = data.getValue(Requester.class);
                            if (requester != null) {
                                requester.setId(data.getKey()); // Ajouter l'ID
                                requesterList.add(requester);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ManageRequestersActivity.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
