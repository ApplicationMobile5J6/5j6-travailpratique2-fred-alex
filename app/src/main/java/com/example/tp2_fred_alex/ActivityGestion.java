package com.example.tp2_fred_alex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityGestion extends AppCompatActivity {

    Button btn_deconnexion, btn_profile, btn_ajouter, btn_data;
    FirebaseAuth bdAuth;
    DatabaseReference ref;
    FirebaseDatabase bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gestion);

        // Initialisation de l'instance FirebaseAuth et du bouton
        Intent intent = getIntent();
        bdAuth = FirebaseAuth.getInstance();
        btn_deconnexion = findViewById(R.id.btn_deco);  // Assurez-vous que cet ID est correct dans le fichier XML
        btn_profile=findViewById(R.id.btn_profile);
        btn_ajouter=findViewById(R.id.buttonAjouter);
        btn_data= findViewById(R.id.buttonData);
        // Application des Insets pour l'espace de la barre de statut, etc.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bd=FirebaseDatabase.getInstance();


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

        //OnClickListener pour afficher la page d'ajout
        btn_ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityGestion.this, ActivityAjout.class);
                startActivity(intent);

            }
        });
        //OnClickListener pour afficher la page d'elements
        btn_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envoyerListeDonnees();
            }
        });

        //onClickListener pour afficher profile
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
    //Parcoure la liste de donnees et l'envoie a ActivityElements
    private void envoyerListeDonnees() {
        ref=bd.getReference("Donnees");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DonneesBD> donneesList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonneesBD donnees = snapshot.getValue(DonneesBD.class);
                    if (donnees != null) {
                        donneesList.add(donnees);
                    }
                }

                if (!donneesList.isEmpty()) {
                    Intent intent = new Intent(ActivityGestion.this, ActivityElements.class);
                    intent.putExtra("donneesList", new ArrayList<>(donneesList));
                    startActivity(intent);
                } else {
                    Toast.makeText(ActivityGestion.this, "BD Vide", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ActivityGestion.this, "Erreur: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
