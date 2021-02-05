package com.example.mvvmnotesapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class NoteEntity {

    /** Data members - Room automatically generates columns for these fields */
    @PrimaryKey(autoGenerate = true)
    private int noteId;

    private String title;
    @ColumnInfo(name = "note_content")
    private String noteContent;
    @ColumnInfo(name = "note_priority")
    private int notePriority;

    public NoteEntity(String title, String noteContent, int notePriority) {
        this.title = title;
        this.noteContent = noteContent;
        this.notePriority = notePriority;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getTitle() {
        return title;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public int getNotePriority() {
        return notePriority;
    }

}
