package com.example.tp2_fred_alex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tp2_fred_alex.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextInputEditText tiet_courriel, tiet_mdp;
    Button btn_connexion, btn_creerCompte;
    FirebaseAuth bdAuth;
    String courriel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);



        // Initialisation des variables
        bdAuth = FirebaseAuth.getInstance();
        tiet_courriel = findViewById(R.id.tiet_courriel);
        tiet_mdp = findViewById(R.id.tiet_mdp);  // Correct this line
        btn_connexion = findViewById(R.id.button_login);
        btn_creerCompte = findViewById(R.id.btn_signup);

        // Ajouter un Listener pour appliquer les Insets de la fenêtre (système)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Écouteur pour le bouton "Créer un compte"
        btn_creerCompte.setOnClickListener(v -> {
            Intent intention = new Intent(MainActivity.this, ActivitySingup.class);
            startActivity(intention);
            finish();
        });

        // Écouteur pour le bouton "Se connecter"
        btn_connexion.setOnClickListener(v -> {
            courriel = tiet_courriel.getText().toString();
            String mdp = tiet_mdp.getText().toString();

            // Vérifier si l'email et le mot de passe sont valides
            if (Patterns.EMAIL_ADDRESS.matcher(courriel).matches() && mdp.length() >= 10) {
                bdAuth.signInWithEmailAndPassword(courriel, mdp).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser usager = bdAuth.getCurrentUser();
                        Intent intention = new Intent(MainActivity.this, ActivityGestion.class);
                        courriel = tiet_courriel.getText().toString().replace(".", ",");
                       // Log.d("ActivityGestion", "Email reçu : " + courriel);
                        intention.putExtra("mail", courriel);
                        startActivity(intention);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (!Patterns.EMAIL_ADDRESS.matcher(courriel).matches()) {
                tiet_courriel.setError("Veuillez entrer un courriel valide");
                tiet_courriel.requestFocus();
            } else {
                tiet_mdp.setError("Le mot de passe doit avoir 10 caractères et plus");
                tiet_mdp.requestFocus();
            }
        });
    }
}
