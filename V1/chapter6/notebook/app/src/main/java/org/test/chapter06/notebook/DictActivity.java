package org.test.chapter06.notebook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DictActivity extends Activity {
	private WordDatabaseHelper dbHelper;
	private Button insert = null;
	private Button search = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dict_layout);

		dbHelper = new WordDatabaseHelper(this, "word.db3", 1);
		insert = (Button) this.findViewById(R.id.insert);
		search = (Button) this.findViewById(R.id.search);

		insert.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String word = ((EditText) findViewById(R.id.word)).getText()
						.toString();
				String detail = ((EditText) findViewById(R.id.detail))
						.getText().toString();

				//插入添加的生词和解释
				insertData(dbHelper.getReadableDatabase(), word, detail);

				Toast.makeText(DictActivity.this,DictConstant.newWordInfo + word,Toast.LENGTH_LONG).show();
				//添加生词和解释成功后实时得到生词总数的数量
				refreshStatus();

				//生词和解释文本框清空
				((EditText) findViewById(R.id.word)).setText("");
				((EditText) findViewById(R.id.detail)).setText("");
			}
		});

		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String key = ((EditText) findViewById(R.id.key)).getText()
						.toString();

				//根据输入的关键词，通过模糊查询，匹配数据据中的单词或者解释列，得到所有的数据
				Cursor cursor = dbHelper.queryForWords(key);

				Bundle data = new Bundle();
				data.putSerializable("data", convertCursorToList(cursor));
				Intent intent = new Intent(DictActivity.this,
						ResultActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}

		});
	}

	//得到查询结果的值
	private ArrayList<Map<String, String>> convertCursorToList(Cursor cursor) {
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("word", cursor.getString(1));
			map.put("detail", cursor.getString(2));

			result.add(map);
		}

		return result;
	}

	//插入添加的生词和解释
	private void insertData(SQLiteDatabase db, String word, String detail) {
		this.dbHelper.insertWord(new String[] { word, detail });
	}

	//添加生词和解释成功后实时得到生词总数的数量
	private void refreshStatus() {
		//添加生词按钮下面的[生词总数：xx]
		TextView statusTxt = (TextView) this.findViewById(R.id.status);

		int totalNum = this.dbHelper.getTotalNumOfRecord();
		if (totalNum > 0)
			statusTxt.setText(DictConstant.totalNumDesp + totalNum);
		else
			statusTxt.setText(DictConstant.emptyNum);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

}
