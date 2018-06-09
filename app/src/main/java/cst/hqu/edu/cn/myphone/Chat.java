package cst.hqu.edu.cn.myphone;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Chat extends AppCompatActivity {
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;

    private List<Msg> msgList = new ArrayList<>();
    private MsgAdapter adapter;

    /*private String[] keys;
    private String[] values;
    private String text;
    private String url;*/

    public static final int  POST_MSG=0x112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter( msgList);
        msgRecyclerView.setAdapter(adapter);
        initMsgs();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1);//当有消息时，刷新RecyclerView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);//将RecyclerView定位到最后一行
                    inputText.setText("");
                    JSONObject jsonObject=Utility.jsonPost(content);
                    HttpUtils httpUtils=new HttpUtils(handler,jsonObject);
                    httpUtils.sendOkHttpRequest();
                    /*try {
                        httpUtils.post();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        httpUtils.get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        });
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==POST_MSG){
                String response=msg.getData().getString("response");
                /*Bundle bundle=msg.getData();
                String response=bundle.getString("response");*/
                response=Utility.jsonResponse(response);
                Log.i("handleMessage:",response);
                Msg receivedMsg=new Msg(response,Msg.TYPE_RECEIVED);
                msgList.add(receivedMsg);
                adapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
            }
        }
    };

    private void initMsgs() {
        Msg msg1 = new Msg("Hello!", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello! Who are you?", Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("I am Dingdang! Nice talking to you.", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }
}
