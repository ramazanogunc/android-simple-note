package com.example.note;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.Model.Note;

import java.util.List;

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.MyViewHolder> {
    List<Note> noteList;
    OnNoteListener onNoteListener;
    public RecylerAdapter(List<Note> noteList, OnNoteListener onNoteListener) {
        this.noteList = noteList;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_list_item, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view ,onNoteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note noteRow = noteList.get(position);

        LinearLayout layout = holder.linearLayout;
        TextView title = holder.title;
        TextView note = holder.note;

        switch (noteRow.getBgColor()){
            case "default":
                layout.setBackgroundColor(Color.TRANSPARENT);
                break;
            case "blue":
                layout.setBackgroundResource(R.color.blue);
                break;
            case "green":
                layout.setBackgroundResource(R.color.green);
                break;
            case "gray":
                layout.setBackgroundResource(R.color.gray);
                break;
        }
        
        title.setText(noteRow.getTitle());
        note.setText(noteRow.getNote());

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout linearLayout;
        public TextView title,note;
        OnNoteListener onNoteListener;
        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.listItem);
            title = itemView.findViewById(R.id.listItemTitle);
            note = itemView.findViewById(R.id.listItemNotes);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
