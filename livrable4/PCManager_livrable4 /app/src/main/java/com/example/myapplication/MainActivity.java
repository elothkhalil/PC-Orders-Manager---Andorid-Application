package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText usernameInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");  // "Users" pour stocker les rôles des utilisateurs

        // Initialiser les éléments d'interface utilisateur
        Button loginButton = findViewById(R.id.loginButton);
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);

        // Gestion du clic sur le bouton de connexion
        loginButton.setOnClickListener(v -> loginUser());
    }

    // Méthode pour gérer la connexion de l'utilisateur
    private void loginUser() {
        String email = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Veuillez entrer les informations de connexion", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            fetchUserRoleAndRedirect(currentUser.getUid());
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Erreur d'authentification", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Récupère le rôle de l'utilisateur dans Firebase et redirige en conséquence
    private void fetchUserRoleAndRedirect(String userId) {
        mDatabase.child(userId).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role = snapshot.getValue(String.class);
                if (role != null) {
                    openNextActivity(role);
                } else {
                    Toast.makeText(MainActivity.this, "Rôle non défini pour cet utilisateur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Erreur de récupération du rôle", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Redirige vers l'activité appropriée en fonction du rôle de l'utilisateur
    private void openNextActivity(String role) {
        Intent intent;
        switch (role) {
            case "admin":
                intent = new Intent(this, AdminActivity.class);
                break;
            case "storekeeper":
                intent = new Intent(this, StoreKeeperActivity.class);
                break;
            case "assembler":
                intent = new Intent(this, AssemblerActivity.class);
                break;
            case "requester":
                intent = new Intent(this, RequesterActivity.class);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                Toast.makeText(this, "Rôle non reconnu", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
        finish();  // Fermer MainActivity pour éviter de revenir en arrière
    }
}
