package org.test.chapter06.notebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

public class ResultActivity extends Activity {
    private Button okBtn;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.popup);
		ListView listView = (ListView)this.findViewById(R.id.show);
		Intent intent = getIntent();		
		Bundle data = intent.getExtras();
		
		@SuppressWarnings("unchecked")
		List<Map<String, String>> list = (List<Map<String, String>>)data.getSerializable("data");

		//第一个参数是context；第二个参数是一个Map的List，第三个参数是布局文件layout资源的ID，
		// 第四个是我put到Map里的key值；第五个是在布局文件中列名对应的视图id
		SimpleAdapter adapter = new SimpleAdapter(ResultActivity.this, list,
				 R.layout.line, new String[]{"word", "detail"}, new int[]{R.id.word, R.id.detail});
		this.setTitle(DictConstant.matchedResult +"(" + list.size() + ")");
		listView.setAdapter(adapter);
		
	    okBtn = (Button)this.findViewById(R.id.ok);
			System.out.println("init okbtn" + okBtn);
			okBtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					 finish();
				}
			});
	}
    
}
