package com.example.note.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.note.MainActivity;
import com.example.note.Model.Note;
import com.example.note.Model.NoteDatabase;
import com.example.note.R;
import java.util.HashMap;

public class AddEditNoteFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    View rootView;
    public boolean editMode;
    int noteId;
    private EditText title, note;
    ScrollView scrollView;
    private TextView updateTime;
    RadioGroup radioGroupBackground;
    NoteDatabase database;
    Note currentNote;

    public static AddEditNoteFragment CreateAddNoteFragment(){
        AddEditNoteFragment fragment = new AddEditNoteFragment();
        fragment.setEditMode(false);
        return fragment;
    }

    public static AddEditNoteFragment CreateEditNoteFragment(int noteId){
        AddEditNoteFragment fragment = new AddEditNoteFragment();
        fragment.setEditMode(true);
        fragment.setNoteId(noteId);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_edit_note, container, false);
        init();
        database = new NoteDatabase(getActivity());
        if (editMode){
            prepareAndShowData();
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (editMode && currentNote.isArchive())
            ((MainActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_detail_archive, menu);
        else
            ((MainActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (editMode && currentNote.isArchive()){
            switch (item.getItemId()){
                case R.id.menuDetailArchiveDelete:
                    deleteNote();
                    break;
                case R.id.menuDetailArchiveUnarchive:
                    database.saveArchive(noteId,false);
                    this.currentNote = database.getNote(noteId);
                    getActivity().invalidateOptionsMenu();
                    break;
                case R.id.menuDetailArchiveSave:
                    saveNote();
                    break;
            }
        }
        else if(editMode) {
            switch (item.getItemId()){
                case R.id.menuDetailDelete:
                    deleteNote();
                    break;
                case R.id.menuDetailArchive:
                    database.saveArchive(noteId,true);
                    this.currentNote = database.getNote(noteId);
                    getActivity().invalidateOptionsMenu();
                    break;
                case R.id.menuDetailSave:
                    saveNote();
                    break;
            }
        }
        else{
            switch (item.getItemId()){
                case R.id.menuDetailDelete:
                    Toast.makeText(getActivity(), "There is not active Note", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menuDetailArchive:
                    Toast.makeText(getActivity(), "There is not active Note", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menuDetailSave:
                    saveNote();
                    break;
            }
        }
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.radioButtonDefault:
                title.setBackgroundColor(Color.TRANSPARENT);
                scrollView.setBackgroundColor(Color.TRANSPARENT);
                break;
            case R.id.radioButtonBlue:
                title.setBackgroundResource(R.color.blue);
                scrollView.setBackgroundResource(R.color.blue);
                break;
            case R.id.radioButtonGreen:
                title.setBackgroundResource(R.color.green);
                scrollView.setBackgroundResource(R.color.green);
                break;
            case R.id.radioButtonGray:
                title.setBackgroundResource(R.color.gray);
                scrollView.setBackgroundResource(R.color.gray);
                break;
        }
    }

    private void init(){
        title = rootView.findViewById(R.id.addNoteEditTextTitle);
        note = rootView.findViewById(R.id.addNoteEditTextNote);
        updateTime = rootView.findViewById(R.id.addEditUpdateAt);
        radioGroupBackground = rootView.findViewById(R.id.backgroundRadioGroup);
        scrollView = rootView.findViewById(R.id.scrollView);
        radioGroupBackground.setOnCheckedChangeListener(this);
        setHasOptionsMenu(true);
    }

    public void prepareAndShowData(){
        currentNote = database.getNote(noteId);
        if (currentNote != null){
            title.setText(currentNote.getTitle());
            note.setText(currentNote.getNote());
            prepareBgColor(currentNote.getBgColor());
            updateTime.setText("Last Updated : "+currentNote.getUpdateAt());
        }

    }

    public void prepareBgColor(String color){
        RadioButton radioButton;
        switch (color){
            case "default":
                title.setBackgroundColor(Color.TRANSPARENT);
                scrollView.setBackgroundColor(Color.TRANSPARENT);
                radioButton = rootView.findViewById(R.id.radioButtonDefault);
                break;
            case "blue":
                title.setBackgroundResource(R.color.blue);
                scrollView.setBackgroundResource(R.color.blue);
                radioButton = rootView.findViewById(R.id.radioButtonBlue);
                break;
            case "green":
                title.setBackgroundResource(R.color.green);
                scrollView.setBackgroundResource(R.color.green);
                radioButton = rootView.findViewById(R.id.radioButtonGreen);
                break;
            case "gray":
                title.setBackgroundResource(R.color.gray);
                scrollView.setBackgroundResource(R.color.gray);
                radioButton = rootView.findViewById(R.id.radioButtonGray);
                break;
            default:
                title.setBackgroundColor(Color.TRANSPARENT);
                scrollView.setBackgroundColor(Color.TRANSPARENT);
                radioButton = rootView.findViewById(R.id.radioButtonDefault);
                break;
        }
        radioButton.setChecked(true);
    }

    private HashMap<String,String> getScreenData(){
        HashMap<String,String> map = new HashMap<>();
        map.put("title",title.getText().toString());
        map.put("note",note.getText().toString());
        String bgColor = "default";
        switch (radioGroupBackground.getCheckedRadioButtonId()){
            case R.id.radioButtonDefault:
                bgColor = "default";
                break;
            case R.id.radioButtonBlue:
                bgColor = "blue";
                break;
            case R.id.radioButtonGreen:
                bgColor = "green";
                break;
            case R.id.radioButtonGray:
                bgColor = "gray";
                break;
        }
        map.put("bgColor",bgColor);
        return map;

    }

    private void saveNote(){
        HashMap<String,String> map = getScreenData();
        boolean success;
        if (editMode) {
            success = database.editNote(this.noteId, map.get("title"), map.get("note"), map.get("bgColor"));
        } else {
            success = database.addNote(map.get("title"), map.get("note"), map.get("bgColor"));
        }

        if (success && editMode){
            Toast.makeText(getActivity(), "Note Updated", Toast.LENGTH_LONG).show();
            prepareAndShowData();
        }
        else if (!success && editMode){
            Toast.makeText(getActivity(), "Note did not Update", Toast.LENGTH_LONG).show();
        }
        else if (success && !editMode){
            this.editMode = true;
            this.noteId = database.getLastSaveNoteId();
            this.currentNote = database.getNote(noteId);
            Toast.makeText(getActivity(), "New Note is Saved", Toast.LENGTH_LONG).show();
            prepareAndShowData();
        }
        else{
            Toast.makeText(getActivity(), "Note did not Save", Toast.LENGTH_LONG).show();
        }

    }

    private void deleteNote(){
        if (editMode) {
            database.deleteNote(noteId);
            Toast.makeText(getActivity(), "Note Deleted", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getActivity(), "There is not active Note", Toast.LENGTH_LONG).show();
        }
    }

    protected void setNoteId(int noteId){
        this.noteId = noteId;
    }
    protected void setEditMode(boolean editMode){
        this.editMode = editMode;
    }
}
