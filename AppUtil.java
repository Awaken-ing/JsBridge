package com.example.poch5;

import android.app.Application;
import android.content.Context;

/**
 * author : ex-zhusiyu003
 * data : 2023/7/8 14:33
 * desc :
 * version :
 */
public class AppUtil {
    public static Application mContext;
    public static void setApplication(Application context) {
        mContext = context;
    }
}
