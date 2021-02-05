package com.example.mvvmnotesapp;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

/* We are informing the custom Adapter class about the custom ViewHolder class that we want to use. */
public class MyNotesAdapter extends ListAdapter<NoteEntity, MyNotesAdapter.MyNoteHolder> {

    private OnItemClickListener itemClickListener;

    public MyNotesAdapter() {
        super(DIFF_UTIL);
    }

    public static final DiffUtil.ItemCallback<NoteEntity> DIFF_UTIL = new DiffUtil.ItemCallback<NoteEntity>() {
        /** Comparison logic */
        @Override
        public boolean areItemsTheSame(@NonNull NoteEntity oldItem, @NonNull NoteEntity newItem) {
            return (oldItem.getNoteId() == newItem.getNoteId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull NoteEntity oldItem, @NonNull NoteEntity newItem) {
            return (oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getNoteContent().equals(newItem.getNoteContent()) &&
                    oldItem.getNotePriority() == newItem.getNotePriority());
        }
    };

    @NonNull
    @Override
    public MyNoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new MyNoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNoteHolder holder, int position) {
        NoteEntity currentNoteEntity = getItem(position);
        holder.noteTitle.setText(currentNoteEntity.getTitle());
        holder.noteContent.setText(currentNoteEntity.getNoteContent());
        holder.notePriority.setText(String.valueOf(currentNoteEntity.getNotePriority()));
    }

    public NoteEntity getNoteEntityAt(int position) {
        return getItem(position);
    }

    /* A ViewHolder describes an item view and metadata about its place within the RecyclerView. */
    class MyNoteHolder extends RecyclerView.ViewHolder {

        private TextView notePriority;
        private TextView noteTitle;
        private TextView noteContent;

        /* The itemView object is the individual card itself, that gets passed to us. */
        public MyNoteHolder(@NonNull View itemView) {
            super(itemView);
            notePriority = itemView.findViewById(R.id.note_priority_text_view);
            noteTitle = itemView.findViewById(R.id.note_title_text_view);
            noteContent = itemView.findViewById(R.id.note_content_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int notePosition = getAdapterPosition();
                    if (itemClickListener != null && notePosition != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(getItem(notePosition));
                    }
                }
            });

        }

    }

    public interface OnItemClickListener {
        void onItemClick(NoteEntity noteEntity);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
