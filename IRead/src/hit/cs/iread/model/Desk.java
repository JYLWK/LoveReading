package hit.cs.iread.model;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Desk {
	private SQLiteOpenHelper helper;
	private ArrayList<Book> books;
	private ArrayList<Notebook> notebooks;
	private String category;

	public Desk() {
		category="全部类别";
		books = new ArrayList<Book>();
		notebooks = new ArrayList<Notebook>();
	}

	public void init(SQLiteOpenHelper helper) {
		this.helper = helper;
		Book book = null;
		Notebook notebook = null;
		String sql = "SELECT book.*,notebook.* FROM book LEFT JOIN notebook ON (book.iSBN=notebook.iSBN)";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor result = db.rawQuery(sql, null);
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			book = new Book(String.valueOf(result.getLong(0)));
			book.setTitle(result.getString(1));
			book.setAuthor(result.getString(2));
			book.setTag(result.getString(3));
			book.setPublisher(result.getString(4));
			book.setPubDate(result.getString(5));
			book.setPages(result.getString(6));
			book.setRating(result.getString(7));
			book.setSummary(result.getString(8));
			book.setBitmap(BitmapFactory.decodeByteArray(result.getBlob(9), 0,
					result.getBlob(9).length));
			books.add(book);
			if (!result.isNull(10)) {
				notebook = new Notebook(result.getInt(10));
				notebook.setCategory(result.getString(11));
				notebook.setBook(book);
				notebooks.add(notebook);
			}
		}
		db.close();
	}

	public void addBook(Book book) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ContentValues cv = new ContentValues();
		cv.put("iSBN", Long.valueOf(book.getiSBN()));
		cv.put("title", book.getTitle());
		cv.put("author", book.getAuthor());
		cv.put("tag", book.getTag());
		cv.put("publisher", book.getPublisher());
		cv.put("pubDate", book.getPubDate());
		cv.put("pages", book.getPages());
		cv.put("rating", book.getRating());
		cv.put("summary", book.getSummary());
		book.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, bos);
		cv.put("bitmap", bos.toByteArray());
		SQLiteDatabase db = helper.getWritableDatabase();
		db.insert("book", null, cv);
		db.close();
		books.add(book);
	}

	public Book searchBook(String iSBN) {
		for (Book book : books)
			if (book.getiSBN().equals(iSBN))
				return book;
		return null;
	}

	public void removeBook(Book book) {
		String whereClause = "iSBN=?";
		String whereArgs[] = new String[] { book.getiSBN() };
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("book", whereClause, whereArgs);
		db.close();
		if(!book.isInit())
			book.init(helper);
		book.clear();
		books.remove(book);
	}

	public void addNotebook(Notebook notebook) {
		ContentValues cv = new ContentValues();
		cv.put("category", notebook.getCategory());
		cv.put("iSBN", notebook.getBook().getiSBN());
		SQLiteDatabase db = helper.getWritableDatabase();
		db.insert("notebook", null, cv);
		Cursor result = db.rawQuery("SELECT last_insert_rowid() FROM "
				+ "notebook", null);
		result.moveToFirst();
		notebook.setNid(result.getInt(0));
		db.close();
		notebooks.add(notebook);
	}

	public void removeNotebook(Notebook notebook) {
		String whereClause = "nid=?";
		String whereArgs[] = new String[] { String.valueOf(notebook.getNid()) };
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("notebook", whereClause, whereArgs);
		db.close();
		if(!notebook.isInit())
			notebook.init(helper);
		notebook.clear();
		notebooks.remove(notebook);
	}

	public boolean isInDesk(String iSBN)
	{
		for(Notebook notebook:notebooks)
			if(notebook.getBook().getiSBN().equals(iSBN))
				return true;
		return false;
	}

	public ArrayList<Book> getBooks() {
		ArrayList<Book> books = new ArrayList<Book>();
		books.addAll(this.books);
		return books;
	}

	public ArrayList<Notebook> getNotebooks() {
		ArrayList<Notebook> notebooks = new ArrayList<Notebook>();
		if (category.equals("全部类别"))
			notebooks.addAll(this.notebooks);
		for (Notebook notebook : this.notebooks)
			if (notebook.getCategory().equals(category))
				notebooks.add(notebook);
		return notebooks;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
