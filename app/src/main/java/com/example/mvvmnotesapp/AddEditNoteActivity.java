package com.example.mvvmnotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Session2Command;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.mvvmnotesapp.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.mvvmnotesapp.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.mvvmnotesapp.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.mvvmnotesapp.EXTRA_PRIORITY";

    /* Data members to hold the values of the Views in layout */
    private EditText titleEditText;
    private EditText contentEditText;
    private NumberPicker priorityNumberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Associating data members with correct Views.
        titleEditText = findViewById(R.id.title_edit_text);
        contentEditText = findViewById(R.id.content_edit_text);
        priorityNumberPicker = findViewById(R.id.priority_number_picker);

        // Setting the min and max values of NumberPicker, which cannot be done in XML
        priorityNumberPicker.setMinValue(1);
        priorityNumberPicker.setMaxValue(25);

        Intent callingIntent = getIntent();

        //To get the close button in top-left corner (ic_close icon)
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        if (callingIntent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");

            String currentTitle = callingIntent.getStringExtra(EXTRA_TITLE);
            String currentDescription = callingIntent.getStringExtra(EXTRA_DESCRIPTION);
            int currentPriority = callingIntent.getIntExtra(EXTRA_PRIORITY, 1);

            titleEditText.setText(currentTitle);
            contentEditText.setText(currentDescription);
            priorityNumberPicker.setValue(currentPriority);
        }
        else
            setTitle("Add Note");

    }

    /* To get the Save icon in the top-right corner of the action bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    /* To ensure that when the ic_save icon is clicked, we are redirected to a piece of code
     *  that does exactly that */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note_menu:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        // Get the input entered by the user
        String noteTitle = String.valueOf(titleEditText.getText());
        String noteContent = String.valueOf(contentEditText.getText());
        int notePriority = priorityNumberPicker.getValue();

        // To ensure that EditText Views hold valid inputs
        if (noteTitle.trim().isEmpty() || noteContent.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Sending information to the calling Activity through Intent extras
        Intent inputData = new Intent();
        inputData.putExtra(EXTRA_TITLE, noteTitle);
        inputData.putExtra(EXTRA_DESCRIPTION, noteContent);
        inputData.putExtra(EXTRA_PRIORITY, notePriority);

        int currentId = getIntent().getIntExtra(EXTRA_ID, -1);
        if(currentId != -1) {
            inputData.putExtra(EXTRA_ID, currentId);
        }

        setResult(RESULT_OK, inputData); // RESULT_OK signifies that we were able to successfully
        // retrieve input data
        finish(); // Close this Activity
    }

}