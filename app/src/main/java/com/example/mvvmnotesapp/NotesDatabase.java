package com.example.mvvmnotesapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {NoteEntity.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {

    private static NotesDatabase notesDatabaseInstance;

    public abstract NoteDao noteDao();

    public static synchronized NotesDatabase getInstance(Context context) {
        if(notesDatabaseInstance == null) {
            notesDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    NotesDatabase.class, "notes_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomDbCallback)
                    .build();
        }
        return notesDatabaseInstance;
    }

    //It is a static method as it is later called from getInstance() method, which is static.
    private static RoomDatabase.Callback roomDbCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new AddInitialNotesAsyncTask(notesDatabaseInstance).execute();
        }
    };

    private static class AddInitialNotesAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDao noteDao;

        private AddInitialNotesAsyncTask(NotesDatabase notesDatabase) {
            this.noteDao = notesDatabase.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insertNote(new NoteEntity("Welcome to MVVM Notes App!",
                    "It aims to clarify MVVM architecture and RoomDB related concepts.",
                    1));
            noteDao.insertNote(new NoteEntity("About me...", "Hi, I am Neha Binwal, " +
                    "an aspiring Android developer.", 2));
            noteDao.insertNote(new NoteEntity("Use of App", "Instead of simply forgetting" +
                    " mundane yet important tasks, note them down in this simple, easy-to-use app " +
                    "with a clean interface.", 3));
            return null;
        }
    }

}
