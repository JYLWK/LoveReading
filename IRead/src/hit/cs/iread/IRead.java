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
    public LinearLayout container;// 装载sub Activity的容器  
    /** 顶部按钮图片 **/  
    int[] topbar_image_array = {R.drawable.image0 , R.drawable.image1 ,R.drawable.image2};
    
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_iread);  
        
        gvTopBar = (GridView) findViewById(R.id.gvTopBar);  
        gvTopBar.setNumColumns(topbar_image_array.length);// 设置每行列数  
        gvTopBar.setSelector(new ColorDrawable(Color.TRANSPARENT));// 选中的时候为透明色  
        gvTopBar.setGravity(Gravity.CENTER);// 位置居中  
        gvTopBar.setVerticalSpacing(0);// 垂直间隔  
        int width = this.getWindowManager().getDefaultDisplay().getWidth()  / 3;  
        topImgAdapter = new ImageAdapter(this, topbar_image_array, width, 48,R.drawable.bg);  
        gvTopBar.setAdapter(topImgAdapter);// 设置菜单Adapter  
        gvTopBar.setOnItemClickListener(new ItemClickEvent());// 项目点击事件  
        container = (LinearLayout) findViewById(R.id.Container);  
        SwitchActivity(0);//默认打开第0页  
    }  
  
    class ItemClickEvent implements OnItemClickListener {  
  
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {  
            SwitchActivity(arg2);  
        }  
    }  
    /** 
     * 根据ID打开指定的Activity 
     * @param id GridView选中项的序号 
     */  
    void SwitchActivity(int id)  
    {  
        topImgAdapter.SetFocus(id);//选中项获得高亮  
        container.removeAllViews();//必须先清除容器中所有的View  
        Intent intent =null;  
        if (id == 0 ) {  
            intent = new Intent(IRead.this, DeskView.class);  
        } else if (id == 1) {  
            intent = new Intent(IRead.this, Day.class);  
        }   else if (id==2) {
        	intent = new Intent(IRead.this, SetMore.class) ;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        //Activity 转为 View  
        Window subActivity = getLocalActivityManager().startActivity(  
                "subActivity", intent);  
        //容器添加View  
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
    	new AlertDialog.Builder(this).setTitle("确认要退出吗？")
    	.setPositiveButton("确定" , new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog , int which ){
    			IRead.this.finish();
    		}
    	})
    	.setNegativeButton("返回" , new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog , int which ){
    			
    		}
    	}).show();
    }
  
}  
