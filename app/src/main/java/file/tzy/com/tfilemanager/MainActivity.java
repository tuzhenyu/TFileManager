package file.tzy.com.tfilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import file.tzy.com.tfilemanager.manager.FileBrowserActivity;

public class MainActivity extends Activity implements View.OnClickListener{


    Button bn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bn = (Button) findViewById(R.id.bn);
        bn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bn:
                Intent intent = new Intent(this, FileBrowserActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }
}
