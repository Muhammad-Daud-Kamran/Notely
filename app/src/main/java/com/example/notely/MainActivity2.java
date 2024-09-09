package com.example.notely;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar; // Correct import for AppCompat Toolbar
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity2 extends AppCompatActivity {

    Button btnexit, btnshare;
    ImageView new_note_image;
    RecyclerView recyclerviewnotes;
    NoteAdapter nadapter;
    DatabaseReference notesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainhome), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Optional: Set a custom title or other settings for the Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Notely");
            getSupportActionBar().setElevation(10);
            // Change toolbar title and subtitle colors
            toolbar.setTitleTextColor(getResources().getColor(R.color.your_custom_color));
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.your_custom_color));
        }
        new_note_image = findViewById(R.id.new_note_image);
        recyclerviewnotes=findViewById(R.id.recyclerviewnotes);
        recyclerviewnotes.setLayoutManager(new LinearLayoutManager(this));

        new_note_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an AlertDialog
Intent intent = new Intent(MainActivity2.this, new_note_layout.class);
startActivity(intent);
            }
        });
        notesRef = FirebaseDatabase.getInstance().getReference().child("Notes");

        // Setup the query to retrieve data from Firebase
        Query query = notesRef.orderByKey(); // You can customize the query as needed

        // Setup FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)  // Set the query and map data to Note class
                .build();

        // Initialize the adapter
        nadapter = new NoteAdapter(options);

        // Set the adapter to the RecyclerView
        recyclerviewnotes.setAdapter(nadapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for Firebase data changes
        nadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening when the activity is stopped
        nadapter.stopListening();
    }
    // Inflate the menu with the search icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notelymenu, menu); // Inflate the menu.xml
        MenuItem searchItem = menu.findItem(R.id.search);

        return true;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate custom layout
        View customView = getLayoutInflater().inflate(R.layout.custom_alert_dialogue, null);
        builder.setView(customView);

        // Find buttons in the custom view
        btnshare = customView.findViewById(R.id.btnshare);
        btnexit = customView.findViewById(R.id.btnexit);

        // Set click listener for share button
        btnshare.setOnClickListener(v -> {
            // Create an implicit intent for sharing
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Notely app where you can write your own notes: https://github.com/Muhammad-Daud-Kamran?tab=repositories");
            // Start the activity to share the text
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        // Set click listener for exit button
        btnexit.setOnClickListener(view -> finish());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
