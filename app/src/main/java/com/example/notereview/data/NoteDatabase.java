package com.example.notereview.data;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDao();

    private static NoteDatabase database = null;
    public static NoteDatabase getDatabase(Context context) {
        if (database != null) return database;
        synchronized (NoteDatabase.class) {
            database = Room.databaseBuilder(
                    context,
                    NoteDatabase.class,
                    "notes"
                    ).build();
        }
        return database;
    }
}
