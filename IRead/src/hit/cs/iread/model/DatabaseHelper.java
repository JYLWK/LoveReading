package hit.cs.iread.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE = "iread.db";
	private static final int VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE, null, VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + "book" + "("
				+ "iSBN INTEGER PRIMARY KEY," + "title VARCHAR(50),"
				+ "author VARCHAR(50)," + "tag VARCHAR(50),"
				+ "publisher VARCHAR(50)," + "pubDate VARCHAR(50),"
				+ "pages INTEGER," + "rating FLOAT," + "summary TEXT,"
				+ "bitmap BLOB)";
		db.execSQL(sql);
		sql = "CREATE TABLE " + "notebook" + "(" + "nid INTEGER PRIMARY KEY,"
				+ "category VARCHAR(50)," + "iSBN INTEGER)";
		db.execSQL(sql);
		sql = "CREATE TABLE " + "note" + "(" + "nid INTEGER," + "id INTEGER PRIMARY KEY,"
				+ "date DATE," + "refer INTEGER," + "content TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE " + "bookmark" + "(" + "iSBN INTEGER,"
				+ "mid INTEGER PRIMARY KEY," + "time DATE," + "refer INTEGER)";
		db.execSQL(sql);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + "book";
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS " + "notebook";
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS " + "note";
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS " + "bookmark";
		db.execSQL(sql);
		onCreate(db);
	}

}
