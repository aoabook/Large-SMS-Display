package com.wiley.aoa.lsmsd;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.wiley.wroxaccessories.UsbConnection12;
import com.wiley.wroxaccessories.WroxAccessory;

public class LSMSDService extends Service {

	WroxAccessory mAccessory;
	UsbConnection12 mConnection;
	UsbManager mUsbManager;

	IntentFilter smsFilter;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mUsbManager = (UsbManager) getSystemService(USB_SERVICE);
		mAccessory = new WroxAccessory(this);
		mConnection = new UsbConnection12(this, mUsbManager);

		try {
			mAccessory.connect(WroxAccessory.USB_ACCESSORY_12, mConnection);

			smsFilter = new IntentFilter();
			smsFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
			registerReceiver(mReceiver, smsFilter);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			mAccessory.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle pudsBundle = intent.getExtras();
			Object[] pdus = (Object[]) pudsBundle.get("pdus");
			SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
			Log.i("LSMSD", messages.getMessageBody());

			try {
				mAccessory.publish("sms", messages.getMessageBody().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
}
