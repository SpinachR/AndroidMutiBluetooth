package com.example.controlbulb;

import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FindBluetoothDevices extends Activity {
	private static final String TAG = "DeviceListActivity";

	public Button scanBtn;
	// Intent key-value
	public static String DEVICE_ADDRESS = "device_address";

	private BluetoothAdapter mBlueTAdapter;

	private ArrayAdapter<String> mPairedDeviceArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_list);
		// In case user hit back btn
		setResult(Activity.RESULT_CANCELED);

		scanBtn = (Button) findViewById(R.id.button_scan);
		scanBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				doDiscovery();
				arg0.setVisibility(View.GONE);
				Log.i(TAG,"start discovery");
			}
		});

		// Initialize two array adapters
		mPairedDeviceArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		// set up ListView for paired devices
		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDeviceArrayAdapter);
		pairedListView.setOnItemClickListener(mDeivceClickListener);

		// set up ListView for New devices
		ListView newListView = (ListView) findViewById(R.id.new_devices);
		newListView.setAdapter(mNewDevicesArrayAdapter);
		newListView.setOnItemClickListener(mDeivceClickListener);

		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);
		
		IntentFilter mfilter = new IntentFilter(
				BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, mfilter);

		mBlueTAdapter = BluetoothAdapter.getDefaultAdapter();

		// get paired devices
		Set<BluetoothDevice> pairedDevice = mBlueTAdapter.getBondedDevices();
		if (pairedDevice.size() > 0) {
			for (BluetoothDevice device : pairedDevice) {
				mPairedDeviceArrayAdapter.add(device.getName() + "\n"
						+ device.getAddress());
			}
		} else {
			mPairedDeviceArrayAdapter.add("No paired devices");
		}
	}

	protected void doDiscovery() {
		// TODO Auto-generated method stub
		setProgressBarIndeterminateVisibility(true);
		if (mBlueTAdapter.isDiscovering()) {
			mBlueTAdapter.cancelDiscovery();
		}
		mBlueTAdapter.startDiscovery();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mBlueTAdapter != null) {
			mBlueTAdapter.cancelDiscovery();
		}
		this.unregisterReceiver(mReceiver);
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice newDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (newDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
					mNewDevicesArrayAdapter.add(newDevice.getName() + "\n"
							+ newDevice.getAddress());
				}
			}
			if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
				//when discovery finished
				scanBtn.setVisibility(View.VISIBLE);
				setProgressBarIndeterminateVisibility(false);
				if(mNewDevicesArrayAdapter.getCount()==0){
					mNewDevicesArrayAdapter.add("No new device");
				}
			}

		}
	};

	//ListView items Onclick listener
	private OnItemClickListener mDeivceClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> av, View v, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			mBlueTAdapter.cancelDiscovery();
			//Get MAC address
			String info = ((TextView) v).getText().toString();
			String Address = info.substring(info.length()-17);
			
			Intent intent = new Intent();
			intent.putExtra(DEVICE_ADDRESS, Address);
			Log.i(TAG,Address);
			
			setResult(Activity.RESULT_OK,intent);
			finish();
			
		}
	};
}
