package cst.hqu.edu.cn.myphone;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {
    private EditText inputText;
    private Button send;
    private Button voice;
    private RecyclerView msgRecyclerView;

    private List<Msg> msgList = new ArrayList<>();
    private MsgAdapter adapter;

    private String speechResult;

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
        /*智能聊天*/
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

        /*语音识别*/
        //语音配置对象初始化
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5b1a871a");
        voice=(Button)findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // voice.setBackgroundResource(R.mipmap.vimg_after);
                switch (v.getId()){
                    case R.id.voice:
                        speechResult="[";
//                      1.创建SpeechRecognizer对象，第2个参数：本地听写时传InitListener
                        SpeechRecognizer mIat=SpeechRecognizer.createRecognizer(Chat.this,null);
//                      2.交互动画
                        RecognizerDialog iatDialog=new RecognizerDialog(Chat.this,null);
//                      3.设置听写参数
                        mIat.setParameter(SpeechConstant.DOMAIN,"iat");
                        mIat.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
                        mIat.setParameter(SpeechConstant.ACCENT,"mandrin");

                        iatDialog.setListener(new RecognizerDialogListener() {
                            @Override
                            public void onResult(RecognizerResult recognizerResult, boolean isLs) {
                                if (!isLs){
                                    speechResult+=recognizerResult.getResultString()+",";
                                }else {
                                    speechResult+=recognizerResult.getResultString()+"]";
                                }
                                if (isLs){
//                                    解析Json列表字符串
                                    Gson gson=new Gson();
                                    List<SpeechUtility>speechUtilityList=gson.fromJson(speechResult,new TypeToken<List<SpeechUtility>>(){}.getType());
                                    String finalResult="";
                                    for (int i=0;i<speechUtilityList.size();i++){
                                        finalResult+=speechUtilityList.get(i).toString();
                                    }
                                    inputText.setText(finalResult);
                                    inputText.requestFocus();
                                    inputText.setSelection(finalResult.length());
                                    Log.i("Result：",finalResult);

                                }
                            }

                            @Override
                            public void onError(SpeechError speechError) {
                                speechError.getPlainDescription(true);

                            }
                        });
                        //开始听写
                        iatDialog.show();
                        break;
                    default:
                        break;
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
