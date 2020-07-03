package data;

import java.util.List;
import java.util.Optional;

import models.Note;

public interface Data {

    public List<Note> getNotes();
    public Optional<Note> findNoteById(int id);
    public void saveNote(Note newNote);
    public void deleteNote(int id);

}
