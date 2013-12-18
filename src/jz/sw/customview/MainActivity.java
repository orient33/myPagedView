package jz.sw.customview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		int h = dm.heightPixels, w = dm.widthPixels;
		MyGridLayout gl=initGridLayout(this,w,h);
		setContentView(gl);
	}

	private  MyGridLayout initGridLayout(Context context,int w,int h){
		MyGridLayout v=(MyGridLayout)View.inflate(context, R.layout.my_view, null);
		int rowCount=v.getRowCount();
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < rowCount; i++) {
				TextView tv = (TextView) View.inflate(context, R.layout.itemview, null);
				tv.setTextSize(25);
				tv.setHeight(h);
				tv.setWidth(w);
				tv.setTag("i="+i+" , j="+j);
				tv.setText("row :" + i + ", colum :" + j);
//				tv.setOnClickListener(new OnClickListener(){
//					@Override
//					public void onClick(View v) {
//						Log.i("sw2df", "onclick");
//						showToast((String)v.getTag());
//					}});
				v.addView(tv);
			}
		}
		return v;
	}

	void showToast(String s){
		Toast.makeText(this, s, 0).show();
	}
}
