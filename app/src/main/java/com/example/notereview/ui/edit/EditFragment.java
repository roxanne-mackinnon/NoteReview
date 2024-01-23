package com.example.notereview.ui.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.notereview.MyApp;
import com.example.notereview.NoteViewModel;
import com.example.notereview.R;
import com.example.notereview.data.Note;
import com.example.notereview.databinding.FragmentEditBinding;

public class EditFragment extends Fragment {

    private FragmentEditBinding binding;
    private NoteViewModel viewModel;
    private ViewGroup container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditBinding.inflate(inflater, container, false);
        viewModel = new NoteViewModel.NoteViewModelFactory(
                ((MyApp)getActivity().getApplication()).getDatabase().getNoteDao()
        ).create(NoteViewModel.class);
        this.container = container;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Note selected = viewModel.getSelectedNote();
        if (selected == null) {
            Toast.makeText(getContext(), "New fragment selected note is null", Toast.LENGTH_LONG).show();
        }
        String title = selected.title;
        String content = selected.content;

        binding.etTitle.setText(title);
        binding.etContent.setText(content);

        binding.btnEditDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteNote();
            }
        });
        binding.btnEditUpdateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String newTitle = binding.etTitle.getText().toString();
                String newContent = binding.etContent.getText().toString();
                Note note = viewModel.getSelectedNote();
                viewModel.editNote(note.id, newTitle, newContent);
                switchToHomeFragment();
            }
        });
    }

    private void confirmDeleteNote() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm delete note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Note note = viewModel.getSelectedNote();
                        viewModel.removeNote(note.id);
                        switchToHomeFragment();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void switchToHomeFragment() {
        viewModel.clearSelectedNote();
        binding.etContent.getText().clear();
        binding.etTitle.getText().clear();
        Navigation.findNavController(container).navigate(R.id.navigation_home);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
