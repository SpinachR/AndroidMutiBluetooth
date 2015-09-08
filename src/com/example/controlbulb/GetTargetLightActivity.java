package com.example.controlbulb;

import java.util.Iterator;
import java.util.Set;
import com.example.bluetoothClient.BlueToothThread;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ListActivity;

public class GetTargetLightActivity extends ListActivity {

	public static String TAG = "TargetBulb";

	public ArrayAdapter<String> TargetBulbAdapter;

	public static final String Mode_READMESSAGE = "Mode";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_get_target_light);
		TargetBulbAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		setListAdapter(TargetBulbAdapter);

		Set<String> key = MainActivity.mBluetoothThreadMap.keySet();
		for (Iterator it = key.iterator(); it.hasNext();) {
			String deviceName = (String) it.next();
			BlueToothThread ListtargetThread = MainActivity.mBluetoothThreadMap
					.get(deviceName);
			if (ListtargetThread != null) {
				if (ListtargetThread.getState() == BlueToothThread.STATE_CONNECTED) {
					TargetBulbAdapter.add(deviceName);
				} else {
					TargetBulbAdapter.remove(deviceName);
					MainActivity.mBluetoothThreadMap.remove(deviceName);
					MainActivity.msetParamFragment.adapter.remove(deviceName);
				}
			}
		}
		if (TargetBulbAdapter.getCount() == 0) {
			Toast.makeText(this, "no connected device, can't write settings",
					Toast.LENGTH_SHORT).show();
			finish();

		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		String deviceName = ((TextView) v).getText().toString();
		BlueToothThread targetThread = MainActivity.mBluetoothThreadMap
				.get(deviceName);
		String ModeType = getIntent().getStringExtra("ModeType");
		String timeMsg = getIntent().getStringExtra("setTime");

		Log.i(TAG, "ModeType: " + ModeType);
		Log.i(TAG, "time: " + timeMsg);

		if (targetThread != null) {
			if (targetThread.getState() == BlueToothThread.STATE_CONNECTED) {

				if (ModeType != null) {
					String sendMsg = Mode_READMESSAGE + "/  " + ModeType + "/";
					targetThread.write(sendMsg.getBytes());
					Toast.makeText(getApplicationContext(),
							"set " + ModeType + " for " + deviceName,
							Toast.LENGTH_SHORT).show();
					Log.i(TAG, sendMsg);
				} else if (timeMsg != null) {
					targetThread.write(timeMsg.getBytes());
					String[] Msg = timeMsg.split("/");
					Toast.makeText(getApplicationContext(),
							"turn off " + deviceName + " after "+Msg[1],
							Toast.LENGTH_SHORT).show();
					Log.i(TAG, timeMsg);
				}

			} else {
				Toast.makeText(getApplicationContext(),
						"This device is not Connected, can't send message",
						Toast.LENGTH_SHORT).show();
				MainActivity.mBluetoothThreadMap.remove(deviceName);
				MainActivity.msetParamFragment.adapter.remove(deviceName);
			}
		} else {
			Toast.makeText(getApplicationContext(), "This thread is null",
					Toast.LENGTH_SHORT).show();
			MainActivity.mBluetoothThreadMap.remove(deviceName);
			MainActivity.msetParamFragment.adapter.remove(deviceName);
		}
	}

}
