/*
 * 笔记目录
 */
package hit.cs.iread;

import hit.cs.iread.model.DatabaseHelper;
import hit.cs.iread.model.Note;
import hit.cs.iread.model.Notebook;

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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Catelog extends Activity {

	private Button addnote;
	private Button select;
	private AutoCompleteTextView auto;
	private ListView listview;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	private HashMap<String, Object> map;
	private HashMap<String, Object> tmap;
	private Button bookmark;
//	private Date date;
	Notebook note;
	int nid;
	private DisplayMetrics dm ;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catelog);

		dm=super.getResources().getDisplayMetrics();
		Intent it = super.getIntent();
		nid=it.getIntExtra("id", 0);
		note=DeskView.desk.getNotebooks().get(nid);
		//note = (Notebook) it.getSerializableExtra("book"); // 获取上一级传递过来的book对象
		if(!note.isInit())
			note.init(new DatabaseHelper(this));
		addnote = (Button) findViewById(R.id.addnote);
		String []DATE=new String[]{"11","22"};
		select=(Button)findViewById(R.id.select);
		auto=(AutoCompleteTextView)findViewById(R.id.auto);
		ArrayAdapter<String> adp=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,DATE);
		this.auto.setAdapter(adp);
		select.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				if(v.getId()==R.id.select)
				{
					int i=-1;
					ArrayList<Note> notes=note.getNotes();
					for(Note nt:notes)
						if(nt.getContent().contains(Catelog.this.auto.getText().toString())&&Catelog.this.auto.getText().toString().length()>0)
						{
							i=notes.indexOf(nt);
							Intent it = new Intent(Catelog.this, NoteView.class);
							it.putExtra("nid", Catelog.this.nid);
							it.putExtra("id", i);
							it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							Catelog.this.startActivity(it);
						}
					if(i==-1)
						Toast.makeText(Catelog.this, "未搜索到相关记录！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		bookmark=(Button)findViewById(R.id.bookmark);
		bookmark.setOnClickListener(new ClickEvent() );
		addnote.setOnClickListener(new ClickEvent());
		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ; try {
		 * date = sdf.parse("2013-10-11") ; } catch (ParseException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */


	}

	@SuppressWarnings("deprecation")
	protected void onResume()
	{
		super.onResume();
		list.clear();
		if (note.getNotes().size() > 0) {
			map = new HashMap<String, Object>();
			map.put("flag", "true");
			map.put("note", note.getNotes().get(0));
			list.add(map);
		}
		for (int i = 1; i < note.getNotes().size(); i++) {
			map = new HashMap<String, Object>();
			tmap = new HashMap<String, Object>();
			if (note.getNotes().get(i).getDate().getMonth() != note.getNotes()
					.get(i - 1).getDate().getMonth()) {
				tmap.put("flag", "false");
				tmap.put("note", note.getNotes().get(i));
				list.add(tmap);
			}
			map.put("flag", "true");
			map.put("note", note.getNotes().get(i));
			list.add(map);
		}
		MyAdapter adapter = new MyAdapter();
		listview = (ListView) findViewById(R.id.cateloglistview);

		listview.setOnItemClickListener(new ItemClickEvent());
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, final long arg3) {
				Dialog dialog = new AlertDialog.Builder(Catelog.this)
						.setTitle("删除？")
						.setMessage("删除选中笔记")
						.setPositiveButton("删除",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										note.remove(note.getNotes().get((int)arg3));
										Catelog.this.onResume();
									}
								}).create();
				dialog.show();
				return true;
			}
		});
	
		listview.setAdapter(adapter);
	}

	class ClickEvent implements OnClickListener {
		public void onClick(View v) {
			if ( v.getId()==R.id.addnote ){
				Intent it = new Intent(Catelog.this, AddNote.class);
				it.putExtra("id", nid);
				it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				Catelog.this.startActivityForResult(it, 1);
			}
			else if ( v.getId()==R.id.bookmark ){
				Intent it = new Intent(Catelog.this , BookmarkView.class);
				it.putExtra("id" , nid) ;
				Catelog.this.startActivity(it);
			}

		}
	}


	class ItemClickEvent implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (((String) list.get((int) id).get("flag")).equals("true")) {
				Intent it = new Intent(Catelog.this, NoteView.class);
				//Bundle mBundle = new Bundle();
				//mBundle.putSerializable("note", (Serializable) note);
				//mBundle.putSerializable("id", id);
				//it.putExtras(mBundle);
				it.putExtra("nid", Catelog.this.nid);
				it.putExtra("id", position);
				it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				Catelog.this.startActivity(it);
			}
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
				view = new TextView(Catelog.this);
			} else
				view = (TextView) arg1;

			// view.setText(""+arg0);
			// view.setHeight(40);
			if (((String) list.get(arg0).get("flag")).equals("false")) {
				// view.setText("2012-11");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
				String dateString = formatter.format(((Note) list.get(arg0)
						.get("note")).getDate());
				//dateString += "__________" ;
				view.setText(dateString.substring(0, 6));
				view.setHeight(25);
			} else {
				String title;
				title = ((Note) list.get(arg0).get("note")).getTitle();
				view.setText(title);
				view.setHeight(dm.heightPixels/12);
				view.setTextSize(20);
				view.setGravity(Gravity.CENTER|Gravity.LEFT);
			}
			return view;
		}

	}

}
