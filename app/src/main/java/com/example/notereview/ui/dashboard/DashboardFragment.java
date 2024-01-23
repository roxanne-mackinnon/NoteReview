package com.example.notereview.ui.dashboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.notereview.NoteViewModel;
import com.example.notereview.R;
import com.example.notereview.databinding.FragmentDashboardBinding;
import com.example.notereview.MyApp;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private NoteViewModel viewModel;
    private ViewGroup container;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        viewModel = new NoteViewModel.NoteViewModelFactory(
                ((MyApp)requireActivity().getApplication()).getDatabase().getNoteDao()
        ).create(NoteViewModel.class);
        this.container = container;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.etTitle.getText().toString();
                String content = binding.etContent.getText().toString();
                if (viewModel.isValidEntry(title, content)) {
                    viewModel.addNote(title, content);
                    switchFragments();
                }
                else {
                    Toast.makeText(getContext(), "Title or body of note cannot be empty.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // this may not work, as onkeylistener is supposedly only for 'hardware keyboards'
        binding.etTitle.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == '\n') {
                    binding.etContent.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    private void switchFragments() {
        binding.etTitle.getText().clear();
        binding.etContent.getText().clear();
        Navigation.findNavController(container).navigate(R.id.navigation_home);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}