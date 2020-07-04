package activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.smartnotes.R;

import data.AppDatabase;
import data.NoteDao;
import data.NoteRepository;
import models.Note;

public class NoteEditActivity extends AppCompatActivity {

    private static final String TAG = "NoteEditActivity";
    public static final String EXTRA_KEY = "note_id";

    private NoteRepository noteRepository;
    private Note note;

    private EditText titleText;
    private EditText contentText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        //noteDao = SharedPrefsNoteDao.getInstance(getSharedPreferences("activites", MODE_PRIVATE));
        noteRepository = NoteRepository.getInstance(this);
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
        noteRepository.findNoteById(noteId).observe(this, foundNote -> {
            if (foundNote == null) {
                note = new Note();
                return;
            }
            note = foundNote;

            titleText.setText(note.getTitle());
            contentText.setText(note.getContent());
        });
    }

    @Override
    public void onBackPressed() {

        note.setTitle(titleText.getText().toString());
        note.setContent(contentText.getText().toString());
        noteRepository.saveNote(note);
        super.onBackPressed();

    }
}