package com.example.poch5.jsbridge;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.example.poch5.AppDatabase;
import com.example.poch5.User;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author givon
 * @author miaohui245
 * @date 2018/10/30 10:12
 **/
public class ServiceModule implements JsModule {
    private static ExecutorService executorDBService;
    private static AppDatabase appDatabase;
    private static final String TAG = "ServiceModule";

    private static StringBuilder stringBuilder = null;

    public static void getH5Event(String s){
        Log.d(TAG,s+"Js调用getH5Event");
    }

    public static void postMessage( final String object, final JSCallback callback) {
        if (TextUtils.isEmpty(object)) {
            Log.d(TAG,"Js调用参数为空");
            return;
        }
        Log.e(TAG,"Js参数:" + object);
        Gson gson = new Gson();
        String method = "";
        try {
            JSONObject jsonObject = new JSONObject(object);
            method = jsonObject.getString("method");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        if (TextUtils.isEmpty(method)) {
            return;
        }
        Log.e(TAG,"jsBridge方法:" + method);
        Type jsonType;
        JsMethod jsMethod;
        switch (method) {
            case ServiceModuleMethodConstants.INSERT_USER:
                try {
                    JSONObject jsonObject = new JSONObject(object);
                    JSONArray param = jsonObject.getJSONArray("param");
                    String obj = param.get(0).toString();
                    if (!TextUtils.isEmpty(obj)) {
                        JSONObject object1 = new JSONObject(obj);
                        int uid = object1.getInt("uid");
                        String userName = object1.getString("userName");
                        int age = object1.getInt("age");
                        String gender = object1.getString("gender");
                        int income = object1.getInt("income");
                        int height = object1.getInt("height");
                        insertUser(uid,userName,age,gender,income,height);
                    }
                } catch (Exception e) {
                    Log.e(TAG,"call teams error:" + e.getMessage());
                }
                break;


            default:
                Log.d(TAG,"Js方法没有设置调用");
                break;
        }

    }

//    private static void receiveHugeDataTranslateEntity(IWebView iwebView, HugeDataTranslateEntity hugeDataTranslateEntity) {
//        if (hugeDataTranslateEntity == null) {
//            return;
//        }
//        if (hugeDataTranslateEntity.getIndex() == 0) {
//            stringBuilder = new StringBuilder();
//        }
//        if (hugeDataTranslateEntity.getIndex() < hugeDataTranslateEntity.getTotal()) {
//            stringBuilder.append(hugeDataTranslateEntity.getBase64().trim());
//        }
//        if (hugeDataTranslateEntity.getIndex() == hugeDataTranslateEntity.getTotal() - 1) {
//            if (HugeDataTranslateEntity.PREVIEW_DOCUMENT_FILE.equals(hugeDataTranslateEntity.getMethod())) {
//                PreviewDocumentEntity previewEntity = new PreviewDocumentEntity();
//                previewEntity.setUrl(stringBuilder.toString());
//                previewEntity.setTitle(hugeDataTranslateEntity.getType());
//                previewEntity.setShowSave(hugeDataTranslateEntity.getShowSave());
//                previewEntity.setShowShare(hugeDataTranslateEntity.getShowShare());
//                previewEntity.setType(hugeDataTranslateEntity.getType());
//                iwebView.openPreviewPhotoActivity(previewEntity);
//            } else if (HugeDataTranslateEntity.SHOW_SHARE_DIALOG.equals(hugeDataTranslateEntity.getMethod())) {
//                ShareData shareData = new ShareData();
//                shareData.setTitle("");
//                shareData.setBase64(stringBuilder.toString());
//                shareData.setH5callback(hugeDataTranslateEntity.getH5callback());
//                iwebView.showShareDialog(shareData);
//            }
//        }
//
//    }

    public static void getJsObject(String options) {
        Log.d(TAG, "getObject: " + options);
    }

    public ServiceModule() {
        executorDBService =  Executors.newFixedThreadPool(2);
        appDatabase = AppDatabase.getDatabase("password");
    }

    @Override
    public String getModuleName() {
        return "service";
    }

    // 插入一条数据
    public static void insertUser(int uid,String userName,int userAge,String userGender,int income,int height){
        User user = new User();
        user.uid = uid;
        user.userName = userName;
        user.age  = userAge;
        user.gender = userGender;
        user.income = income;
        user.height = height;
        executorDBService.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.userDao().insert(user);
            }
        });
    }

}
