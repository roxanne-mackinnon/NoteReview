package com.example.notereview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.notereview.data.Note;
import com.example.notereview.data.NoteDao;

import java.util.List;

import kotlin.Suppress;

public class NoteViewModel extends ViewModel {
    private NoteDao dao;

    private static Note selectedNote;

    public NoteViewModel(NoteDao dao) {
        this.dao = dao;
    }

    public List<Note> getAllNotes() {
        return dao.getAllNotes();
    }

    public void select(Note note) {
        selectedNote = note;
    }

    public Note getSelectedNote() {
        return this.getNote();
    }

    public synchronized void clearSelectedNote() {
        selectedNote = null;
    }

    public void addNote(String title, String content) {
        Note note = getNewNote(title, content);
        insertNote(note);
    }

    public void editNote(Long id, String title, String content) {
        Note note = getUpdatedNote(id, title, content);
        updateNote(note);
    }

    public void removeNote(Long id) {
        Note note = getUpdatedNote(id, null, null);
        deleteNote(note);
    }

    public void clearAllNotes() {
        deleteNotes();
    }

    public boolean isValidEntry(String title, String content) {
        if (title.isBlank() || content.isBlank()) {
            return false;
        }
        return true;
    }

    private Note getUpdatedNote(Long id, String title, String content) {
        Note result = new Note(id, title, content);
        return result;
    }

    private void insertNote(Note note) {
        Thread insertThread = new Thread() {
            public void run() {
                dao.insert(note);
            }
        };
        insertThread.start();
    }

    private void deleteNote(Note note) {
        Thread deleteThread = new Thread() {
            public void run() {
                dao.delete(note);
            }
        };
        deleteThread.start();
    }

    private void deleteNotes() {
        Thread deleteThread = new Thread() {
            public void run() {
                dao.deleteNotes();
            }
        };
        deleteThread.start();
    }

    private void updateNote(Note note) {
        Thread updateThread = new Thread() {
            public void run() {
                dao.update(note);
            }
        };
        updateThread.start();
    }

    private Note getNewNote(String title, String content) {
        Note note = new Note(title, content);
        return note;
    }

    private synchronized Note getNote() {
        return selectedNote;
    }



    public static class NoteViewModelFactory implements ViewModelProvider.Factory {
        private NoteDao dao;

        public NoteViewModelFactory(NoteDao dao) {
            this.dao = dao;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> className) {
            // make sure class can be instantiated from NoteViewModel
            if (className.isAssignableFrom(NoteViewModel.class)) {
                @SuppressWarnings("unchecked") T result = (T) new NoteViewModel(this.dao);
                return result;
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
