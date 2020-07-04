package data;

import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import entities.Note;

public class InMemoryNoteDao implements NoteDao {

    private static final String TAG = "InMemoryData";

    private static NoteDao noteDao;

    private static List<Note> notes = new ArrayList<>();
    private int idIncrement = 0;

    private InMemoryNoteDao() {
        initData();
    }


    private void initData() {

        Log.d(TAG, "initData: started");

        Note note1 = new Note(++idIncrement, "Life is beautiful", "dweh wgfew fiwuf wifhwfer");
        notes.add(note1);

        Note note2 = new Note(++idIncrement, "Love my life", "dweh wgfew fiwuf wifhwfer");
        notes.add(note2);

        Note note3 = new Note(++idIncrement, "Another day to be alive", "dweh wgfew fiwuf wifhwfer");
        notes.add(note3);

        Note note4 = new Note(++idIncrement, "This is great", "dweh wgfew fiwuf wifhwfer");
        notes.add(note4);
    }

        public LiveData<List<Note>> getNotes() {
            MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>();
            notesLiveData.setValue(notes);
            return notesLiveData;
        }

        public LiveData<Note> findNoteById(int id) {
            Note foundNote = notes.stream().filter( note -> note.getId() == id).findFirst().orElse(null);
            MutableLiveData<Note> noteLiveData = new MutableLiveData<>();
            noteLiveData.setValue(foundNote);
            return noteLiveData;
        }

        public void saveNote(Note newNote) {

            Note existingNote = notes.stream()
                    .filter( note -> note.getId() == newNote.getId())
                    .findFirst()
                    .orElse(null);

            if (existingNote != null) return;

            notes.add(newNote);
            newNote.setId(++idIncrement);

        }

    @Override
    public void updateNote(Note updatedNote) {
        saveNote(updatedNote);
    }

    public void deleteNote(Note noteToDelete) {
            notes.removeIf( note -> note.getId() == noteToDelete.getId());
        }



        public static NoteDao getInstance() {
            if (noteDao == null) {
                noteDao = new InMemoryNoteDao();
            }

            return noteDao;
        }

}
