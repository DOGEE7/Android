package cst.hqu.edu.cn.myphone;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ASUS on 2018/6/3.
 * 图灵机器人API
 * APIkey:a06b4815364e4d0392bee6b333fc6ca3
 * 秘钥： 916441cdb9b7e1eb
 *接口：http://openapi.tuling123.com/openapi/api/v2
 */

public class Utility {
    /***
     * 解析和处理服务器返回的数据
     * */

    /*
    * {
	"reqType":0,
    "perception": {
        "inputText": {
            "text": "附近的酒店"
        },
        "inputImage": {
            "url": "imageUrl"
        },
        "selfInfo": {
            "location": {
                "city": "北京",
                "province": "北京",
                "street": "信息路"
            }
        }
    },
    "userInfo": {
        "apiKey": "",
        "userId": ""
    }
}
将数据包装成json
    * */
    public static JSONObject jsonPost(String post){
        JSONObject jsonObject=new JSONObject();
        try {
            //jsonObject.put("reqType",0);

            JSONObject perception=new JSONObject();
            JSONObject inputText=new JSONObject();
            inputText.put("text",post);
            perception.put("inputText",inputText);
            jsonObject.put("perception",perception);

            JSONObject userInfo=new JSONObject();
            userInfo.put("apiKey","a06b4815364e4d0392bee6b333fc6ca3");
            userInfo.put("userId","0");
            jsonObject.put("userInfo",userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /*
    *  {
    "intent": {
        "code": 10005,
        "intentName": "",
        "actionName": "",
        "parameters": {
            "nearby_place": "酒店"
        }
    },
    "results": [
        {
         	"groupType": 1,
            "resultType": "url",
            "values": {
                "url": "http://m.elong.com/hotel/0101/nlist/#indate=2016-12-10&outdate=2016-12-11&keywords=%E4%BF%A1%E6%81%AF%E8%B7%AF"
            }
        },
        {
         	"groupType": 1,
            "resultType": "text",
            "values": {
                "text": "亲，已帮你找到相关酒店信息"
            }
        }
    ]
}

    * */
    public static String jsonResponse(String response){
        String text="";
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray results=jsonObject.getJSONArray("results");
            JSONObject result=results.getJSONObject(0);
            JSONObject values=result.getJSONObject("values");
            text=values.getString("text");
          /*  for (int i=0;i<results.length();i++){
                JSONObject result=results.getJSONObject(i);
                JSONObject intent=result.getJSONObject("intent");//必须要
                JSONObject values=result.getJSONObject("vlues");
                text=text+values.getString("text");
                Log.i("Text:",text);
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return text;
    }

}
