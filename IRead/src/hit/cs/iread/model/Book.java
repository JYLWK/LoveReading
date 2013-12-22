package hit.cs.iread.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class Book {
	private SQLiteOpenHelper helper;
	private String iSBN;
	private String title;
	private String author;
	private String tag;
	private String publisher;
	private String pubDate;
	private String pages;
	private String rating;
	private String summary;
	private Bitmap bitmap;
	private ArrayList<Bookmark> bookmarks;

	public Book() {
		bookmarks = new ArrayList<Bookmark>();
	}

	public Book(String iSBN) {
		this.iSBN = iSBN;
		bookmarks = new ArrayList<Bookmark>();
	}

	public void init(SQLiteOpenHelper helper) {
		this.helper = helper;
		Bookmark bookmark = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		String sql = "SELECT mid,time,refer FROM " + "bookmark"
				+ " WHERE iSBN=?";
		String selectionArgs[] = new String[] { iSBN };
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor result = db.rawQuery(sql, selectionArgs);
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			try {
				bookmark = new Bookmark(result.getInt(0));
				bookmark.setTime(sdf.parse(result.getString(1)));
				bookmark.setRefer(result.getInt(2));
				bookmarks.add(bookmark);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}

	public boolean isInit() {
		return helper != null;
	}

	public void add(Bookmark bookmark) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		ContentValues cv = new ContentValues();
		cv.put("iSBN", iSBN);
		cv.put("time", sdf.format(bookmark.getTime()));
		cv.put("refer", bookmark.getRefer());
		SQLiteDatabase db = helper.getWritableDatabase();
		db.insert("bookmark", null, cv);
		Cursor result = db.rawQuery("SELECT last_insert_rowid() FROM "
				+ "bookmark", null);
		result.moveToFirst();
		bookmark.setMid(result.getInt(0));
		db.close();
		bookmarks.add(bookmark);
	}

	public void update(Bookmark bookmark) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		ContentValues cv = new ContentValues();
		cv.put("time", sdf.format(bookmark.getTime()));
		cv.put("refer", bookmark.getRefer());
		String whereClause = "iSBN=? AND mid=?";
		String whereArgs[] = new String[] { iSBN,
				String.valueOf(bookmark.getMid()) };
		SQLiteDatabase db = helper.getWritableDatabase();
		db.update("bookmark", cv, whereClause, whereArgs);
		db.close();
	}

	public void remove(Bookmark bookmark) {
		String whereClause = "iSBN=? AND mid=?";
		String whereArgs[] = new String[] { iSBN,
				String.valueOf(bookmark.getMid()) };
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("bookmark", whereClause, whereArgs);
		db.close();
		bookmarks.remove(bookmark);
	}

	public void clear() {
		String whereClause = "iSBN=?";
		String whereArgs[] = new String[] { iSBN };
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("bookmark", whereClause, whereArgs);
		db.close();
		bookmarks.clear();
	}

	public String getiSBN() {
		return iSBN;
	}

	public void setiSBN(String iSBN) {
		this.iSBN = iSBN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public ArrayList<Bookmark> getBookmarks() {
		return bookmarks;
	}

}
