package com.example.mvvmnotesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    private final int ADD_NOTE_REQUEST_CODE = 1;
    private final int EDIT_NOTE_REQUEST_CODE = 2;
    private NotesViewModel notesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addNoteButton = findViewById(R.id.add_note_button);
        addNoteButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST_CODE);
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        MyNotesAdapter notesAdapter = new MyNotesAdapter();
        recyclerView.setAdapter(notesAdapter);

        notesViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(NotesViewModel.class);
        notesViewModel.getAllNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                //This is where RecyclerView is updated.
                notesAdapter.submitList(noteEntities);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                NoteEntity noteEntity = notesAdapter.getNoteEntityAt(viewHolder.getAdapterPosition());
                notesViewModel.deleteNote(noteEntity);
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.design_default_color_error))
                        .addSwipeRightLabel("Delete")
                        .addSwipeLeftLabel("Delete")
                        .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 16)
                        .setSwipeRightLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 16)
                        .setSwipeLeftLabelColor(ContextCompat.getColor(MainActivity.this, R.color.design_default_color_background))
                        .setSwipeRightLabelColor(ContextCompat.getColor(MainActivity.this, R.color.design_default_color_background))
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);

        notesAdapter.setOnItemClickListener(new MyNotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NoteEntity noteEntity) {
                /* Here, we want to go to the same Activity as the one where new notes got added */
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);

                /* Because ID is the primary key i.e. unique identifier, which enables system to know
                   which note to update */
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, noteEntity.getNoteId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, noteEntity.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, noteEntity.getNoteContent());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, noteEntity.getNotePriority());

                startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 0);

            // Creating a new NoteEntity
            NoteEntity noteEntity = new NoteEntity(title, description, priority);
            notesViewModel.insertNote(noteEntity);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note could not be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 0);

            // Creating a new NoteEntity
            NoteEntity noteEntity = new NoteEntity(title, description, priority);
            noteEntity.setNoteId(id);
            notesViewModel.updateNote(noteEntity);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Note dismissed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_all_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes_icon:
                notesViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}