package data;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import entities.Note;

public class SharedPrefsNoteDao implements NoteDao {

    private static final String TAG = "SharedPrefsData";
    private static final String SHARED_PREFS_NOTE_COUNT_KEY = "notes_no";
    private static final String SHARED_PREFS_NOTE_ID_PREFIX_KEY = "note";

    private static NoteDao noteDao;

    private SharedPreferences sharedPreferences;
    private static Gson gson = new GsonBuilder().create();
    private int idIncrement = 0;

    private SharedPrefsNoteDao(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public LiveData<List<Note>> getNotes() {

        Log.d(TAG, "getNotes: called");

        List<Note> notes = new ArrayList<>();

        int notesNo = sharedPreferences.getInt(SHARED_PREFS_NOTE_COUNT_KEY, 0);

        Log.d(TAG, "getNotes: NotesNo "+notesNo);

        if (notesNo == 0) return new MutableLiveData<>();

        int notesFound = 0;
        int id = 0;
        while (notesFound < notesNo) {
            Note note = gson.fromJson(sharedPreferences.getString(SHARED_PREFS_NOTE_ID_PREFIX_KEY+(++id), ""), Note.class);
            if (note != null) {
                notes.add(note);
                notesFound++;
            }
        }

        idIncrement = notes.get(notes.size()-1).getId();

        Log.d(TAG, "getNotes: Found number of notes: " + notes.size());

        MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>();
        notesLiveData.setValue(notes);

        return notesLiveData;
    }

    @Override
    public LiveData<Note> findNoteById(int id) {
        Note note = gson.fromJson(sharedPreferences.getString(SHARED_PREFS_NOTE_ID_PREFIX_KEY+id, ""), Note.class);
        MutableLiveData<Note> noteLiveData = new MutableLiveData<>();
        noteLiveData.setValue(note);
        return noteLiveData;
    }

    @Override
    public void saveNote(Note newNote) {

        boolean isInsert = false;
        int noteId = newNote.getId();

        if (noteId == 0) {
            noteId = ++idIncrement;
            isInsert = true;
            Log.d(TAG, "saveNote: Note insert detected!");
        }

        newNote.setId(noteId);

        Log.d(TAG, "saveNote: "+newNote.getId() + " " +newNote.getTitle() + " "+ newNote.getContent());

        sharedPreferences.edit().putString(SHARED_PREFS_NOTE_ID_PREFIX_KEY+noteId, gson.toJson(newNote)).apply();
        Note note = gson.fromJson(sharedPreferences.getString(SHARED_PREFS_NOTE_ID_PREFIX_KEY+noteId, ""), Note.class);
        if (note == null) {
            //save wasn't successful
            return;
        }

        if (isInsert) {
            int notesNo = sharedPreferences.getInt(SHARED_PREFS_NOTE_COUNT_KEY, 0);
            sharedPreferences.edit().putInt(SHARED_PREFS_NOTE_COUNT_KEY, ++notesNo).apply();
        }
    }

    @Override
    public void updateNote(Note updatedNote) {
        saveNote(updatedNote);
    }

    @Override
    public void deleteNote(Note noteToDelete) {
        Log.d(TAG, "deleteNote: "+noteToDelete.getId());
        sharedPreferences.edit().remove(SHARED_PREFS_NOTE_ID_PREFIX_KEY+noteToDelete.getId()).apply();
        int notesNo = sharedPreferences.getInt(SHARED_PREFS_NOTE_COUNT_KEY, 0);
        sharedPreferences.edit().putInt(SHARED_PREFS_NOTE_COUNT_KEY, --notesNo).apply();
    }

    public static NoteDao getInstance(SharedPreferences sharedPreferences) {
        if (noteDao == null) {
            noteDao = new SharedPrefsNoteDao(sharedPreferences);
        }

        return noteDao;
    }

}
