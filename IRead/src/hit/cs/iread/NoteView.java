/*
 * 查看笔记界面
 * 从book。java中跳转到这
 * 单击界面会显示/隐藏两个按钮gotocatelog 和 gotoedit
 */
package hit.cs.iread;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import hit.cs.iread.model.Note;
import hit.cs.iread.model.Notebook;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NoteView extends Activity implements OnTouchListener,
		OnGestureListener, android.view.GestureDetector.OnGestureListener {

	TextView notetitle; // 笔记标题
	TextView notebookpage;// 笔记对应的书本页数
	TextView notedate; // 笔记日期
	TextView noteview; // 笔记内容
	Button gotocatelog;
	Button share;
	private String noteTitle;
	private String noteText;
	private String noteDate;
	// private int noteStartPage;

	private Notebook notebook;
	private int id;
	private Note note;
	private GestureDetector mGestureDetector;

	public NoteView() {
		mGestureDetector = new GestureDetector(this);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);

		Intent it = super.getIntent();
		notebook = DeskView.desk.getNotebooks().get(it.getIntExtra("nid", 0));// (Notebook)
																				// it.getSerializableExtra("book");//
																				// 获取上一级传递的对象
		id = it.getIntExtra("id", 0);// (Integer) it.getSerializableExtra("id");
		note = notebook.getNotes().get(id);

		noteview = (TextView) findViewById(R.id.textview);
		notetitle = (TextView) findViewById(R.id.notetitle);
		notedate = (TextView) findViewById(R.id.date);
		notebookpage = (TextView) findViewById(R.id.page);
		gotocatelog = (Button) findViewById(R.id.gotocatelog);
		gotocatelog.setVisibility(View.INVISIBLE);
		share = (Button) findViewById(R.id.share);
		share.setVisibility(View.INVISIBLE);

		/*
		 * noteview.setText("这是本好书！！！！！！"); notetitle.setText("满月之夜白鲸现");
		 * notebookpage.setText("页数：122"); notedate.setText("2013-11-11");
		 */

		noteview.setOnTouchListener(this);
		noteview.setLongClickable(true);
		mGestureDetector.setIsLongpressEnabled(true);

		gotocatelog.setOnClickListener(new ClickEvent());
		share.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "爱读书 iread");
				intent.putExtra(Intent.EXTRA_TEXT, note.getContent());
				intent.putExtra(Intent.EXTRA_TITLE, "分享我的笔记");
				String title = "分享我的笔记";
				/*PackageManager pm = getPackageManager();
				List<ResolveInfo> matches = pm.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
				String packageName = "com.sina.weibo";
				ResolveInfo info = null;
				for (ResolveInfo each : matches) {
					String pkgName = each.activityInfo.applicationInfo.packageName;
					if (packageName.equals(pkgName)) {
						info = each;
						break;
					}
				}
				if (info == null) {
					Toast.makeText(NoteView.this, "没有找到新浪微博", Toast.LENGTH_SHORT).show();
					//ToastUtils.showShort(NoteView.this, "没有找到新浪微博");
					return;
				} else {
					intent.setClassName(packageName, info.activityInfo.name);
				}

				startActivity(intent);*/
				 Intent chooser = Intent.createChooser(intent, title);
				 startActivity(chooser);

			}
		});

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		noteDate = formatter.format(note.getDate());
		noteDate = note.getDate().getYear() + "-" + note.getDate().getMonth()
				+ "-" + note.getDate().getDate();
		noteTitle = note.getTitle();
		// noteStartPage = notebook.getNotes().get(id).getStartPage() ;
		noteText = note.getContent();

		noteview.setText("\t\t\t"+noteText);
		notetitle.setText(noteTitle);
		notedate.setText(noteDate);
		notebookpage.setText("页数：" + note.getRefer());
		// notebookpage.setText(""+noteStartPage);
	}

	class ClickEvent implements OnClickListener {
		public void onClick(View v) {
			if (v.getId() == R.id.gotocatelog) {
				// Intent it = new Intent(NoteView.this, Catelog.class);
				// NoteView.this.startActivity(it);
				// setContentView(R.layout.catelog) ;
				finish();
			} else {
				Intent it = new Intent(NoteView.this, AddNote.class);
				NoteView.this.startActivityForResult(it, 0);
				// setContentView(R.layout.addnote);
			}
		}
	}

	public boolean onTouch(View v, MotionEvent event) {

		return mGestureDetector.onTouchEvent(event);
	}

	// 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
	public boolean onDown(MotionEvent e) {
		return false;
	}

	// 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
	// 注意和onDown()的区别，强调的是没有松开或者拖动的状态
	public void onShowPress(MotionEvent e) {
	}

	// 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
	public boolean onSingleTapUp(MotionEvent e) {
		if (gotocatelog.getVisibility() == View.VISIBLE) {
			gotocatelog.setVisibility(View.INVISIBLE);
			share.setVisibility(View.INVISIBLE);
		} else {
			gotocatelog.setVisibility(View.VISIBLE);
			share.setVisibility(View.VISIBLE);
		}
		return false;
	}

	// 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 0) {
			noteview.setText("左");
		}
		if (e1.getX() - e2.getX() < 0) {
			noteview.setText("右");
		}
		return false;
	}

	// 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
	public void onLongPress(MotionEvent e) {

	}

	// 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	@Override
	public void onGesture(GestureOverlayView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGestureCancelled(GestureOverlayView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGestureEnded(GestureOverlayView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGestureStarted(GestureOverlayView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}
}
