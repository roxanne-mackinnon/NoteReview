package com.example.notereview.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey
    public Long id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "content")
    public String content;

    public Note() {}
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public Note(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
