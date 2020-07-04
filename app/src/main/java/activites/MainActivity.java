package activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.smartnotes.R;

import adapters.RecyclerViewAdapter;
import data.NoteRepository;
import entities.Note;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private NoteRepository noteRepository;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started");
        noteRepository = NoteRepository.getInstance(this);
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: resume called");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: called");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.d(TAG, "onOptionsItemSelected: item selected "+item.getItemId());

        switch (item.getItemId()) {
            case R.id.addNote:
                addNote(item.getActionView());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNote(View view) {
        Intent intent = new Intent(this, NoteEditActivity.class);
        startActivity(intent);
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: called");

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter.setOnNoteDeleteListener(view -> {
            int noteId = (Integer) view.getTag();
            Note note = new Note();
            note.setId(noteId);
            noteRepository.deleteNote(note);
        });
        recyclerViewAdapter.setOnNoteEditListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteEditActivity.class);
            int noteId = (Integer) view.getTag();
            intent.putExtra(NoteEditActivity.EXTRA_KEY, noteId);
            startActivity(intent);
        });

        noteRepository.getNotes().observe(this, notes -> {
            recyclerViewAdapter.setNotes(notes);
        });

    }
    
}