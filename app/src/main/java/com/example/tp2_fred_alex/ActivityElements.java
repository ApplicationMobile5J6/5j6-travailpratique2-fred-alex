package com.example.tp2_fred_alex;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class ActivityElements extends AppCompatActivity {
    Button btn_update, btn_delete;
    ListView lv_donnees;
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
        lv_donnees = findViewById(R.id.lv_donnees);

        List<DonneesBD> donneesList = (List<DonneesBD>) getIntent().getSerializableExtra("donneesList");

        List<String> statusList = new ArrayList<>();
        if (donneesList != null) {
            for (DonneesBD donnees : donneesList) {
                statusList.add(donnees.statut);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                statusList
        );
        lv_donnees.setAdapter(adapter);
        lv_donnees.setOnItemClickListener((parent, view, position, id) -> {

            DonneesBD selectedDonnees = donneesList.get(position);

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
    }
    }

