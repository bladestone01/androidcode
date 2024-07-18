package org.test.chapter04.caculators;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.test.chapter04.caculators.info.ConstantInfo;
import org.test.chapter04.caculators.R;

public class MainActivity extends Activity {
	private EditText firstText;
	private EditText secondText;
	private TextView calcTypeText;

	private int firstNumber = 0;
	private int secondNumber = 0;
	// 0: +, 1: -, 2: * , 3: /
	private int calcType = -1;

	// button 声明
	private Button selectBtn;
	private Button calcBtn;

	// request Code
	public static final int RequstCode = 0;
	//public static final int res = 0;

	//protected 类内部，本包，子类可访问，外部包不可访问
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		firstText = (EditText) this.findViewById(R.id.firstNumber);
		secondText = (EditText) this.findViewById(R.id.secondNumber);

		selectBtn = (Button) this.findViewById(R.id.selectType);
		calcBtn = (Button) this.findViewById(R.id.calculate);

		selectBtn.setOnClickListener(new SelectCalculationTypeListener());
		calcBtn.setOnClickListener(new CalculationListener());

		calcTypeText = (TextView)this.findViewById(R.id.calc_type);
	}

	class SelectCalculationTypeListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			//Android中提供了Intent机制来协助应用间的交互与通讯,Intent不仅可用于应用程序之间，
			// 也可用于应用程序内部的activity, service和broadcast receiver之间的交互
			Intent intent = new Intent(MainActivity.this,
					CalculationTypeActivity.class);
			//以便确定返回的数据是从哪个Activity中返回，用来标识目标activity。
			MainActivity.this.startActivityForResult(intent, RequstCode);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//这整数resultCode是由子Activity通过其setResult()方法返回。适用于多个activity都返回数据时，来标识到底是哪一个activity返回的值
		super.onActivityResult(requestCode, resultCode, data);
		if (this.RequstCode == requestCode &&
				CalculationTypeActivity.resultCode == resultCode) {
			Bundle bundle = data.getExtras();
			calcType = data.getIntExtra("type1", -1);
			System.out.println("CalcType:" + calcType);
			calcTypeText.setText(ConstantInfo.typeMap.get(calcType));
		}
	}

	class CalculationListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this,
					ResultActivity.class);
			intent.putExtra("firstNumber", firstText.getText().toString());
			intent.putExtra("secondNumber", secondText.getText().toString());
			intent.putExtra("calcType", calcType);
			int result = 0;
			int firstNumber = Integer.parseInt(firstText.getText().toString());
			int secondNumber = Integer.parseInt(secondText.getText().toString());
			switch (calcType) {
				case 1:
					result = firstNumber + secondNumber;
					break;
				case 2:
					result = firstNumber - secondNumber;
					break;
				case 3:
					result = firstNumber * secondNumber;
					break;
				case 4:
					result = firstNumber/secondNumber;
					break;
				default:
					result = 1;
					break;
			}
			intent.putExtra("result", result);
			startActivity(intent);
		}
	}
}
