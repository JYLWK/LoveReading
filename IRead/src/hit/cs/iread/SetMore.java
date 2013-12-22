package hit.cs.iread;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

public class SetMore extends Activity {
	
	private EditText clockTime ;
	private ImageButton addtime ;
	private ImageButton subtime ;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setmore);
	}
}
