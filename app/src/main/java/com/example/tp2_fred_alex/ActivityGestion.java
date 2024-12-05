package com.example.tp2_fred_alex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ActivityGestion extends AppCompatActivity {

    Button btn_deconnexion;
    Button btn_profile;
    FirebaseAuth bdAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gestion);

        // Initialisation de l'instance FirebaseAuth et du bouton
        Intent intent = getIntent();
        bdAuth = FirebaseAuth.getInstance();
        btn_deconnexion = findViewById(R.id.btn_deco);  // Assurez-vous que cet ID est correct dans le fichier XML
        btn_profile=findViewById(R.id.btn_profile);
        // Application des Insets pour l'espace de la barre de statut, etc.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // OnClickListener pour déconnexion
        btn_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Déconnexion et redirection vers la page de connexion
                bdAuth.signOut();
                Intent intention = new Intent(ActivityGestion.this, MainActivity.class);
                startActivity(intention);

            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = intent.getStringExtra("mail");
                if (uid != null) {
                    Log.d("ActivityGestion", "Résultat de l'intent : " + uid);

                    // Ensuite, vous envoyez cette donnée à l'activité suivante
                    Intent intention = new Intent(ActivityGestion.this, ActivityGestionProfile.class);
                    intention.putExtra("uid", uid);  // Envoyer 'uid' à la prochaine activité
                    startActivity(intention);

                } else {
                    Log.d("ActivityGestion", "Le mail est null, aucune donnée reçue");
                }
            }
        });
    }
}