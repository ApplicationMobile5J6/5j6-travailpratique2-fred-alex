package com.example.tp2_fred_alex;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityChange extends AppCompatActivity {
    EditText et_country, et_city, et_age, et_status;
    Button btn_save;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_country = findViewById(R.id.et_country);
        et_city = findViewById(R.id.et_city);
        et_age = findViewById(R.id.et_age);
        et_status = findViewById(R.id.et_status);
        btn_save = findViewById(R.id.btn_save);

        String country = getIntent().getStringExtra("country");
        String city = getIntent().getStringExtra("city");
        String age = getIntent().getStringExtra("age");
        String status = getIntent().getStringExtra("status");


        et_country.setText(country);
        et_city.setText(city);
        et_age.setText(age);
        et_status.setText(status);

        databaseReference = FirebaseDatabase.getInstance().getReference("Donnees");

        btn_save.setOnClickListener(v -> {
            String country_nouveau = et_country.getText().toString();
            String city_nouveau = et_city.getText().toString();
            String age_nouveau = et_age.getText().toString();
            String status_nouveau = et_status.getText().toString();
            if (country_nouveau.isEmpty() || city_nouveau.isEmpty() || age_nouveau.isEmpty() || status_nouveau.isEmpty()) {
                Toast.makeText(ActivityChange.this, "Tout doit etre rempli.", Toast.LENGTH_SHORT).show();
                return;
            }
            DonneesBD updatedDonnees = new DonneesBD(country_nouveau, city_nouveau, age_nouveau, status_nouveau);


            databaseReference.child(status).removeValue().addOnCompleteListener(removeTask -> {
                if (removeTask.isSuccessful()) {

                    databaseReference.child(status_nouveau).setValue(updatedDonnees).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(ActivityChange.this, "Changement succes!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(ActivityChange.this, "C foutu" + updateTask.getException(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        });


    }
}