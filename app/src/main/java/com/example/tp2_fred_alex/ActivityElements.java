package com.example.tp2_fred_alex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityElements extends AppCompatActivity {
    Button btn_update, btn_delete, btn_return;
    ListView lv_donnees;
    int lv_position = -1;
    DatabaseReference databaseReference;
    List<DonneesBD> donneesList;
    ArrayAdapter<String> adapter;
    List<String> statusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_elements);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        btn_return = findViewById(R.id.btn_return);
        lv_donnees = findViewById(R.id.lv_donnees);


        databaseReference = FirebaseDatabase.getInstance().getReference("Donnees");


        donneesList = new ArrayList<>();
        statusList = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                statusList
        );
        lv_donnees.setAdapter(adapter);

        miseAJour();

        lv_donnees.setOnItemClickListener((parent, view, position, id) -> {
            DonneesBD selectedDonnees = donneesList.get(position);
            lv_position = position;
            FragmentDonnees fragment = FragmentDonnees.newInstance(
                    selectedDonnees.pays,
                    selectedDonnees.ville,
                    selectedDonnees.age
            );

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fr_fragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });


        btn_update.setOnClickListener(v -> {
            if (lv_position != -1) {
                DonneesBD selectedDonnees = donneesList.get(lv_position);

                Intent intent = new Intent(ActivityElements.this, ActivityChange.class);
                intent.putExtra("country", selectedDonnees.pays);
                intent.putExtra("city", selectedDonnees.ville);
                intent.putExtra("age", selectedDonnees.age);
                intent.putExtra("status", selectedDonnees.statut);
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(ActivityElements.this, "Choisir un statut.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_delete.setOnClickListener(v -> {
            if (lv_position != -1) {
                DonneesBD selectedDonnees = donneesList.get(lv_position);
                databaseReference.child(selectedDonnees.statut).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ActivityElements.this, "Bye bye", Toast.LENGTH_SHORT).show();

                        donneesList.remove(lv_position);
                        statusList.remove(lv_position);

                        adapter.notifyDataSetChanged();

                        lv_position = -1;
                    }
                });
            } else {
                Toast.makeText(ActivityElements.this, "Choisir la victime.", Toast.LENGTH_SHORT).show();
            }
        });
        btn_return.setOnClickListener(v ->{
            finish();
        });

    }

    private void miseAJour() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donneesList.clear();
                statusList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonneesBD donnees = snapshot.getValue(DonneesBD.class);
                    if (donnees != null) {
                        donneesList.add(donnees);
                        statusList.add(donnees.statut);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityElements.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            miseAJour();
        }
    }
}
