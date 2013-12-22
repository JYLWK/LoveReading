package hit.cs.iread.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Notebook implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SQLiteOpenHelper helper;
	private int nid;
	private String category;
	private Book book;
	private ArrayList<Note> notes;

	public Notebook() {
		notes = new ArrayList<Note>();
		category = "Œ¥∑÷¿‡";
	}

	public Notebook(int nid) {
		this.nid = nid;
		notes = new ArrayList<Note>();
	}

	public void init(SQLiteOpenHelper helper) {
		this.helper = helper;
		Note note = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		String sql = "SELECT id,date,refer,content FROM " + "note"
				+ " WHERE nid=? ORDER BY date,id";
		String selectionArgs[] = new String[] { String.valueOf(nid) };
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor result = db.rawQuery(sql, selectionArgs);
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			try {
				note = new Note(result.getInt(0));
				note.setDate(sdf.parse(result.getString(1)));
				note.setRefer(result.getInt(2));
				note.setContent(result.getString(3));
				notes.add(note);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}

	public boolean isInit() {
		return helper != null;
	}

	public void add(Note note) {
		int i;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		ContentValues cv = new ContentValues();
		cv.put("nid", nid);
		cv.put("date", sdf.format(note.getDate()));
		cv.put("refer", note.getRefer());
		cv.put("content", note.getContent());
		SQLiteDatabase db = helper.getWritableDatabase();
		db.insert("note", null, cv);
		Cursor result = db.rawQuery(
				"SELECT last_insert_rowid() FROM " + "note", null);
		result.moveToFirst();
		note.setId(result.getInt(0));
		db.close();
		for (i = notes.size() - 1; i >= 0; i--)
			if (note.getDate().compareTo(notes.get(i).getDate()) >= 0)
				break;
		notes.add(i + 1, note);
	}

	public void update(Note note) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		ContentValues cv = new ContentValues();
		cv.put("date", sdf.format(note.getDate()));
		cv.put("refer", note.getRefer());
		cv.put("content", note.getContent());
		String whereClause = "nid=? AND id=?";
		String whereArgs[] = new String[] { String.valueOf(nid),
				String.valueOf(note.getId()) };
		SQLiteDatabase db = helper.getWritableDatabase();
		db.update("note", cv, whereClause, whereArgs);
		db.close();
	}

	public void remove(Note note) {
		String whereClause = "nid=? AND id=?";
		String whereArgs[] = new String[] { String.valueOf(nid),
				String.valueOf(note.getId()) };
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("note", whereClause, whereArgs);
		db.close();
		notes.remove(note);
	}

	public void clear() {
		String whereClause = "nid=?";
		String whereArgs[] = new String[] { String.valueOf(nid) };
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("note", whereClause, whereArgs);
		db.close();
		notes.clear();
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public ArrayList<Note> getNotes() {
		return notes;
	}

}
