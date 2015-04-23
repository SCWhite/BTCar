package ha.bluetooth;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class MemoryPath extends Activity {
    
    private Switch swStartRecord;
    private Switch swStartClean;
    
    private Boolean remember = null;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_path);

        buildViews();
    }

    private void buildViews() {
        // TODO Auto-generated method stub
        swStartRecord = (Switch)findViewById(R.id.swIdStartRemember);
        swStartRecord.setOnCheckedChangeListener(swListener);
    }
    
    private OnCheckedChangeListener swListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            // TODO Auto-generated method stub
            if(isChecked){  
                System.out.println("open");
                remember = true;
            }
            else {
                System.out.println("closed");  
                remember = false;
            }
             
        }

    
    };
}
