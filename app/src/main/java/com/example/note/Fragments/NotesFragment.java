package com.example.note.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.note.MainActivity;
import com.example.note.Model.Note;
import com.example.note.Model.NoteDatabase;
import com.example.note.R;
import com.example.note.RecylerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class NotesFragment extends Fragment implements RecylerAdapter.OnNoteListener,View.OnClickListener {
    View rootView;
    List<Note> noteList;
    NoteDatabase database;
    RecyclerView recyclerView;
    SearchView search;
    FloatingActionButton btnAdd;
    RecylerAdapter recylerAdapter;
    boolean isArchiveFragment;

    public static NotesFragment CreateArchiveList(){
        NotesFragment fragment = new NotesFragment();
        fragment.isArchiveFragment = true;
        return fragment;
    }

    public static NotesFragment CreateNotesList(){
        NotesFragment fragment = new NotesFragment();
        fragment.isArchiveFragment = false;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        init();
        prepareNoteList();
        return rootView;
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
                saveViewMode("list");
                break;
            case R.id.menu_main_grid:
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                saveViewMode("grid");
                break;
        }
        return true;
    }

    @Override
    public void onNoteClick(int position) {
        Integer id = noteList.get(position).getId();
        ((MainActivity)getActivity()).loadFragment(AddEditNoteFragment.CreateEditNoteFragment(id));
    }

    @Override
    public void onClick(View view) {
        ((MainActivity)getActivity()).loadFragment(AddEditNoteFragment.CreateAddNoteFragment());
    }

    private void init(){
        recyclerView = rootView.findViewById(R.id.notesRecycler);
        btnAdd = rootView.findViewById(R.id.btnAddNote);
        btnAdd.setOnClickListener(this);
        database = new NoteDatabase(getActivity());
        setHasOptionsMenu(true);
    }

    private void prepareNoteList(){
        noteList =  isArchiveFragment ? database.getAllNotes(true): database.getAllNotes(false);
        recylerAdapter = new RecylerAdapter(noteList,this);
        recyclerView.setAdapter(recylerAdapter);
        if (getViewMode() == "list" || getViewMode() == "")
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

    }

    private void saveViewMode(String mode){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("viewMode", MODE_PRIVATE).edit();
        editor.putString("mode",mode);
        editor.commit();
    }

    private String getViewMode(){
        SharedPreferences preferences = getActivity().getSharedPreferences("viewMode", MODE_PRIVATE);
        return preferences.getString("mode", "");
    }


}
