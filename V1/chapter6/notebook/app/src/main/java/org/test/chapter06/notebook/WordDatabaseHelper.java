package org.test.chapter06.notebook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordDatabaseHelper extends SQLiteOpenHelper {
	private final String CREATE_SQL = "create table if not exists dict(_id integer primary key, word, detail)";

	
	//在SQLiteOpenHelper子类当中，必须有该构造函数
	public WordDatabaseHelper(Context context, String name, int version) {

		//必须通过super调用父类的构造函数
		//name：表的名字 version:当前数据库的版本，递增的正数，第三个参数先不讲
		super(context, name, null, version);
	}

	//是一个回调方法，当打开一个数据库的时候调用，如：创建一个数据库
	//第一次创建数据库的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法，也即调用getReadableDatabase或getWritableDatabase方法时才调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_SQL);
	}

	////当更新数据库的时候，注意不是更新数据库里面的表的字段,如：把数据库的版本从1-》2
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("-------------- OnUpdate Called ------------------"
				+ oldVersion + "--->" + newVersion);
	}

	//传入参数：输入的关键词，通过关键词模糊查询，匹配数据据中的单词或者解释列
	public Cursor queryForWords(String key) {
		//rawQuery用于执行select语句
		return this.getReadableDatabase().rawQuery(
				"select * from dict where word like ? or detail like ?",
				new String[] { "%" + key + "%", "%" + key + "%" });
	}

	//得到表总共有多少条数据
	public int getTotalNumOfRecord() {
		Cursor cursor = this.getReadableDatabase().rawQuery(
				"select count(*) from dict", null);
		int totalNum = 0;
		if (cursor.moveToNext()) {
			totalNum = cursor.getInt(0);
		}

		return totalNum;
	}

	//插入添加的生词和解释
	public void insertWord(String[] cols) {
		this.getWritableDatabase().execSQL(
				"insert into dict values(null, ?, ?)", cols);
	}
}
