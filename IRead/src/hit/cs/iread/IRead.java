package hit.cs.iread;

import android.app.ActivityGroup;    
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;   
import android.graphics.Color;  
import android.graphics.drawable.ColorDrawable;  
import android.os.Bundle;    
import android.view.Gravity;  
import android.view.KeyEvent;
import android.view.View;  
import android.view.Window;  
import android.view.ViewGroup.LayoutParams;  
import android.widget.AdapterView;  
import android.widget.GridView;  
import android.widget.LinearLayout;    
import android.widget.AdapterView.OnItemClickListener;  
/** 
 *  
 * @author GV 
 * 
 */  
@SuppressWarnings("deprecation")
public class IRead extends ActivityGroup{  
  
    private GridView gvTopBar;  
    private ImageAdapter topImgAdapter;  
    public LinearLayout container;// װ��sub Activity������  
    /** ������ťͼƬ **/  
    int[] topbar_image_array = {R.drawable.image0 , R.drawable.image1 ,R.drawable.image2};
    
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_iread);  
        
        gvTopBar = (GridView) findViewById(R.id.gvTopBar);  
        gvTopBar.setNumColumns(topbar_image_array.length);// ����ÿ������  
        gvTopBar.setSelector(new ColorDrawable(Color.TRANSPARENT));// ѡ�е�ʱ��Ϊ͸��ɫ  
        gvTopBar.setGravity(Gravity.CENTER);// λ�þ���  
        gvTopBar.setVerticalSpacing(0);// ��ֱ���  
        int width = this.getWindowManager().getDefaultDisplay().getWidth()  / 3;  
        topImgAdapter = new ImageAdapter(this, topbar_image_array, width, 48,R.drawable.bg);  
        gvTopBar.setAdapter(topImgAdapter);// ���ò˵�Adapter  
        gvTopBar.setOnItemClickListener(new ItemClickEvent());// ��Ŀ����¼�  
        container = (LinearLayout) findViewById(R.id.Container);  
        SwitchActivity(0);//Ĭ�ϴ򿪵�0ҳ  
    }  
  
    class ItemClickEvent implements OnItemClickListener {  
  
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {  
            SwitchActivity(arg2);  
        }  
    }  
    /** 
     * ����ID��ָ����Activity 
     * @param id GridViewѡ�������� 
     */  
    void SwitchActivity(int id)  
    {  
        topImgAdapter.SetFocus(id);//ѡ�����ø���  
        container.removeAllViews();//������������������е�View  
        Intent intent =null;  
        if (id == 0 ) {  
            intent = new Intent(IRead.this, DeskView.class);  
        } else if (id == 1) {  
            intent = new Intent(IRead.this, Day.class);  
        }   else if (id==2) {
        	intent = new Intent(IRead.this, SetMore.class) ;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        //Activity תΪ View  
        Window subActivity = getLocalActivityManager().startActivity(  
                "subActivity", intent);  
        //�������View  
        container.addView(subActivity.getDecorView(),  
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);  
    }  
    
    public boolean onKeyDown(int key , KeyEvent event){
    	if ( key == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0 ){
    		aDialog();
    	}
    	return true ;
    }
    
    public void aDialog(){
    	new AlertDialog.Builder(this).setTitle("ȷ��Ҫ�˳���")
    	.setPositiveButton("ȷ��" , new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog , int which ){
    			IRead.this.finish();
    		}
    	})
    	.setNegativeButton("����" , new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog , int which ){
    			
    		}
    	}).show();
    }
  
}  
