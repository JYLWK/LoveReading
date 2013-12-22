/*
 * �鿴�ʼǽ���
 * ��book��java����ת����
 * �����������ʾ/����������ťgotocatelog �� gotoedit
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

	TextView notetitle; // �ʼǱ���
	TextView notebookpage;// �ʼǶ�Ӧ���鱾ҳ��
	TextView notedate; // �ʼ�����
	TextView noteview; // �ʼ�����
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
																				// ��ȡ��һ�����ݵĶ���
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
		 * noteview.setText("���Ǳ����飡����������"); notetitle.setText("����֮ҹ�׾���");
		 * notebookpage.setText("ҳ����122"); notedate.setText("2013-11-11");
		 */

		noteview.setOnTouchListener(this);
		noteview.setLongClickable(true);
		mGestureDetector.setIsLongpressEnabled(true);

		gotocatelog.setOnClickListener(new ClickEvent());
		share.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "������ iread");
				intent.putExtra(Intent.EXTRA_TEXT, note.getContent());
				intent.putExtra(Intent.EXTRA_TITLE, "�����ҵıʼ�");
				String title = "�����ҵıʼ�";
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
					Toast.makeText(NoteView.this, "û���ҵ�����΢��", Toast.LENGTH_SHORT).show();
					//ToastUtils.showShort(NoteView.this, "û���ҵ�����΢��");
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
		notebookpage.setText("ҳ����" + note.getRefer());
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

	// �û��ᴥ����������1��MotionEvent ACTION_DOWN����
	public boolean onDown(MotionEvent e) {
		return false;
	}

	// �û��ᴥ����������δ�ɿ����϶�����һ��1��MotionEvent ACTION_DOWN����
	// ע���onDown()������ǿ������û���ɿ������϶���״̬
	public void onShowPress(MotionEvent e) {
	}

	// �û����ᴥ���������ɿ�����һ��1��MotionEvent ACTION_UP����
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

	// �û����´������������ƶ����ɿ�����1��MotionEvent ACTION_DOWN, ���ACTION_MOVE, 1��ACTION_UP����
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 0) {
			noteview.setText("��");
		}
		if (e1.getX() - e2.getX() < 0) {
			noteview.setText("��");
		}
		return false;
	}

	// �û��������������ɶ��MotionEvent ACTION_DOWN����
	public void onLongPress(MotionEvent e) {

	}

	// �û����´����������϶�����1��MotionEvent ACTION_DOWN, ���ACTION_MOVE����
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
