package com.example.mvvmnotesapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    //Data members
    private NotesRepository repository;
    private LiveData<List<NoteEntity>> allNotes;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        repository = new NotesRepository(application);
        allNotes = repository.getAllNotes();
    }

    /** Later, our Activity only has a reference to ViewModel and not to Repository.
     *  Thus, we need to create wrapper methods for the database operations specified in
     *  NotesRepository.java, in this NotesViewModel.java class. */
    public void insertNote(NoteEntity noteEntity) {
        repository.insertNote(noteEntity);
    }

    public void updateNote(NoteEntity noteEntity) {
        repository.updateNote(noteEntity);
    }

    public void deleteNote(NoteEntity noteEntity) {
        repository.deleteNote(noteEntity);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }
}
