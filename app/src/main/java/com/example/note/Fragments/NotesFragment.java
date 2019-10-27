package com.example.note.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import com.example.note.Activities.MainActivity;
import com.example.note.Model.Note;
import com.example.note.Model.NoteDatabase;
import com.example.note.Model.Settings;
import com.example.note.R;
import com.example.note.Model.RecylerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;


public class NotesFragment extends Fragment implements RecylerAdapter.OnNoteListener,View.OnClickListener {
    View rootView;
    List<Note> noteList;
    NoteDatabase database;
    RecyclerView recyclerView;
    SearchView search;
    FloatingActionButton btnAdd;
    RecylerAdapter recylerAdapter;
    boolean isArchiveFragment;
    Settings settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        init();
        prepareNoteList();
        prepareLayoutManager();
    }

    private void init(){
        recyclerView = rootView.findViewById(R.id.notesRecycler);
        btnAdd = rootView.findViewById(R.id.btnAddNote);
        database = new NoteDatabase(getActivity());
        settings = new Settings(getActivity());
        setHasOptionsMenu(true);
        decideNormalOrArchive();
    }

    private void decideNormalOrArchive(){
        if (getArguments() != null){
            this.isArchiveFragment = true;
            btnAdd.hide();
        }
        else{
            this.isArchiveFragment = false;
            btnAdd.setOnClickListener(this);
        }
    }

    private void prepareNoteList(){
        noteList =  isArchiveFragment ? database.getAllNotes(true): database.getAllNotes(false);
        recylerAdapter = new RecylerAdapter(noteList,this);
        recyclerView.setAdapter(recylerAdapter);
    }

    private void prepareLayoutManager(){
        if (settings.getViewMode().equals("list"))
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        ((MainActivity)getActivity()).getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem mSearch = menu.findItem(R.id.menu_main_search);
        search = (SearchView) mSearch.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Note> searchList = new ArrayList<>();
                for (int i = 0; i < noteList.size(); i++){
                    if (noteList.get(i).getTitle().toLowerCase().contains(s.toLowerCase())
                            ||
                            noteList.get(i).getNote().toLowerCase().contains(s.toLowerCase()) )
                        searchList.add(noteList.get(i));

                }
                recylerAdapter.updateData(searchList);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main_list:
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                settings.setViewMode("list");
                break;
            case R.id.menu_main_grid:
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                settings.setViewMode("grid");
                break;
        }
        return true;
    }

    @Override
    public void onNoteClick(int position) {
        Integer id = noteList.get(position).getId();
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        Navigation.findNavController(rootView)
                .navigate(R.id.action_notesFragment_to_addEditNoteFragment,bundle);
    }

    @Override
    public void onClick(View view) {
        Navigation.findNavController(rootView)
                .navigate(R.id.action_notesFragment_to_addEditNoteFragment);
    }

}
