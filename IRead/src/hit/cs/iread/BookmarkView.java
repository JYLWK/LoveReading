package hit.cs.iread;

import hit.cs.iread.model.Book;
import hit.cs.iread.model.Bookmark;
import hit.cs.iread.model.DatabaseHelper;
import hit.cs.iread.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class BookmarkView extends Activity {
	
	private Button addmark ;
	private ListView bookmarklistview ;
	private ArrayList<HashMap<String , Object>> list = new ArrayList<HashMap<String , Object>>();
	private HashMap<String , Object> map ;
	private Book book ;
	private ArrayList<Bookmark> marklist ; 
	int nid ;
	private int screenHeight ;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmarkview);
		
		DisplayMetrics dm=super.getResources().getDisplayMetrics();
		screenHeight = dm.heightPixels;
		
		Intent it = super.getIntent();
		nid=it.getIntExtra("id", 0);
		book = DeskView.desk.getNotebooks().get(nid).getBook();
		if(!book.isInit())
		book.init(new DatabaseHelper(this));
		
	}
	
	protected void onResume(){
		super.onResume();
		marklist = book.getBookmarks() ;
		list.clear();
		for ( int i=0;i<marklist.size();i++ ){
			map = new HashMap<String , Object>();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
			String dateString = formatter.format(marklist.get(i).getTime());
			map.put("time", dateString);
			map.put("refer", marklist.get(i).getRefer());
		//	map.put("id", marklist.get(i).getMid());
			list.add(map);
		}
		
		bookmarklistview = (ListView) findViewById(R.id.bookmarklist);
		MyAdapter adapter = new MyAdapter();
		bookmarklistview.setAdapter(adapter);
		bookmarklistview.setOnItemClickListener(new ItemClickEvent());
		addmark = (Button) findViewById(R.id.addmark);
		
		bookmarklistview.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, final long arg3) {
				Dialog dialog = new AlertDialog.Builder(BookmarkView.this)
						.setTitle("删除？")
						.setMessage("删除选中图书")
						.setPositiveButton("删除",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										book.remove( marklist.get((int)arg3) );
										BookmarkView.this.onResume();
									}
								}).create();
				dialog.show();
				return true;
			}
		});

		addmark.setOnClickListener(new ClickEvent());
	}
	class ClickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Intent it = new Intent(BookmarkView.this , )
			Intent it = new Intent(BookmarkView.this , Addmark.class);
			it.putExtra("nid", BookmarkView.this.nid);
			BookmarkView.this.startActivityForResult(it, 0);
		}
		
	}
	
	class ItemClickEvent implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent it = new Intent(BookmarkView.this , Updatemark.class);
			it.putExtra("nid", BookmarkView.this.nid);
			it.putExtra("id", (int)id);
			BookmarkView.this.startActivityForResult(it, 1);
			
		}
	}
	
	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			TextView view = null;

			if (arg1 == null) {
				view = new TextView(BookmarkView.this);
			} else
				view = (TextView) arg1;

			// view.setText(""+arg0);
			// view.setHeight(40);
			view.setText("看到了第"+list.get(arg0).get("refer")+"页"+"\n时间："+list.get(arg0).get("time"));
			
			view.setHeight(screenHeight/10);
			return view;
		}

	}
}
