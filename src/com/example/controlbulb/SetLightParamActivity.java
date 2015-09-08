package com.example.controlbulb;

import com.example.bluetoothClient.BlueToothThread;
import com.example.controlbulb.ColorPickerView.OnColorChangedListener;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SetLightParamActivity extends Activity {
	
	public String TAG="setparam";

	BlueToothThread targetThread;

	private SeekBar seekBar;
	private TextView tv_lightIntensity;

	private ColorPickerView colorPickerView;
	private TextView tv_lightColor;

	// send message type
	public static final String Intensity_READMESSAGE = "Intensity";
	public static final String Color_READMESSAGE = "Color";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set_light_param);
		String DeviceName = getIntent().getStringExtra(
				FragmentSetPara.TargetDeviceName);

		targetThread = MainActivity.mBluetoothThreadMap.get(DeviceName);

		if(targetThread==null){
			Toast.makeText(getApplicationContext(),
					"This thread is null",
					Toast.LENGTH_SHORT).show();
			MainActivity.mBluetoothThreadMap.remove(DeviceName);
			MainActivity.msetParamFragment.adapter.remove(DeviceName);
			finish();
		}
		
		if (targetThread.getState() != BlueToothThread.STATE_CONNECTED) {
			Toast.makeText(getApplicationContext(),
					"This device is not Connected, can't send message",
					Toast.LENGTH_SHORT).show();
			MainActivity.mBluetoothThreadMap.remove(DeviceName);
			MainActivity.msetParamFragment.adapter.remove(DeviceName);
			finish();
		} else {
			seekBar = (SeekBar) findViewById(R.id.lightIntensity_seekbar);
			tv_lightIntensity = (TextView) findViewById(R.id.tv_lightIntensity);

			colorPickerView = (ColorPickerView) findViewById(R.id.color_picker_view);
			tv_lightColor = (TextView) findViewById(R.id.tv_lightColor);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		// _________________________ lightIntensity SeekBar__________________
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				tv_lightIntensity.setText(String.valueOf(arg1));
				String sendMsg = Intensity_READMESSAGE + "/  "
						+ String.valueOf(arg1)+"/";
				Log.i(TAG,sendMsg);
				targetThread.write(sendMsg.getBytes());
			}
		});

		// ______________________lightColor colorPickerView____________________
		colorPickerView.setOnColorChangedListener(new OnColorChangedListener() {

			@Override
			public void colorChanged(int color) {
				// TODO Auto-generated method stub
				// 因为color在这里成了一个 负数 ，所以在转化为16进制的时候前面会多了2个ff。在这里转化一下
				String temp = Integer.toHexString(color);
				String HexColor = "  #" + temp.substring(temp.length() - 6);
				String lightColorParam = HexColor + " / "
						+ String.valueOf(color);
				tv_lightColor.setText(lightColorParam);

				String sendMsg = Color_READMESSAGE + "/ "
						+ String.valueOf(color)+"/ ";
				Log.i(TAG,sendMsg);
				targetThread.write(sendMsg.getBytes());
			}
		});

	}
}
