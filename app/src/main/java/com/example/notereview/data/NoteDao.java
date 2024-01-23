package com.example.notereview.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.ForeignKey;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY id")
    public List<Note> getAllNotes();
    @Query("SELECT * FROM notes WHERE id = :id")
    public Note getNoteById(Long id);
    @Query("DELETE FROM notes")
    public void deleteNotes();
    @Insert
    public void insert(Note note);
    @Update
    public void update(Note note);
    @Delete
    public void delete(Note note);
}
