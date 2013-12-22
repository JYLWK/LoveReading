/*
 * 写新笔记
 *
 */
package hit.cs.iread;

import java.util.Calendar;
import java.util.Date;

import hit.cs.iread.model.Note;
import hit.cs.iread.model.Notebook;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddNote extends Activity {

	TextView date;
	Button select;
	EditText page;
	EditText edittext;
	Button submit;
	private Notebook notebook;

	String title;
	String text;
	private Date day;
	int id;

	// Button goback ;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnote);

		Intent it = super.getIntent();
		id = it.getIntExtra("id", 0);
		notebook = (Notebook) DeskView.desk.getNotebooks().get(id);

		date = (TextView) findViewById(R.id.date);
		select = (Button) findViewById(R.id.select);
		page = (EditText) findViewById(R.id.page);
		edittext = (EditText) findViewById(R.id.newnote);
		submit = (Button) findViewById(R.id.submit);

		edittext.addTextChangedListener(new TextWatcher());
		submit.setOnClickListener(new ClickEvent());
		day = new Date();
		Calendar c = Calendar.getInstance();
		day.setYear(c.get(Calendar.YEAR));
		day.setMonth(c.get(Calendar.MONTH));
		day.setDate(c.get(Calendar.DAY_OF_MONTH));
		date.setText(day.getYear() + "年" + (day.getMonth() + 1) + "月"
				+ day.getDate() + "日");
		// goback.setOnClickListener(new ClickEvent()) ;
		select.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(0);
			}
		});
	}

	class ClickEvent implements OnClickListener {
		public void onClick(View v) {
			if (v.getId() == R.id.submit) {
				// textview.setText(edittext.getText().toString());
				text = edittext.getText().toString();
				if (text.length() > 0) {
					try {
						Note note = new Note();
						if (page.getText().toString().length() > 0)
							note.setRefer(Integer.valueOf(page.getText()
									.toString()));
						note.setContent(text);
						note.setDate(day);
						// Date curDate = new
						// Date(System.currentTimeMillis());// 获取当前时间
						// note.setDate(curDate);
						notebook.add(note);
						AddNote.this.finish();
					} catch (Exception e) {
						Toast.makeText(AddNote.this, "提交错误！",
								Toast.LENGTH_SHORT).show();
					}
				} else
					Toast.makeText(AddNote.this, "内容不能为空！！", Toast.LENGTH_SHORT)
							.show();
			}

		}
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		Calendar c = Calendar.getInstance();
		dialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {
					@SuppressWarnings("deprecation")
					public void onDateSet(DatePicker dp, int year, int month,
							int dayOfMonth) {
						date.setText(year + "年" + (month + 1) + "月"
								+ dayOfMonth + "日");
						day.setYear(year);
						day.setMonth(month);
						day.setDate(dayOfMonth);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		return dialog;
	}

	class TextWatcher implements android.text.TextWatcher {
		// private CharSequence temp;
		// private int selectionStart;
		// private int selectionEnd;
		private TextView view;

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// temp = s;
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void afterTextChanged(Editable s) {
			view = (TextView) findViewById(R.id.textnumber);
			int num = s.length();
			num = 300 - num;
			view.setText(num + "/300");
		}
	}
}
