package com.wiley.aoa.lsmsd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void startService(View v){
		toggleService(true);
	}
	
	public void stopService(View v){
		toggleService(false);
	}

	private void toggleService(boolean status) {
		Intent service = new Intent(MainActivity.this, LSMSDService.class);
		if (status)
			startService(service);
		else
			stopService(service);
	}
}
