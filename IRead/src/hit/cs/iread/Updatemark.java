package hit.cs.iread;

import java.util.Date;

import hit.cs.iread.model.Book;
import hit.cs.iread.model.Bookmark;
import hit.cs.iread.model.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Updatemark extends Activity {

	int nid;
	private int id;
	private EditText newmark;
	private Button submit;
	private Book book;
	private Bookmark bookmark;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatemark);

		Intent it = super.getIntent();
		nid = it.getIntExtra("nid", 0);
		id = it.getIntExtra("id", 0);
		book = DeskView.desk.getNotebooks().get(nid).getBook();
		if (!book.isInit())
			book.init(new DatabaseHelper(this));
		bookmark = book.getBookmarks().get(id);

		newmark = (EditText) findViewById(R.id.newmark);
		submit = (Button) findViewById(R.id.sss1);

		submit.setOnClickListener(new ClickEvent());
	}

	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			if (newmark.getText().toString().length() > 0) {
				try {
					// TODO Auto-generated method stub
					bookmark.setRefer(Integer.valueOf(newmark.getText()
							.toString()));
					Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
					bookmark.setTime(curDate);
					book.update(bookmark);
					finish();
				} catch (Exception e) {
					Toast.makeText(Updatemark.this, "提交错误！", Toast.LENGTH_SHORT)
							.show();
				}
			} else
				Toast.makeText(Updatemark.this, "不能为空！", Toast.LENGTH_SHORT)
						.show();
		}

	}
}
