package com.example.mvvmnotesapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NotesRepository {

    //Data members
    private NoteDao noteDao;
    private LiveData<List<NoteEntity>> allNotes;

    //Constructor to assign necessary variables
    public NotesRepository(Application application) {
        NotesDatabase dbInstance = NotesDatabase.getInstance(application);
        noteDao = dbInstance.noteDao(); /* Normally, abstract methods cannot be called (due to lack
                of a body). However, we have created NotesDatabase using the Room.databaseBuilder.
                So, Room has auto-generated all code for noteDao() method. */
        allNotes = noteDao.getAllNotes();
    }

    /** Creating methods for all database operations specified by us. */
    /** These methods form the API that the Repository exposes to the outside world. */

    public void insertNote(NoteEntity noteEntity) {
        new InsertNoteAsyncTask(noteDao).execute(noteEntity);
    }

    public void updateNote(NoteEntity noteEntity) {
        new UpdateNoteAsyncTask(noteDao).execute(noteEntity);
    }

    public void deleteNote(NoteEntity noteEntity) {
        new DeleteNoteAsyncTask(noteDao).execute(noteEntity);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {

        private NoteDao noteDao; /* Since class is static, we cannot access NoteDao object of ours
                                    directly. */

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        //For any class extending AsyncTask, we have to override doInBackground() method
        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDao.insertNote(noteEntities[0]); // Since we pass only 1 note, only index 0.
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {

        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDao.updateNote(noteEntities[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {

        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDao.deleteNote(noteEntities[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDao noteDao;

        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

}
