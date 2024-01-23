package com.example.notereview;

import android.app.Application;

import com.example.notereview.data.NoteDao;
import com.example.notereview.data.NoteDatabase;

public class MyApp extends Application {
    private NoteDatabase database = null;
    public NoteDatabase getDatabase() {
        if (database != null) return database;
        database = NoteDatabase.getDatabase(this);
        return database;
    }
}
