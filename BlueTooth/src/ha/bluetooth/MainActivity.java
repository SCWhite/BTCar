package ha.bluetooth;

import java.io.IOException; 
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;



public class MainActivity extends Activity {
	private Button btClick_Check;
	private Button btClick_CheckOpen;
	private Button btClick_Close;
	private Button btClick_CanBeFound;
    private Button btClick_ConnectBefore;
    private Button btClick_SendMsg;
    
    private BluetoothSocket socket;
    private static PrintWriter _printWriter = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        
        buildViews();
    }

	private void buildViews() {
		// TODO Auto-generated method stub		
		btClick_Check = (Button)findViewById(R.id.btIdBTCheck);
		btClick_Check.setOnClickListener(btListener);
		
		btClick_CheckOpen = (Button)findViewById(R.id.btIdBTCheckOpen);
		btClick_CheckOpen.setOnClickListener(btListener);
		
		btClick_Close = (Button)findViewById(R.id.btIdBTClose);
		btClick_Close.setOnClickListener(btListener);
		
		btClick_CanBeFound = (Button)findViewById(R.id.btIdBTCanBeFound);
        btClick_CanBeFound.setOnClickListener(btListener);
        
		btClick_ConnectBefore = (Button)findViewById(R.id.btIdBTConnectBefore);
		btClick_ConnectBefore.setOnClickListener(btListener);
		
		btClick_SendMsg = (Button)findViewById(R.id.btIdBTSendMsg);
        btClick_SendMsg.setOnClickListener(btListener);
	}
	
	private OnClickListener btListener = new OnClickListener() {		


        public void onClick(View v) {
			// TODO Auto-generated method stub
			String outString = new String();
			BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
			switch(v.getId()) {
				case R.id.btIdBTCheck:	
					if (bluetooth.equals(null)) {
						outString = "NULL";
					}
					else {
						outString = bluetooth.toString();
					}
					break;
				case R.id.btIdBTCheckOpen:
					if (bluetooth.isEnabled()){						 
						outString = "藍芽已經開啟";
					}
					else {
						//Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
						//startActivity(enabler);
					    bluetooth.enable();
						outString = "你好";
					}
					break;
				case R.id.btIdBTClose:
					//Intent enabler = new Intent(BluetoothAdapter.)
					if (bluetooth.isEnabled()){						 
					    bluetooth.disable();
						outString = "關閉藍芽了"; 
					}
					else {
						outString = "藍芽未開啟";
					}
					break;
				case R.id.btIdBTCanBeFound:
					if (bluetooth.isEnabled()) {
						//blue.disable();
						Intent enabler=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					    startActivity(enabler);
					}
					else {
						outString = "藍芽未開啟";
					}
					break;					
				case R.id.btIdBTConnectBefore:
				    if (bluetooth.isEnabled()) {
				        Set deviceHistory = bluetooth.getBondedDevices();
	                    BluetoothDevice device = (BluetoothDevice) deviceHistory.iterator().next();
	                    System.out.println(device.getBondState());
	                    
	                    try {
	                        connect(device);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
	 
	                    System.out.println(device.getName());
	                    outString = "已連接到" + device.getName();
	                    
	                    Intent intent = new Intent();
	                    intent.setClass(MainActivity.this, ControlMenu.class);
	                    //intent.putExtra(Intent.EXTRA_STREAM,MainActivity.getPrintWriter());
	                    startActivity(intent);
                    }
                    else {
                        outString = "藍芽未開啟";
                    }                 
                    break;
				case R.id.btIdBTSendMsg:       
                    break;
			}
			
			Toast.makeText(
					MainActivity.this,
					outString,
					Toast.LENGTH_SHORT)
					.show();				
		}
        
        protected void connect(BluetoothDevice device) throws IOException {
            // TODO Auto-generated method stub
            final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB"; 
            UUID uuid = UUID.fromString(SPP_UUID);            
            
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            System.out.println("SocketConnect:" + socket.isConnected());
           
            _printWriter = new PrintWriter(socket.getOutputStream(), true);
            
            
        }
        
        private void sendMsg(String msg) {
            // TODO Auto-generated method stub
            _printWriter.println(msg);
            _printWriter.flush();
        }
        
	};

	public static PrintWriter getPrintWriter () {
        return  _printWriter;
    }
	
}
