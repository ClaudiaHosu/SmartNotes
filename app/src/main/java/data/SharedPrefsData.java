package data;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import models.Note;

public class SharedPrefsData implements Data {

    private static final String TAG = "SharedPrefsData";
    private static final String SHARED_PREFS_NOTE_COUNT_KEY = "notes_no";
    private static final String SHARED_PREFS_NOTE_ID_PREFIX_KEY = "note";

    private static Data data;

    private SharedPreferences sharedPreferences;
    private static Gson gson = new GsonBuilder().create();
    private int idIncrement = 0;

    private SharedPrefsData(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();

        int notesNo = sharedPreferences.getInt(SHARED_PREFS_NOTE_COUNT_KEY, 0);

        if (notesNo == 0) return notes;

        idIncrement = notesNo;
        for (int i = 1; i <= notesNo; i++) {
            Note note = gson.fromJson(sharedPreferences.getString(SHARED_PREFS_NOTE_ID_PREFIX_KEY+i, ""), Note.class);
            if (note != null) {
                notes.add(note);
            }
        }

        return notes;
    }

    @Override
    public Optional<Note> findNoteById(int id) {
        Note note = gson.fromJson(sharedPreferences.getString(SHARED_PREFS_NOTE_ID_PREFIX_KEY+id, ""), Note.class);
        return Optional.of(note);
    }

    @Override
    public void saveNote(Note newNote) {

        int noteId = newNote.getId();

        if (noteId == 0) {
            noteId = ++idIncrement;
        }
        newNote.setId(noteId);

        sharedPreferences.edit().putString(SHARED_PREFS_NOTE_ID_PREFIX_KEY+noteId, gson.toJson(newNote)).apply();
        Note note = gson.fromJson(sharedPreferences.getString(SHARED_PREFS_NOTE_ID_PREFIX_KEY+noteId, ""), Note.class);
        if (note == null) {
            //save wasn't successful
            return;
        }

        int notesNo = sharedPreferences.getInt(SHARED_PREFS_NOTE_COUNT_KEY, 0);
        sharedPreferences.edit().putInt(SHARED_PREFS_NOTE_COUNT_KEY, ++notesNo).apply();
    }

    @Override
    public void deleteNote(int id) {
        sharedPreferences.edit().remove(SHARED_PREFS_NOTE_ID_PREFIX_KEY+id).apply();
        int notesNo = sharedPreferences.getInt(SHARED_PREFS_NOTE_COUNT_KEY, 0);
        sharedPreferences.edit().putInt(SHARED_PREFS_NOTE_COUNT_KEY, --notesNo).apply();
    }

    public static Data getInstance(SharedPreferences sharedPreferences) {
        if (data == null) {
            data = new SharedPrefsData(sharedPreferences);
        }

        return data;
    }

}
