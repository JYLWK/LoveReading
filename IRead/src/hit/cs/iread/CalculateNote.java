package hit.cs.iread;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CalculateNote extends Activity{
	
	private TextView text ;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculatenote);
	
		text = ( TextView ) findViewById(R.id.text);
		Intent it = super.getIntent();
		text.setText(it.getStringExtra("date"));
	}
}
