package hit.cs.iread;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Day extends Activity {

	private List<String> list = new ArrayList<String>() ;
//	private TextView textview ;
	private Spinner myspinner ;
	private ArrayAdapter <String> adapter ;
	ArrayList<HashMap<String,Object>> weekdaygrid=new ArrayList<HashMap<String,Object>>();
	ArrayList<HashMap<String,Object>> grid=new ArrayList<HashMap<String,Object>>(); 
	HashMap<String,Object> map ;
	private GridView wgrid ;
	private GridView dgrid ;
	private int whichday ;
	private int endday ;
	private ImageButton prebutton ;
	private ImageButton nextbutton ;
	private TextView date ;
	private int year ;
	private int month ;
	private int yearnow ;
	private int monthnow ;
	private int daynow ;
	private CalculateFirstday calculation ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.day);
		
		myspinner = (Spinner) findViewById(R.id.spinner) ;
		prebutton = (ImageButton) findViewById(R.id.premonth) ;
		nextbutton = (ImageButton) findViewById(R.id.nextmonth);
		date = (TextView) findViewById(R.id.date);
			
		prebutton.setOnClickListener(new ClickEvent());
		nextbutton.setOnClickListener(new ClickEvent());
		
		list.add("21天学会PHP");
		list.add("JAVA");
		list.add(".NET");
		
		Calendar c = Calendar.getInstance();
		yearnow = c.get(Calendar.YEAR);
		monthnow = c.get(Calendar.MONTH)+1;
		daynow = c.get(Calendar.DAY_OF_MONTH);
		year = yearnow ;
		month = monthnow; 
		
		adapter = new ArrayAdapter<String> (this , android.R.layout.simple_spinner_item ,list) ;
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		myspinner.setAdapter(adapter);
		
		myspinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//textview.setText(adapter.getItem(arg2));
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		putCalendar();
	}

	public void putCalendar(){
		weekdaygrid.clear();
		grid.clear();
		
		date.setText(""+year+"年"+month);
		calculation = new CalculateFirstday(year , month ) ;
		whichday = calculation.getFirstday();
		endday = calculation.getEndday();
		
		for ( int i=1;i<8;i++ ){
			map = new HashMap<String , Object>();
			map.put("weekday", "星期"+i);
			weekdaygrid.add(map);
		}
		
		for ( int i=1;i<whichday;i++ ){
			map = new HashMap<String,Object>();
			grid.add(map);
		}
		
		for ( int i=whichday;i<endday+whichday;i++ ){
			map = new HashMap<String , Object>();
			map.put("day" , "  "+(i-whichday+1) );
			map.put("numberofnotes", "  "+i);
			grid.add(map);
		}
		
		wgrid = (GridView) findViewById(R.id.weekday);
		dgrid = (GridView) findViewById(R.id.calendar);
		
		SimpleAdapter wadapter = new SimpleAdapter(this ,
								weekdaygrid,
								R.layout.weekdaygridview,
								new String[]{"weekday"},
								new int[]{R.id.week});
		
		MyAdapter dadapter = new MyAdapter();
		//dgrid.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		DisplayMetrics  dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); 
	//	int screenWidth = dm.widthPixels;  
		
		//dgrid.setColumnWidth(screenWidth/7);
		
		wgrid.setAdapter(wadapter);
		dgrid.setAdapter(dadapter);
		
		dgrid.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if ( arg2+1>=whichday ){
					Intent it = new Intent(Day.this , CalculateNote.class);
					it.putExtra("date" , ""+year+"年"+month+"月"+(arg2-whichday+2));
					Day.this.startActivity(it);
				}
			}
			
		});
 	}
	
	class ClickEvent implements OnClickListener{
		
		public void onClick(View v) {
			
			if ( v.getId()==R.id.premonth ){
				if ( month==1 ){
					year--;
					month=12;
				}
				else {
					month--;
				}
				putCalendar();
			}
			else if ( v.getId()==R.id.nextmonth ){
				if ( month==12 ){
					year++;
					month=1;
				}
				else { 
					month++;
				}
				putCalendar();
			}
		
		}
	
	}
	
	public class MyAdapter extends BaseAdapter{

		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return  whichday+endday-1;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0 ;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0 ;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			TextView view=null ;
			if ( arg1==null ){
				view = new TextView(Day.this); 
			}	
			else view = (TextView) arg1 ;
			if ( arg0+1>=whichday ){
				view.setText(""+(arg0-whichday+2)+"\n");
			}
			view.setBackgroundColor(Color.WHITE);
			view.setHeight(80) ;
			if ( yearnow==calculation.getYear() && monthnow==calculation.getMonth() && daynow==arg0+2-whichday ){
				view.setBackgroundColor(Color.GREEN) ;
			}
			
			return view ;
		}
		
	}

}
	
	
