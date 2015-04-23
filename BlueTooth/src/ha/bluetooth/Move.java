package ha.bluetooth;

import java.io.PrintWriter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.Touch;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class Move extends Activity {

    private ImageButton IbtForward;
    private ImageButton IbtBackward;
    private ImageButton IbtLeft;
    private ImageButton IbtRight;
    private ImageButton IbtStop;

    private String msg;

    private PrintWriter _printWriter = MainActivity.getPrintWriter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move);

        buildViews();
    }

    private void buildViews() {
        // TODO Auto-generated method stub

        IbtForward = (ImageButton) findViewById(R.id.imageButtonForward);
        IbtForward.setOnTouchListener(btOnTouchListener);

        IbtBackward = (ImageButton) findViewById(R.id.imageButtonBackward);
        IbtBackward.setOnTouchListener(btOnTouchListener);

        IbtLeft = (ImageButton) findViewById(R.id.imageButtonLeft);
        IbtLeft.setOnTouchListener(btOnTouchListener);

        IbtRight = (ImageButton) findViewById(R.id.imageButtonRight);
        IbtRight.setOnTouchListener(btOnTouchListener);
        
        IbtStop = (ImageButton) findViewById(R.id.imageButtonStop);
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
                case R.id.imageButtonForward:
                    System.out.println("f");
                    msg = "f";
                    break;
                case R.id.imageButtonBackward:
                    System.out.println("k");
                    msg = "k";
                    break;
                case R.id.imageButtonLeft:
                    System.out.println("l");
                    msg = "l";
                    break;
                case R.id.imageButtonRight:
                    System.out.println("r");
                    msg = "r";
                    break;
                case R.id.imageButtonStop:
                    System.out.println("s");
                    msg = "s";
                default:
                    break;
                }
                
            }
            else if (event.getAction() == KeyEvent.ACTION_UP){
                System.out.println("s");
                msg = "s";
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

    private void sendMsg() {
        // TODO Auto-generated method stub
        _printWriter.println(msg);
        _printWriter.flush();
    }
}
