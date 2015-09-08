package com.example.controlbulb;

import com.example.bluetoothClient.BlueToothThread;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentSetPara extends ListFragment {
	
	private String TAG = "setParaFragment";
	public ListView connectedDeviceList;
	public ArrayAdapter<String> adapter;
	
	

	public static String TargetDeviceName = "TargetDeviceName";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.tab_set_lightparam, container,
				false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
	
		String deviceName = ((TextView) v).getText().toString();
		BlueToothThread targetThread = MainActivity.mBluetoothThreadMap
				.get(deviceName);
		

		if (targetThread != null) {
			if (targetThread.getState() != BlueToothThread.STATE_CONNECTED) {
				Toast.makeText(getActivity(),
						"This device is not Connected, can't send message",
						Toast.LENGTH_SHORT).show();
				MainActivity.msetParamFragment.adapter.remove(deviceName);
				MainActivity.mBluetoothThreadMap.remove(deviceName);
				return;
			} else {
				
				Intent setLightParamIntent = new Intent(getActivity(),
						SetLightParamActivity.class);
		
				setLightParamIntent.putExtra(TargetDeviceName, deviceName);
				
				startActivity(setLightParamIntent);
			}
		} else {
			Toast.makeText(getActivity(), "This thread is null",
					Toast.LENGTH_SHORT).show();

		}
	}

}
