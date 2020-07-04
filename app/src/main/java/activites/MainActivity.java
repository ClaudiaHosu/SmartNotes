package activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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

import java.util.List;

import adapters.RecyclerViewAdapter;
import data.AppDatabase;
import data.NoteDao;
import data.NoteRepository;
import models.Note;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private NoteRepository noteRepository;
    private RecyclerView recyclerView;
    //private boolean noteAdded;

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

        /*if (noteAdded) {
            noteRepository.getNotes().observe(this, notes -> {
                if (notes.size() == 0) return;
                ((RecyclerViewAdapter) recyclerView.getAdapter()).getNotes().add(notes.get(notes.size()-1));
                recyclerView.getAdapter().notifyDataSetChanged();
                noteAdded = false;
            });
        }*/

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
                Intent intent = new Intent(this, NoteEditActivity.class);
                startActivity(intent);
                //noteAdded = true;
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: called");

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter.setOnNoteDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int noteId = (Integer) view.getTag();
                Note note = new Note();
                note.setId(noteId);
                noteRepository.deleteNote(note);
            }
        });
        recyclerViewAdapter.setOnNoteEditListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteEditActivity.class);
                int noteId = (Integer) view.getTag();
                intent.putExtra(NoteEditActivity.EXTRA_KEY, noteId);
                startActivity(intent);
            }
        });

        noteRepository.getNotes().observe(this, notes -> {
            recyclerViewAdapter.setNotes(notes);
        });



    }
    
}