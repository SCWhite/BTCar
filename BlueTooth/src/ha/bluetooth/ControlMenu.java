package ha.bluetooth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ControlMenu extends Activity {

    private Button btClick_FreeMode;
    private Button btClick_MemoryPath;
    private Button btClick_Random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_menu);

        buildViews();
    }

    private void buildViews() {
        // TODO Auto-generated method stub
        btClick_FreeMode = (Button) findViewById(R.id.btIdBTFreeMode);
        btClick_FreeMode.setOnClickListener(btListener);

        btClick_MemoryPath = (Button) findViewById(R.id.btIdBTMemoryPath);
        btClick_MemoryPath.setOnClickListener(btListener);

        btClick_Random = (Button) findViewById(R.id.btIdBTRandom);
        btClick_Random.setOnClickListener(btListener);
    }

    private OnClickListener btListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.btIdBTFreeMode:
                Intent intentFreeMode = new Intent();
                intentFreeMode.setClass(ControlMenu.this, Move.class);
                startActivity(intentFreeMode);
                break;
            case R.id.btIdBTMemoryPath:
                Intent intentMemoryPath = new Intent();
                intentMemoryPath.setClass(ControlMenu.this, MemoryPath.class);
                startActivity(intentMemoryPath);    
                break;
            case R.id.btIdBTRandom:

                break;
            default:
                break;
            }
        }

    };
}
