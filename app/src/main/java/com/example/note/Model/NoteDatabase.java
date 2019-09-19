package com.example.note.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {

    Context context;
    private static String DbName = "Note";
    private static int DbVersiyon = 1;

    private static String TableName = "notes";
    private static String ColumnId = "id";
    private static String ColumnTitle = "title";
    private static String ColumnNote = "note";
    private static String ColumnColor = "color";
    private static String ColumnArchive = "archive";

    public NoteDatabase(@Nullable Context context) {
        super(context, DbName, null, DbVersiyon);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE "+TableName+" ("+ColumnId+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ColumnTitle+" TEXT,"+
                ColumnNote+" TEXT,"+
                ColumnColor+" TEXT,"+
                ColumnArchive+" BOOLEAN DEFAULT 0,"+
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
        sqLiteDatabase.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(sqLiteDatabase);

    }

    public boolean addNote(String title,String note, String bgColor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(ColumnTitle,title);
        data.put(ColumnNote,note);
        data.put(ColumnColor,bgColor);
        try {
            db.insert(TableName,null,data);
            return true;
        } catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean editNote(int id,String title,String note, String bgColor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(ColumnTitle,title);
        data.put(ColumnNote,note);
        data.put(ColumnColor,bgColor);
        //data.put("updated_at",System.currentTimeMillis());
        try {
            db.update(TableName,data,"id=?", new String[]{""+id});
            return true;
        } catch (Exception e){
            Toast.makeText(context, "edit problem "+e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }



    public Integer getLastSaveNoteId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public List<Note> getAllNotes(boolean archiveStatus){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] allColums = {ColumnId,ColumnTitle,ColumnNote,ColumnColor};
        List<Note> noteList = new ArrayList<>();

        try {
            Cursor cursor = db.query(TableName,allColums,ColumnArchive+" = ?",new String[]{archiveStatus ? "1":"0"},null,null,"updated_at DESC");
            if (cursor == null)
                return null;
            while (cursor.moveToNext()){
                Note tempNote = new Note();
                tempNote.setId(cursor.getInt(0));
                tempNote.setTitle(cursor.getString(1));
                tempNote.setNote(cursor.getString(2));
                tempNote.setBgColor(cursor.getString(3));
                tempNote.setArchive(false);
                noteList.add(tempNote);
            }
        } catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return noteList;
    }

    public Note getNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] allColums = {ColumnId,ColumnTitle,ColumnNote,ColumnColor, ColumnArchive,"updated_at"};
        Note note = new Note();
        try {
            Cursor cursor = db.query(TableName,allColums,ColumnId+" = ?",new String[]{""+id},null,null,null);
            cursor.moveToFirst();
            note.setId(id);
            note.setTitle(cursor.getString(1));
            note.setNote(cursor.getString(2));
            note.setBgColor(cursor.getString(3));
            note.setArchive((cursor.getString(4)).equals("1"));

            /*Timestamp ts=new Timestamp(cursor.getInt(5));
            Date date= new Date(cursor.getString(5));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");

            note.setUpdateAt(dateFormat.format(date));*/

        } catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return note;
    }

    public void deleteNote(int id){
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(TableName,"id = ?",new String[]{""+id});
    }
}
