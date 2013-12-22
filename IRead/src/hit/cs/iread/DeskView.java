/*
 * 显示已收藏书的界面，显示于IRead中
 */
package hit.cs.iread;

import hit.cs.iread.model.Notebook;
import hit.cs.iread.model.Desk;
import hit.cs.iread.model.DatabaseHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class DeskView extends Activity {
	static Desk desk;
	GridView myview;
	ListView mylist;
	Bitmap bitmap;
	Bitmap newbookimage;
	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> grid = new ArrayList<HashMap<String, Object>>();
	HashMap<String, Object> map;
	ArrayList<Notebook> notebooks;
	int numofbooks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_book);
		desk = new Desk();
		desk.init(new DatabaseHelper(this));
	}

	protected void onResume()
	{
		super.onResume();
		getBookInfor();
		jumpToGrid();
	}

	public void jumpToGrid() {
		setContentView(R.layout.grid_book);
		myview = (GridView) findViewById(R.id.gridview);
		SimpleAdapter adapter = new SimpleAdapter(this, grid,
				R.layout.bookgridview, new String[] { "image" },
				new int[] { R.id.image });
		adapter.setViewBinder(new ViewBinder() {

			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				// 判断是否为我们要处理的对象
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView iv = (ImageView) view;
					iv.setImageBitmap((Bitmap) data);
					return true;
				} else
					return false;
			}
		});
		myview.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, final long arg3) {
				Dialog dialog = new AlertDialog.Builder(DeskView.this)
						.setTitle("删除？")
						.setMessage("删除选中图书")
						.setPositiveButton("删除",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										remove((int) arg3);
										getBookInfor();
										jumpToGrid();
									}
								}).create();
				dialog.show();
				return true;
			}
		});

		myview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (id == numofbooks) {
					Intent it = new Intent(DeskView.this, com.google.zxing.demo.CaptureActivity.class);
					DeskView.this.startActivity(it);
				} else {
					Intent it = new Intent(DeskView.this, Catelog.class);
					it.putExtra("id", (int)id);
					//it.putExtra("book", notebooks.get((int) id));
					/*Bundle mBundle = new Bundle();
					mBundle.putSerializable("book",
							 notebooks.get((int) id));
					it.putExtras(mBundle);*/
					DeskView.this.startActivity(it);

				}
			}
		});

		myview.setAdapter(adapter);

		Button button1 = (Button) findViewById(R.id.list);
		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				jumpToList();
			}
		});
/*		ImageButton refreshbutton = (ImageButton) findViewById(R.id.refresh);
		refreshbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*
				 * Intent it = new Intent(book.this , IRead.class);
				 * startActivity(it); finish();
				 
				getBookInfor();
				jumpToGrid();
			}
		});*/

	}

	@SuppressLint("CutPasteId")
	public void jumpToList() {

		setContentView(R.layout.list_book);
		mylist = (ListView) findViewById(R.id.MyListView);
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.booklistview, new String[] { "image", "bookname",
						"authorname", "pages" }, new int[] { R.id.image,
						R.id.bookname, R.id.authorname, R.id.pages });
		adapter.setViewBinder(new ViewBinder() {

			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				// 判断是否为我们要处理的对象
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView iv = (ImageView) view;
					iv.setImageBitmap((Bitmap) data);
					return true;
				} else
					return false;
			}
		});
		mylist.setAdapter(adapter);
		Button button2 = (Button) findViewById(R.id.grid);
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				jumpToGrid();
			}
		});
		ImageButton refreshbutton = (ImageButton) findViewById(R.id.refresh);
		refreshbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*
				 * Intent it = new Intent(book.this , IRead.class);
				 * startActivity(it); finish();
				 */
				getBookInfor();
				jumpToList();
			}
		});

	}

	class ClickEvent implements OnClickListener {
		public void onClick(View v) {
			Intent it = new Intent(DeskView.this, NoteView.class);
			DeskView.this.startActivity(it);
		}
	}

	public void remove(int id) {
		desk.removeNotebook(notebooks.get(id));
		desk.removeBook(notebooks.get(id).getBook());
	}

	public void getBookInfor() {
		DisplayMetrics dm=super.getResources().getDisplayMetrics();
		this.notebooks = desk.getNotebooks();

		InputStream is = getResources().openRawResource(R.drawable.newbook);
		newbookimage = BitmapFactory.decodeStream(is);
		newbookimage = Bitmap.createScaledBitmap(newbookimage, dm.widthPixels/4, dm.heightPixels/5, true);
		numofbooks = notebooks.size();

		list.clear();
		grid.clear();
		for (int i = 0; i < numofbooks; i++) {
			map = new HashMap<String, Object>();
			bitmap = this.notebooks.get(i).getBook().getBitmap();
			bitmap = Bitmap.createScaledBitmap(bitmap, dm.widthPixels/4, dm.heightPixels/5, true);
			map.put("image", bitmap);
			grid.add(map);
			if (i + 1 == numofbooks) {
				map = new HashMap<String, Object>();
				map.put("image", newbookimage);
				grid.add(map);
			}
		}

		if (numofbooks == 0) {
			map = new HashMap<String, Object>();
			map.put("image", newbookimage);
			grid.add(map);
		}

		for (int i = 0; i < numofbooks; i++) {
			map = new HashMap<String, Object>();
			bitmap = this.notebooks.get(i).getBook().getBitmap();
			bitmap = Bitmap.createScaledBitmap(bitmap, dm.widthPixels/4, dm.heightPixels/5, true);
			map.put("image", bitmap);
			map.put("bookname", this.notebooks.get(i).getBook().getTitle());
			map.put("authorname", "作者：  "
					+ this.notebooks.get(i).getBook().getAuthor());
			map.put("pages", "页数：  "
					+ this.notebooks.get(i).getBook().getPages() + "页");
			list.add(map);

		}
		if (numofbooks == 0) {
			map = new HashMap<String, Object>();
			map.put("image", newbookimage);
			map.put("bookname", "newbook");
			list.add(map);
		}
	}
}
