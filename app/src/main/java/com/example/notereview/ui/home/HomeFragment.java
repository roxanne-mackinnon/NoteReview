package com.example.notereview.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notereview.MyApp;
import com.example.notereview.NoteAdapter;
import com.example.notereview.NoteViewModel;
import com.example.notereview.R;
import com.example.notereview.data.Note;
import com.example.notereview.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NoteViewModel viewModel;
    private ViewGroup container;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new NoteViewModel.NoteViewModelFactory(
                ((MyApp)requireActivity().getApplication()).getDatabase().getNoteDao()
        ).create(NoteViewModel.class);
        this.container = container;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NoteAdapter adapter = new NoteAdapter();
        // set onClick for 'clearNotesButton' - call method for viewmodel and notify adapter
        binding.clearNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.clearAllNotes();
                adapter.clearNotes();
                adapter.notifyDataSetChanged();
            }
        });

        adapter.setOnClickListener(new NoteAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Note item) {
                viewModel.select(item);
                if (viewModel.getSelectedNote() == null) {
                    Toast.makeText(getContext(), "Selected is null", Toast.LENGTH_LONG).show();
                }
                Navigation.findNavController(container).navigate(R.id.navigation_edit);
            }
        });
        // set onClick method for the delete button method each note should use
        adapter.setOnClickDeleteListener(new NoteAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Note item) {
                // remove note from database
                viewModel.removeNote(item.id);
                // remove note from adapter dataset in memory
                adapter.removeNote(position);
                // remove note from view
                adapter.notifyItemRemoved(position);
            }
        });

        // retrieve note dataset from DB in another thread
        Thread retrieveNotes = new Thread () {
            public void run() {
                List<Note> notes = viewModel.getAllNotes();
                adapter.setNotes(notes);
            }
        };
        retrieveNotes.start();

        binding.notesRecyclerView.setAdapter(adapter);
        binding.notesRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}