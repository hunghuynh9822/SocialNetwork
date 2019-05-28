package laptrinhdidong.nhom9.socialnetwork_client.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import laptrinhdidong.nhom9.socialnetwork_client.Adapter.ChatBoxAdapter;
import laptrinhdidong.nhom9.socialnetwork_client.Common.Const;
import laptrinhdidong.nhom9.socialnetwork_client.Model.Message;
import laptrinhdidong.nhom9.socialnetwork_client.R;


public class ChatBoxActivity extends AppCompatActivity {

    //declare socket object
    private Socket socket;
    private String nickname ;
    private String ipserver ;

    public RecyclerView myRecylerView ;
    public List<Message> MessageList ;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messagetxt ;
    public Button send ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        // get the nickame of the user

        nickname = (String)getIntent().getExtras().getString(Const.NICKNAME);
        ipserver = (String)getIntent().getExtras().getString(Const.IPSERVER);

        InitSocketIO();
        Init();
        SetEvent();
        SetUpListenerSocket();
    }

    private void InitSocketIO(){
        //connect you socket client to the server
        try {
            //if you are using a phone device you should connect to same local network as your laptop and disable your pubic firewall as well
            socket = IO.socket("http://"+ipserver+":3000");
            //create connection
            socket.connect();
            // emit the event join along side with the nickname
            socket.emit("join",nickname);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void SetUpListenerSocket(){
        socket.on("userjoinedthechat", onUserJoinChatBox);
        socket.on("message", onRetrieveMessage);
        socket.on("userdisconnect", onUserDisconnect);
    }

    private Emitter.Listener onUserJoinChatBox = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];
                    // get the extra data from the fired event and display a toast
                    Toast.makeText(ChatBoxActivity.this, data, Toast.LENGTH_SHORT).show();

                }
            });
        }
    };

    private Emitter.Listener onRetrieveMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        //extract data from fired event
                        String nickname = data.getString("senderNickname");
                        String message = data.getString("message");
                        // make instance of message
                        Message m = new Message(nickname,message);
                        //add the message to the messageList
                        MessageList.add(m);
                        // add the new updated list to the adapter
                        chatBoxAdapter = new ChatBoxAdapter(MessageList);
                        // notify the adapter to update the recycler view
                        chatBoxAdapter.notifyDataSetChanged();
                        //set the adapter for the recycler view
                        myRecylerView.setAdapter(chatBoxAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onUserDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];
                    Toast.makeText(ChatBoxActivity.this,data,Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    private void Init(){
        messagetxt = (EditText) findViewById(R.id.message) ;
        send = (Button)findViewById(R.id.send);
        MessageList = new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());


    }

    private void SetEvent(){
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                if(!messagetxt.getText().toString().isEmpty()){
                    socket.emit("messagedetection",nickname,messagetxt.getText().toString());
                    messagetxt.setText(" ");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}
