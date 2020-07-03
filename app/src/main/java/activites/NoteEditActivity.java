package activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.smartnotes.R;

import data.Data;
import data.InMemoryData;
import data.SharedPrefsData;
import models.Note;

public class NoteEditActivity extends AppCompatActivity {

    private static final String TAG = "NoteEditActivity";
    public static final String EXTRA_KEY = "note_id";

    private Data data;
    private Note note;

    private EditText titleText;
    private EditText contentText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        data = SharedPrefsData.getInstance(getSharedPreferences("activites", MODE_PRIVATE));
        initNote();
    }

    private void initNote() {

        titleText = (EditText) findViewById(R.id.noteTitleEditText);
        contentText = findViewById(R.id.noteContentEditText);

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            note = new Note();
            return;
        }

        int noteId = extras.getInt(EXTRA_KEY);
        note = data.findNoteById(noteId).get();

        if (note == null) {
            note = new Note();
            return;
        }

        titleText.setText(note.getTitle());
        contentText.setText(note.getContent());
    }

    @Override
    public void onBackPressed() {

        //save note - in-memory (for now)
        note.setTitle(titleText.getText().toString());
        note.setContent(contentText.getText().toString());
        data.saveNote(note);

        super.onBackPressed();

    }
}