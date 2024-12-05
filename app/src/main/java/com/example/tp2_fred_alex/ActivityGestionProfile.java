package com.example.tp2_fred_alex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tp2_fred_alex.databinding.ActivityGestionProfileBinding;
import com.example.tp2_fred_alex.databinding.ActivityMain2Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;


public class ActivityGestionProfile extends AppCompatActivity {
    ActivityGestionProfileBinding binding;
    TextView tvNom, tvEmail;
    EditText editTextNom, editTextEmail;

    Button  btn_modifNom,btn_modifEmail;
    FirebaseDatabase bd;
    DatabaseReference ref;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGestionProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    //    setContentView(R.layout.activity_gestion_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        // Initialisation des vues
        tvNom = findViewById(R.id.tv_nom);
        tvEmail = findViewById(R.id.tv_email);
        editTextNom = findViewById(R.id.et_nom);
        editTextEmail = findViewById(R.id.et_email);
        btn_modifEmail=findViewById(R.id.btn_modifEmail);
        btn_modifNom=findViewById(R.id.btn_modifNom);

        // Initialisation de Firebase
        bd = FirebaseDatabase.getInstance();
        ref = bd.getReference("Utilisateurs");

        // Lecture des données depuis Firebase
        uid = intent.getStringExtra("uid");; // Remplace par le nom de l'utilisateur (clé)

        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nom = snapshot.child("nomcomplet").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    // Mise à jour des vues avec les données récupérées
                    tvNom.setText(nom);
                    tvEmail.setText(email);
                    editTextNom.setText(nom);
                    editTextEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        btn_modifNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                majDonnees(  editTextNom.getText().toString());
            }
        });



        btn_modifEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                majDonnees(editTextEmail.getText().toString());
            }
        });

    }
    private void majDonnees(String chaine) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            if (chaine.contains("@")){


                user.updateEmail(chaine)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updateUserInDatabase(chaine);  // Update user data in Realtime Database
                                } else {
                                    Toast.makeText(ActivityGestionProfile.this, "Erreur dans la mise à jour de l'email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                updateUserInDatabase(chaine);
            }

        }
    }


    private void updateUserInDatabase(String chaine) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Utilisateurs");

        if (chaine.contains("@")) {
            String uidmaj = chaine.replace(".", ","); // Nouvelle clé basée sur l'email

            // Récupérer les données existantes avant la mise à jour
            ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Récupérer les données existantes
                        Map<String, Object> existingData = (Map<String, Object>) snapshot.getValue();

                        if (existingData != null) {

                            String nomComplet = (String) existingData.get("nomcomplet");
                            existingData.put("email", chaine);


                            // Mise à jour de l'utilisateur avec le nouvel email
                            ref.child(uidmaj).setValue(existingData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        ref.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ActivityGestionProfile.this, "Email mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                                    uid=uidmaj;
                                                } else {
                                                    Toast.makeText(ActivityGestionProfile.this, "Erreur lors de la suppression de l'ancien utilisateur", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ActivityGestionProfile.this, "Erreur lors de la mise à jour de l'email dans la base de données", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(ActivityGestionProfile.this, "Données utilisateur introuvables", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ActivityGestionProfile.this, "Erreur : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Mise à jour du nom
            Map<String, Object> usager = new HashMap<>();
            usager.put("nomcomplet", chaine);

            ref.child(uid).updateChildren(usager).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ActivityGestionProfile.this, "Nom mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActivityGestionProfile.this, "Erreur lors de la mise à jour du nom", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }









}
