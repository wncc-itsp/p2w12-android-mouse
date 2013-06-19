package com.itsp.btmouse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
public class MainActivity extends Activity implements View.OnClickListener {
	// Debugging
	private static final String TAG = "BTMouse";
	private static final boolean D = true;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	// Layout Views
	private TextView mTitle;
	private ListView mConversationView;
	private EditText mOutEditText;
	private Button mSendButton;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	ConnectThread obj;
	
	Button RClick, LClick, ScrollDown, ScrollUp, bConnect;
	TextView stats;
	MediaPlayer clicksound;
	Accelerate use;
	SensorManager sm;
	float x, y;
	
	ConnectedThread thr1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");

		setContentView(R.layout.activity_main);
		initialize();
		clicksound.start();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		use = new Accelerate();

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

	private void initialize() {
		// TODO Auto-generated method stub
		RClick = (Button) findViewById(R.id.bRClick);
		LClick = (Button) findViewById(R.id.bLClick);
		ScrollUp = (Button) findViewById(R.id.bScrollUp);
		ScrollDown = (Button) findViewById(R.id.bScrollDown);
		bConnect = (Button) findViewById(R.id.bConnect);
		stats = (TextView) findViewById(R.id.tvStats);
		bConnect.setOnClickListener(this);
		clicksound = MediaPlayer.create(this, R.raw.clickmouse);
		x = y = 0;

	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bConnect:
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			break;
		case R.id.bLClick:
			clicksound.start();
			
				byte[] temp = new byte[] {'l'};
            	thr1.write(temp);
			
			break;
		case R.id.bRClick:
			clicksound.start();
			
				byte[] temp1 = new byte[] {'r'};
            	thr1.write(temp1);
			
			break;
		}
	}
		
	@Override
	public synchronized void onPause() {
		super.onPause();
		use.pause();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				Toast.makeText(this,
						"Found Some Device to Connect!! " + address,
						Toast.LENGTH_SHORT).show();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				obj = new ConnectThread(device);
				Toast.makeText(this,
						"Completed Execution of Connect Thread Constructor.",
						Toast.LENGTH_SHORT).show();
				// Attempt to connect to the device
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				obj.start();
				Toast.makeText(this,
						"Completed Execution of Connect Thread Run.",
						Toast.LENGTH_SHORT).show();

				}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				
				Toast.makeText(this, "Bluetooth Enabled", Toast.LENGTH_SHORT)
						.show();

			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	public class Accelerate implements SensorEventListener {

		Accelerate() {
			sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		}

		public void resume() {
			if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
				Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
				sm.registerListener(this, s, SensorManager.SENSOR_DELAY_GAME);
			}

		}

		public void pause() {
			// TODO Auto-generated method stub
			sm.unregisterListener(this);
		}

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			x = event.values[0];
			y = event.values[1];
			stats.setText("Sensor stats are X= " + x + " and Y = " + y);
			byte[] temp;
            String tstr = "(" + String.valueOf(x) + "x" + String.valueOf(y) + ")" +":" + String.valueOf(2);
            
            temp= tstr.getBytes();
            thr1.write(temp);
		}

	}

	public class ConnectThread extends Thread {
		private final UUID MY_UUID = UUID
				.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		

		public ConnectThread(BluetoothDevice device) {

			BluetoothSocket tmp = null;
			mmDevice = device;
			BluetoothDevice otherDevice = BluetoothAdapter.getDefaultAdapter()
					.getRemoteDevice(device.getAddress());

			Method m;
			try {

				m = otherDevice.getClass().getMethod("createRfcommSocket",
						new Class[] { int.class });
				Log.i("TRY!!", "This try happened");
				tmp = (BluetoothSocket) m.invoke(otherDevice,
						Integer.valueOf(1));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.i("Socket operation", "socket's up!!!!");
			Log.i("socket info", tmp.toString());

			mmSocket = tmp;

		}

		public void run() {

			//connectStateChanger.setText("Will Try To Connect");
			Log.i("socket operation", "successfully started");
			// Cancel discovery because it will slow down the connection
			BluetoothAdapter btadapter = BluetoothAdapter.getDefaultAdapter();
			btadapter.cancelDiscovery();

			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception
				// connectStateChanger.setText("Will now try to connect");
				mmSocket.connect();
				Log.i("socket operation", "trying to connect");
				// BlumouseActivity.GiveConnectedSocket(mmSocket);
			} catch (IOException connectException) {
				// Unable to connect; close the socket and get out
				Log.i("socket operation", "caught ioexception");
				Log.i("Message", connectException.getMessage());
				Log.i("LMessage", connectException.getLocalizedMessage());
				try {
					mmSocket.close();
				} catch (IOException closeException) {
				}
				return;
			}

			// Do work to manage the connection (in a separate thread)
			Log.i("socket operation", "completed connect thread");
			
			thr1 = new ConnectedThread(mmSocket);
			thr1.start();
			String init = "Connection Started :)";

			byte[] bytearray = init.getBytes();
			thr1.write(bytearray);
			
			use.resume();
			LClick.setOnClickListener(MainActivity.this);
			RClick.setOnClickListener(MainActivity.this);
			
		}

		// Will cancel an in-progress connection, and close the socket
		public void cancel() {
			try {
				Log.i("socket operation", "closing socket");
				mmSocket.close();
			} catch (IOException e) {
			}
		}

	}

	
	

	
}