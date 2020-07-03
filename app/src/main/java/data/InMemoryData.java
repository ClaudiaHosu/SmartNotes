package data;

import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import models.Note;

public class InMemoryData implements Data {

    private static final String TAG = "InMemoryData";

    private static Data data;

    private static List<Note> notes = new ArrayList<>();
    private int idIncrement = 0;

    private InMemoryData() {
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

        public List<Note> getNotes() {
            return notes;
        }

        public Optional<Note> findNoteById(int id) {
            return notes.stream().filter( note -> note.getId() == id).findFirst();
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

        public void deleteNote(int id) {
            notes.removeIf( note -> note.getId() == id);
        }



        public static Data getInstance() {
            if (data == null) {
                data = new InMemoryData();
            }

            return data;
        }

}
