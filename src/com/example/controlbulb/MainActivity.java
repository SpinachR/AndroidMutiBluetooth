package com.example.controlbulb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.example.bluetoothClient.BlueToothThread;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private String TAG = "MainActivity";

	public LinearLayout mTabSetParam;
	public LinearLayout mTabSetTimer;

	private SildeMenu left_menu;

	private ImageButton mBtnSetParam;
	private ImageButton mBtnSetTimer;
	private ImageButton mBtnSetupMode;
	private ImageButton mBtnFindDevice;

	// mode button
	private ImageButton mBtnDailyMode;
	private ImageButton mBtnSleepMode;
	private ImageButton mBtnWorkMode;
	private ImageButton mBtnMusicMode;
	private ImageButton mBtnPartyMode;
	private ImageButton mBtnRestMode;
	private ImageButton mBtnLoveMode;

	public static FragmentSetPara msetParamFragment;
	public static FragmentSetTimer msetTimerFragment;

	private FragmentManager fm;

	// Message types sent from Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key name -bundle from Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes onActivityforResult
	private static final int REQUEST_CONNECT_DEVICE_ADDRESS = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	public static Map<String, BlueToothThread> mBluetoothThreadMap = new HashMap<String, BlueToothThread>();

	// private BlueToothThread mBluetoothThread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		fm = getSupportFragmentManager();

		initView();// 初始化控件

		initEvent(); // 初始化事件
		selectFragment(0);
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
		// else {
		// if (mBluetoothThread == null) {
		// mBluetoothThread = new BlueToothThread(this, mHandler);
		// mOutStringBuffer = new StringBuffer("");
		// }
		// }
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// if (mBluetoothThread != null) {
		// mBluetoothThread.start();
		// }
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Collection<BlueToothThread> c = mBluetoothThreadMap.values();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			BlueToothThread btt = (BlueToothThread) it.next();
			btt.stop();
		}
		mBluetoothThreadMap.clear();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		// if the findDeviceActivity return the MAC addresss
		case REQUEST_CONNECT_DEVICE_ADDRESS:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(
						FindBluetoothDevices.DEVICE_ADDRESS);
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				Log.i(TAG, device.getName());
				BlueToothThread mBlueToothThread = new BlueToothThread(
						getApplicationContext(), mHandler);
				mBlueToothThread.connect(device);
				mBluetoothThreadMap.put(device.getName(), mBlueToothThread);

			}
			break;
		case REQUEST_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
				// mBluetoothThread = new BlueToothThread(this, mHandler);
				// mOutStringBuffer = new StringBuffer("");
			} else {
				Toast.makeText(this, "BT not enabled", Toast.LENGTH_LONG)
						.show();
				finish();
			}

		default:
			break;
		}

	}

	private void initEvent() {
		// TODO Auto-generated method stub
		mBtnSetParam.setOnClickListener(this);
		mBtnSetTimer.setOnClickListener(this);

		mBtnSetupMode.setOnClickListener(this);
		mBtnFindDevice.setOnClickListener(this);

		// mode click event
		mBtnDailyMode.setOnClickListener(this);
		mBtnSleepMode.setOnClickListener(this);
		mBtnWorkMode.setOnClickListener(this);
		mBtnMusicMode.setOnClickListener(this);
		mBtnLoveMode.setOnClickListener(this);
		mBtnPartyMode.setOnClickListener(this);
		mBtnRestMode.setOnClickListener(this);

	}

	private void initView() {
		// TODO Auto-generated method stu
		mTabSetParam = (LinearLayout) findViewById(R.id.id_control);
		mTabSetTimer = (LinearLayout) findViewById(R.id.id_timer);

		mBtnSetParam = (ImageButton) findViewById(R.id.id_tab_control_btn);
		mBtnSetTimer = (ImageButton) findViewById(R.id.id_tab_timer_btn);
		mBtnSetupMode = (ImageButton) findViewById(R.id.img_btn_setup_mode);
		mBtnFindDevice = (ImageButton) findViewById(R.id.img_btn_find_device);

		mBtnDailyMode = (ImageButton) findViewById(R.id.img_btn_daily);
		mBtnSleepMode = (ImageButton) findViewById(R.id.img_btn_sleep);
		mBtnWorkMode = (ImageButton) findViewById(R.id.img_btn_work);
		mBtnMusicMode = (ImageButton) findViewById(R.id.img_btn_music);
		mBtnLoveMode = (ImageButton) findViewById(R.id.img_btn_love);
		mBtnPartyMode = (ImageButton) findViewById(R.id.img_btn_party);
		mBtnRestMode = (ImageButton) findViewById(R.id.img_btn_rest);

		left_menu = (SildeMenu) findViewById(R.id.id_left_menu);
	}

	@Override
	public void onClick(View imgBtn) {
		// TODO Auto-generated method stub
		reSetBackGround();
		switch (imgBtn.getId()) {
		case R.id.id_tab_control_btn:
			selectFragment(0);
			break;
		case R.id.id_tab_timer_btn:
			selectFragment(1);
			break;
		case R.id.img_btn_setup_mode:
			left_menu.toggle();
			break;
		case R.id.img_btn_find_device:
			Intent findDeviceIntent = new Intent(MainActivity.this,
					FindBluetoothDevices.class);
			startActivityForResult(findDeviceIntent,
					REQUEST_CONNECT_DEVICE_ADDRESS);
			break;

		case R.id.img_btn_daily:
			// 日常 模式 Intensity: 60; Color: #ffffff
			setLightMode("dailyMode");
			break;
		case R.id.img_btn_sleep:
			// 睡觉模式 Intensity: 35; Color: #f5fca4
			setLightMode("sleepMode");
			break;
		case R.id.img_btn_work:
			// 工作模式 Intensity: 75; Color: #cffffb
			setLightMode("workMode");
			break;
		case R.id.img_btn_party:
			setLightMode("partyMode");
			break;
		case R.id.img_btn_rest:
			// 休息模式 Intensity: 50; Color: #ffcd76
			setLightMode("restMode");
			break;
		case R.id.img_btn_love:
			// 睡觉模式 Intensity: 40; Color: #ffcccc
			setLightMode("loveMode");
			break;
		case R.id.img_btn_music:
			// 音乐模式 Intensity: 60; Color: #8cff5f
			setLightMode("musicMode");
			break;
		default:
			break;
		}

	}

	private void setLightMode(String mode) {
		Intent getTargetBulbIntent = new Intent(MainActivity.this,
				GetTargetLightActivity.class);
		// getTargetBulbIntent.putExtra("ModeIntensity", intensity);
		// getTargetBulbIntent.putExtra("ModeColor", color);
		getTargetBulbIntent.putExtra("ModeType", mode);
		startActivity(getTargetBulbIntent);
	}

	/**
	 * Send a message
	 */
	private void sendMessage(String message) {
		// if (mBluetoothThread.getState() != BlueToothThread.STATE_CONNECTED) {
		// Toast.makeText(this, "Not Connected, can't send message",
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// if(message.length()>0){
		// byte[] send = message.getBytes();
		// mBluetoothThread.write(send);
		// }
	}

	/**
	 * 移除所Tab的背景
	 */

	private void reSetBackGround() {
		// TODO Auto-generated method stub
		mTabSetParam.setBackgroundResource(0);
		mTabSetTimer.setBackgroundResource(0);

	}

	private void selectFragment(int i) {
		android.support.v4.app.FragmentTransaction fmTransaction = fm
				.beginTransaction();

		hideFragment(fmTransaction);
		switch (i) {
		case 0:
			if (msetParamFragment == null) {

				msetParamFragment = new FragmentSetPara();
				// msetParamFragment.setListAdapter(mConnectedDevicesArrayAdapter);

				fmTransaction.add(R.id.id_main_fragment_content,
						msetParamFragment);

			} else {
				fmTransaction.show(msetParamFragment);
			}
			mTabSetParam.setBackgroundResource(R.drawable.hover_button);
			break;
		case 1:
			if (msetTimerFragment == null) {
				msetTimerFragment = new FragmentSetTimer();
				fmTransaction.add(R.id.id_main_fragment_content,
						msetTimerFragment);
			} else {
				fmTransaction.show(msetTimerFragment);
			}
			mTabSetTimer.setBackgroundResource(R.drawable.hover_button);
			break;

		default:
			break;
		}
		fmTransaction.commit();

	}

	private void hideFragment(
			android.support.v4.app.FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if (msetParamFragment != null) {
			transaction.hide(msetParamFragment);
		}
		if (msetTimerFragment != null) {
			transaction.hide(msetTimerFragment);
		}
	}

	private final void setStatus(String subTitle) {

	}

	// The Handler that gets information from BluetoothThread
	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BlueToothThread.STATE_CONNECTED:
					setStatus("Connected");
					break;
				case BlueToothThread.STATE_CONNECTING:
					setStatus("Connecting .....");
					break;
				case BlueToothThread.STATE_LISTEN:
				case BlueToothThread.STATE_NONE:
					setStatus("Not Available");
					break;
				default:
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				String writeMessage = new String(writeBuf);
				// Toast.makeText(getApplicationContext(),
				// "send :" + writeMessage, Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				String readMessage = new String(readBuf, 0, msg.arg1);
				Toast.makeText(getApplicationContext(),
						"receive :" + readMessage, Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_DEVICE_NAME:
				String mConnectedDeviceName = msg.getData().getString(
						DEVICE_NAME);
				msetParamFragment.adapter.add(mConnectedDeviceName);
				Toast.makeText(getApplicationContext(),
						"connected to :" + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
			default:
				break;
			}
		}

	};

}
