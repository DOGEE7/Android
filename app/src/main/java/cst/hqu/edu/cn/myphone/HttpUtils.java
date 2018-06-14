package cst.hqu.edu.cn.myphone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static cst.hqu.edu.cn.myphone.Chat.POST_MSG;

/**
 * Created by ASUS on 2018/6/2.
 */

public class HttpUtils {
    /**
     *与服务器建立连接
     */
    //public static final int POST_MSG=0x112;
    private Handler handler;
    private static String url="http://openapi.tuling123.com/openapi/api/v2";
    private JSONObject jsonObject;

    public HttpUtils(Handler handler,JSONObject jsonObject){
        this.handler=handler;
        this.jsonObject=jsonObject;
    }
    /*



    * 发送请求并获得json数据
    * */
    public  void sendOkHttpRequest(){
        OkHttpClient client=new OkHttpClient();
        final MediaType JSON=MediaType.parse("application/json");
        String json=String.valueOf(jsonObject);
        RequestBody requestBody=RequestBody.create(JSON,json);
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("Callback:","Fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();

                Log.i("OnResponse:",responseData);
                final Bundle bundle=new Bundle();
                bundle.putString("response",responseData);//bundle类中加入数据，在另一个activity中利用key取用数据
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message=handler.obtainMessage(POST_MSG);
                       *//* Message message=Message.obtain();
                        message.what=POST_MSG;*//*
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                }).start();*/
                Message message=Message.obtain();
                message.what=POST_MSG;
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }
        );
    }

/*
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private  OkHttpClient client = new OkHttpClient();
    String json=String.valueOf(jsonObject);

    public  Response post() throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public  Response get() throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String responseData=response.body().string();
        final Bundle bundle=new Bundle();
        bundle.putString("response",responseData);//bundle类中加入数据，在另一个activity中利用key取用数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=Message.obtain();
                message.what=POST_MSG;
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
        return response;
    }*/


}
