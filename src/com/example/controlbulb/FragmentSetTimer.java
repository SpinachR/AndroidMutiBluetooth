package com.example.controlbulb;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FragmentSetTimer extends Fragment {

	private EditText hourEdit;
	private EditText minuteEdit;
	private EditText secondEdit;

	private Button setBtn;

	private int hour = 0;
	private int minute = 0;
	private int second = 0;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.tab_set_timer, container, false);

		hourEdit = (EditText) v.findViewById(R.id.hour);
		minuteEdit = (EditText) v.findViewById(R.id.minute);
		secondEdit = (EditText) v.findViewById(R.id.second);
		setBtn = (Button) v.findViewById(R.id.btn_settimer);

		return v;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		setBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String hourText = hourEdit.getText().toString().trim();
				String minuteText = minuteEdit.getText().toString().trim();
				String secondText = secondEdit.getText().toString().trim();
				textTime(hourText, minuteText, secondText);

				int time = (hour * 3600 + minute * 60 + second) * 1000;
				Intent setTimerIntent = new Intent(getActivity(),
						GetTargetLightActivity.class);

				setTimerIntent.putExtra("setTime", "setTime /" + time + " /");
				startActivity(setTimerIntent);
			}
		});

	}

	private void textTime(String hourText, String minuteText, String secondText) {
		// TODO Auto-generated method stub
		try {
			if (hourText.equals("")) {
				hour = 0;
			} else {
				hour = Integer.parseInt(hourText);
			}

			if (minuteText.equals("")) {
				minute = 0;
			} else {
				minute = Integer.parseInt(minuteText);
			}

			if (secondText.equals("")) {
				second = 0;
			} else {
				second = Integer.parseInt(secondText);
			}
		} catch (NumberFormatException e) {
			Toast.makeText(getActivity(), "Number Error", Toast.LENGTH_SHORT)
					.show();
		}

	}
}
