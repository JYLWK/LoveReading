package hit.cs.iread;

import java.util.Date;

import hit.cs.iread.model.Book;
import hit.cs.iread.model.Bookmark;
import hit.cs.iread.model.DatabaseHelper;
import android.app.Activity;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Addmark extends Activity {

	private EditText markpage;
	private Button submit;
	private Book book;
	int nid;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmark);

		Intent it = super.getIntent();
		nid = it.getIntExtra("nid", 0);
		book = DeskView.desk.getNotebooks().get(nid).getBook();
		if (!book.isInit())
			book.init(new DatabaseHelper(this));

		markpage = (EditText) findViewById(R.id.pagetext);
		submit = (Button) findViewById(R.id.sss);

		submit.setOnClickListener(new ClickEvent());
	}

	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (markpage.getText().toString().length() > 0) {
				try {
					Bookmark bookmark = new Bookmark();
					bookmark.setRefer(Integer.valueOf(markpage.getText()
							.toString()));
					Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
					bookmark.setTime(curDate);
					book.add(bookmark);
					finish();
				} catch (Exception e) {
					Toast.makeText(Addmark.this, "提交错误！", Toast.LENGTH_SHORT)
							.show();
				}
			} else
				Toast.makeText(Addmark.this, "不能为空！", Toast.LENGTH_SHORT)
						.show();
		}

	}
}
