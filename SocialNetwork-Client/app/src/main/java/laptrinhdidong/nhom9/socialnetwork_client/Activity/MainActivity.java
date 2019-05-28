package laptrinhdidong.nhom9.socialnetwork_client.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import laptrinhdidong.nhom9.socialnetwork_client.Common.Const;
import laptrinhdidong.nhom9.socialnetwork_client.R;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private EditText nickname;
    private EditText ipserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //call UI components  by id
        btn = (Button)findViewById(R.id.enterchat) ;
        nickname = (EditText) findViewById(R.id.nickname);
        ipserver = (EditText) findViewById(R.id.ipserver);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the nickname is not empty go to chatbox activity and add the nickname to the intent extra
                if(!nickname.getText().toString().isEmpty()){

                    Intent i  = new Intent(MainActivity.this,ChatBoxActivity.class);

                    //retreive nickname from EditText and add it to intent extra
                    i.putExtra(Const.NICKNAME,nickname.getText().toString());
                    i.putExtra(Const.IPSERVER,ipserver.getText().toString());

                    startActivity(i);
                }
            }
        });
    }
}
