package data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entities.Note;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    public LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM note WHERE id IN (:noteId)")
    public LiveData<Note> findNoteById(int noteId);

    @Insert
    public void saveNote(Note newNote);

    @Update
    public void updateNote(Note updatedNote);

    @Delete
    public void deleteNote(Note note);

}
