package com.example.tp2_fred_alex;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tp2_fred_alex.databinding.ActivityMain2Binding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivitySingup extends AppCompatActivity {
    ActivityMain2Binding binding;


    TextInputEditText tiet_courriel, tiet_mdp, tiet_mdpConfirmation,tiet_nom;
    Button btn_inscription;
    FirebaseAuth bdAuth;
    Dialog bteDialog;
    String email,nom;
    FirebaseDatabase bd;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        tiet_nom=findViewById(R.id.tiet_nom);
        tiet_courriel = findViewById(R.id.tiet_courriel);
        tiet_mdp = findViewById(R.id.tiet_mdp);
        tiet_mdpConfirmation = findViewById(R.id.tiet_confirmmdp);
        btn_inscription = findViewById(R.id.btn_inscription);
        bdAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courriel = tiet_courriel.getText().toString();
                String mdp = tiet_mdp.getText().toString();
                String mdp_confirmation = tiet_mdpConfirmation.getText().toString();
                nom=binding.tietNom.getText().toString();
                email=binding.tietCourriel.getText().toString();
                if (Patterns.EMAIL_ADDRESS.matcher(courriel).matches()) {
                    if (mdp.equals(mdp_confirmation) && mdp.length() >= 5) {
                        Utilisateur user = new Utilisateur(nom,email);
                        email = tiet_courriel.getText().toString().replace(".", ",");
                        bd=FirebaseDatabase.getInstance();
                        ref=bd.getReference("Utilisateurs");
                        ref.child(email).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ActivitySingup.this, "User dans bd", Toast.LENGTH_SHORT).show();
                            }

                        });
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        if (imm != null && getCurrentFocus() != null) {
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }

                        bdAuth.createUserWithEmailAndPassword(courriel, mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ActivitySingup.this, "Utilisateur créé", Toast.LENGTH_SHORT).show();
                                    FirebaseUser usager = bdAuth.getCurrentUser();
                                    if (usager != null) {
                                        Intent intention = new Intent(ActivitySingup.this, ActivityGestion.class);
                                        email = tiet_courriel.getText().toString().replace(".", ",");

                                        intention.putExtra("mail", email);
                                        startActivity(intention);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(ActivitySingup.this, "Erreur d'authentification", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ActivitySingup.this, "Les mots de passe ne correspondent pas ou sont trop courts", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivitySingup.this, "Veuillez entrer un courriel valide", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
