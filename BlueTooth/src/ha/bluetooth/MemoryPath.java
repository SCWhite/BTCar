package ha.bluetooth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Switch;

public class MemoryPath extends Activity {

    private Switch swStartRecord;
    private Switch swStartClean;

    private Boolean remember = null;
    private Boolean move = null;

    private static Context context;
    private File dir;
    private File outFile;
    private FileReader inFile;

    private ImageButton IbtForward;
    private ImageButton IbtBackward;
    private ImageButton IbtLeft;
    private ImageButton IbtRight;
    private ImageButton IbtStop;

    private char msg;
    
    private char data[] = new char[128];
    private int index = 0; 
    private int Maxdata = 0;

    private PrintWriter _printWriter = MainActivity.getPrintWriter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_path);

        context = getApplicationContext();

        buildViews();
    }

    private void buildViews() {
        // TODO Auto-generated method stub
        
        swStartRecord = (Switch) findViewById(R.id.swIdStartRemember);
        swStartRecord.setOnCheckedChangeListener(swListener);
        
        swStartClean = (Switch) findViewById(R.id.swIdStratMove);
        swStartClean.setOnCheckedChangeListener(swListener);

        
        IbtForward = (ImageButton) findViewById(R.id.imageButtonForwardRecord);
        IbtForward.setOnTouchListener(btOnTouchListener);

        IbtBackward = (ImageButton) findViewById(R.id.imageButtonBackwardRecord);
        IbtBackward.setOnTouchListener(btOnTouchListener);

        IbtLeft = (ImageButton) findViewById(R.id.imageButtonLeftRecord);
        IbtLeft.setOnTouchListener(btOnTouchListener);

        IbtRight = (ImageButton) findViewById(R.id.imageButtonRightRecord);
        IbtRight.setOnTouchListener(btOnTouchListener);

        IbtStop = (ImageButton) findViewById(R.id.imageButtonStopRecord);
        IbtStop.setOnTouchListener(btOnTouchListener);
    }

    private OnTouchListener btOnTouchListener = new OnTouchListener() {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            System.out.println("ontouch");          
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (v.getId()) {
                case R.id.imageButtonForwardRecord:
                    System.out.println("f");
                    msg = 'f';
                    break;
                case R.id.imageButtonBackwardRecord:
                    System.out.println("k");
                    msg = 'k';
                    break;
                case R.id.imageButtonLeftRecord:
                    System.out.println("l");
                    msg = 'l';
                    break;
                case R.id.imageButtonRightRecord:
                    System.out.println("r");
                    msg = 'r';
                    break;
                case R.id.imageButtonStopRecord:
                    System.out.println("s");
                    msg = 's';
                default:
                    break;
                }

            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                System.out.println("s");
                msg = 's';
            }

            if (remember) {
                data[index] = msg;
                index++;
                //writeFile(outFile, msg);
            } else if (move) {
                msg = data[index];
                if (index < Maxdata)
                    index++;
            }
            try {
                sendMsg();
                Thread.sleep(100);
            } catch (Exception e) {
                // TODO: handle exception
            }

            return false;
        }

        

    };

    private OnCheckedChangeListener swListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            dir = context.getFilesDir();
            System.out.println(dir);
            switch (buttonView.getId()) {
            case R.id.swIdStartRemember:
                if (isChecked) {
                    outFile = new File(dir, "record.txt");
                    System.out.println("R open");
                    remember = true;

                } else {
                    System.out.println("R closed");
                    Maxdata = index;
                    index = 0;
                    remember = false;
                    writeFile(outFile, data);
                }
                break;
            case R.id.swIdStratMove:
                try {
                    inFile = new FileReader(dir + "record.txt");
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (isChecked) {
                    System.out.println("M open");
                    move = true;
                    while (index < Maxdata) {
                        try {
                            msg = data[index];
                            index++;
                            sendMsg();
                            Thread.sleep(100);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        
                    }                 
                } else {
                    System.out.println("M closed");
                    move = false;
                    index = 0;
                }
                break;
            default:
                break;
            }

            // TODO Auto-generated method stub

        }

    };

    public static Context getContext() {
        return context;
    }

    private void sendMsg() {
        // TODO Auto-generated method stub
        _printWriter.println(msg);
        _printWriter.flush();
    }
    
    private char[] readFile(FileReader inFile) throws IOException {
        // TODO Auto-generated method stub
        char data [] = new char [128];
   
        Maxdata = inFile.read(data);
         
        inFile.close();
        return data;
    }

    private void writeFile(File outFile, char[] data) {
        // TODO Auto-generated method stub
        FileOutputStream _fileOutputStream = null;
        try {
            _fileOutputStream = new FileOutputStream(outFile);
            _fileOutputStream.write(data.toString().getBytes());
            _fileOutputStream.flush();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            try {
                _fileOutputStream.close();
            } catch (Exception e) {
                ;
            }
        }
    }
}
