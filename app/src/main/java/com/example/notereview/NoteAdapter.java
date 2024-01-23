package com.example.notereview;

import android.icu.util.ValueIterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notereview.data.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> notes;
    private View.OnClickListener clickListener;
    private OnClickListener listener;
    private OnClickListener deleteListener;

    public NoteAdapter() {
        this.notes = new ArrayList<Note>();
    }
    public NoteAdapter(List<Note> notes) {
        this.notes = notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        this.notifyDataSetChanged();
    }

    public void clearNotes() {
        notes.clear();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }
    public void setOnClickDeleteListener(OnClickListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.note_card_item,
                parent,
                false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.bind(note);
        holder.setOnClickDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this could be bad, might cause a hangup, lets see i guess.
                int holderPosition = holder.getAdapterPosition();
                Note holderNote = holder.getNote();
                // ensure adapter position is valid
                if (deleteListener != null) {
                    deleteListener.onClick(holderPosition, holderNote);
                }
            }
        });

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int holderPosition = holder.getAdapterPosition();
                Note holderNote = holder.getNote();
                if (listener != null) {
                    listener.onClick(holderPosition, holderNote);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (notes == null) return 0;
        return notes.size();
    }

    // only removes note from my own dataset, not from the underlying database
    public void removeNote(int position) {
        if (position >= notes.size()) return;
        notes.remove(position);

    }

    public interface OnClickListener {
        void onClick(int position, Note item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView content;
        private Note note;
        private Button btnDelete;
        private View view;

        public ViewHolder(View v) {
            super(v);
            this.title = v.findViewById(R.id.tvTitle);
            this.content = v.findViewById(R.id.tvContent);
            this.btnDelete = v.findViewById(R.id.btnDeleteNote);
            this.view = v;

        }

        public void setOnClickListener(View.OnClickListener listener) {
            view.setOnClickListener(listener);
        }

        public void setOnClickDeleteListener(View.OnClickListener listener) {
            btnDelete.setOnClickListener(listener);
        }

        public Note getNote() {
            return note;
        }

        public void bind(Note note) {
            this.note = note;
            this.title.setText(note.title);
            this.content.setText(note.content);
        }
    }
}
