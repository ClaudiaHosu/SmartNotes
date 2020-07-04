package data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import entities.Note;

public class NoteRepository {

    private static NoteRepository noteRepository;

    private NoteDao noteDao;
    private LiveData<List<Note>> notes;

    private NoteRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        noteDao = appDatabase.noteDao();
        notes = noteDao.getNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public LiveData<Note> findNoteById(int noteId) {
          return noteDao.findNoteById(noteId);
    }

    public void saveNote(Note newNote) {
        if (newNote.getId() == 0) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                noteDao.saveNote(newNote);
            });
        } else {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                noteDao.updateNote(newNote);
            });
        }

    }

    public void deleteNote(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteDao.deleteNote(note);
        });
    }

    public static NoteRepository getInstance(Context context) {
        if (noteRepository == null) {
            noteRepository =  new NoteRepository(context);
        }

        return noteRepository;
    }


}
