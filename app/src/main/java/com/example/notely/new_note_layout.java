package com.example.notely;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

public class new_note_layout extends AppCompatActivity {
    ImageView backarrow;
    Button btnsave;
    EditText ettitle, etdescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_note_layout);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.newnotemain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backarrow = findViewById(R.id.backarrow);
        btnsave = findViewById(R.id.btnsave);
        ettitle = findViewById(R.id.ettitle);
        etdescription = findViewById(R.id.etdescription);

        backarrow.setOnClickListener(view -> {
            Intent backedarrow = new Intent(new_note_layout.this, MainActivity2.class);
            startActivity(backedarrow);
        });

        btnsave.setOnClickListener(view -> {
            TextView note_timestamp = findViewById(R.id.note_timestamp);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            Date newDate = new Date();
            String timestamp = formatter.format(newDate);

            String title = ettitle.getText().toString().trim();
            String description = etdescription.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Title or description cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Creating a HashMap for storing the data
            HashMap<String, Object> data = new HashMap<>();
            data.put("title", title);
            data.put("description", description);
            data.put("timestamp", timestamp);

            // Getting reference to Firebase Realtime Database and adding a new entry
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Notes");
            databaseRef.push().setValue(data) // Corrected method for pushing new data
                    .addOnSuccessListener(unused ->
                            Toast.makeText(getApplicationContext(), "Note created", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
