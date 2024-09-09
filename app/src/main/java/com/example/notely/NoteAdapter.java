package com.example.notely;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class NoteAdapter extends FirebaseRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {

    public NoteAdapter(@NonNull FirebaseRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_note_design, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        // Bind the Note object to the ViewHolder
        holder.noteTitle.setText(note.getTitle());
        holder.noteDescription.setText(note.getDescription());
        holder.noteTimestamp.setText(note.getTimestamp());
        holder.itemView.setOnLongClickListener(view -> {
            // Show confirmation dialog
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Delete", (dialogInterface, i) -> {
                        // Remove note from Firebase
                        getRef(position).removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    // Successfully deleted from Firebase
                                    Toast.makeText(view.getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to delete from Firebase
                                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        // Do nothing on cancel
                        dialogInterface.dismiss();
                    })
                    .create()
                    .show();
            return true; // Indicate that the long-click event is consumed
        });
    }

    // ViewHolder class to hold UI elements for each note
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteDescription, noteTimestamp;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteDescription = itemView.findViewById(R.id.note_description);
            noteTimestamp = itemView.findViewById(R.id.note_timestamp);
        }
    }
}
