package com.example.tp2_fred_alex;

import android.content.Intent;
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

public class ActivityAjout extends AppCompatActivity {
    EditText et_country, et_city, et_status, et_age;
    FirebaseDatabase bd;
    DatabaseReference ref;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        et_country = findViewById(R.id.et_country);
        et_city = findViewById(R.id.et_city);
        et_age = findViewById(R.id.et_age);
        et_status = findViewById(R.id.et_status);
        btn_save = findViewById(R.id.bt_save);
        bd = FirebaseDatabase.getInstance();
        ref = bd.getReference("Donnees");
        btn_save.setOnClickListener(v -> enregisterDansDonnees());
    }

    //Enregistre les donnness dans la BD firebase
    private void enregisterDansDonnees() {
        String country = et_country.getText().toString();
        String city = et_city.getText().toString();
        String age = et_age.getText().toString();
        String status = et_status.getText().toString();

        if (country.isEmpty() || city.isEmpty() || age.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent etre remplis", Toast.LENGTH_SHORT).show();

        } else {
            ref.child(status).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    Toast.makeText(this, "Le role est deja occupé", Toast.LENGTH_SHORT).show();
                } else {

                    DonneesBD donnees = new DonneesBD(country, city, age, status);
                    ref.child(status).setValue(donnees).addOnCompleteListener(BDtask -> {

                        if (BDtask.isSuccessful()) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("country", country);
                            resultIntent.putExtra("city", city);
                            resultIntent.putExtra("age", age);
                            resultIntent.putExtra("status", status);
                            setResult(RESULT_OK, resultIntent);
                            Toast.makeText(this, "Donnees enregistrees avec succes!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "C'est invalide bro" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

    }
}