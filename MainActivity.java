package com.example.socketclient;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Delayed;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageView imageView = null;
	private Bitmap bmp = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		imageView = (ImageView) findViewById(R.id.ivImage1);
		Button btn = (Button) findViewById(R.id.btIdBTCheck);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Thread t = new Thread() {
					@Override
					public void run() {
						super.run();
						Socket socket = null;
						try {
							socket = new Socket("172.21.111.49", 12345);
							DataInputStream dataInput = new DataInputStream(
									socket.getInputStream());
							int size = dataInput.readInt();
							byte[] data = new byte[size];
							// dataInput.readFully(data);
							int len = 0;
							while(true) {
								
								while (len < size) {
									len += dataInput.read(data, len, size - len);
									System.out.println(len);
								}
	
								ByteArrayOutputStream outPut = new ByteArrayOutputStream();
								bmp = BitmapFactory.decodeByteArray(data, 0,
										data.length);
								bmp.compress(CompressFormat.JPEG, 0, outPut);
								
								myHandler.obtainMessage().sendToTarget();
								dataInput = new DataInputStream(
										socket.getInputStream());
								//size = dataInput.readInt();
								len= 0;
							}							
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				t.start();
			}
		});
	}
	
	private Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			imageView.setImageBitmap(bmp);
		};
	};
}