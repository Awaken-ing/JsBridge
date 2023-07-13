package com.example.poch5.jsbridge;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;



import java.util.HashMap;

/**
 * JSBridge
 */
public class JSBridge {

    private static JsBridgeConfigImpl config = JsBridgeConfigImpl.getInstance();
    private final static String TAG = "JSBridge";
    static {
        config.registerMethodRun(JSBridgeReadyRun.class);
    }

    public static JsBridgeConfig getConfig() {
        return config;
    }

    /**
     * 注入JS
     */
    public static void injectJs(WebView webView) {
        StringBuilder builder = new StringBuilder();
        // 注入默认方法
        builder.append("window." + config.getProtocol() + " = {");
        for (String platform : config.getExposedMethods().keySet()) {
            builder.append(platform + ":{");
            HashMap<String, JsMethod> methods = config.getExposedMethods().get(platform);
            for (String method : methods.keySet()) {
                JsMethod jsMethod = methods.get(method);
                builder.append(jsMethod.getInjectJs());
            }
            builder.append("},");
        }
        builder.append("};");
        // 注入可执行方法
        for (Class<? extends JsMethodRun> run : config.getMethodRuns()) {
            try {
                String execJs = run.newInstance().execJs();
                builder.append(execJs);
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
            }
        }
        webView.loadUrl("javascript:" + builder.toString());
    }


    /**
     * 执行js回调
     *
     * @param webView
     * @param uriString
     * @return
     */
    public static String callJsPromptO(Object activity, WebView webView, String uriString) {
        if (webView == null || TextUtils.isEmpty(uriString)) {
            return null;
        }
        Uri uri = Uri.parse(uriString);
        String methodName = "";
        String className = "";
        String param = "";
        String port = "";
        if (uriString.startsWith(config.getProtocol())) {
            className = uri.getHost();
            param = uri.getQuery();
            String path = uri.getPath();
            port = uri.getPort() + "";
            if (!TextUtils.isEmpty(path)) {
                methodName = path.replace("/", "");
            }
        }
        if (config.getExposedMethods().containsKey(className)) {
            HashMap<String, JsMethod> methodHashMap = config.getExposedMethods().get(className);
            if (methodHashMap != null && methodHashMap.size() != 0 && methodHashMap.containsKey(methodName)) {
                JsMethod method = methodHashMap.get(methodName);
                if (method != null && method.getJavaMethod() != null && method.getParameterType() != -1) {
                    try {
                        Object ret = null;
                        switch (method.getParameterType()) {
                            case ParameterType.TYPE_O:
                                ret = method.invoke(param);
                                break;
                            case ParameterType.TYPE_AO:
                                ret = method.invoke(activity, param);
                                break;
                            case ParameterType.TYPE_WO:
                                ret = method.invoke(webView, param);
                                break;
                            case ParameterType.TYPE_AWO:
                                ret = method.invoke(activity, webView, param);
                                break;
                            case ParameterType.TYPE_AOJ:
                                ret = method.invoke(activity, param, new JSCallback(webView, method, port));
                                break;
                            case ParameterType.TYPE_IOJ:
                                ret = method.invoke(activity, param, new JSCallback(webView, method, port));
                                break;
                            case ParameterType.TYPE_FOJ:
                                ret = method.invoke(activity, param, new JSCallback(webView, method, port));
                                break;
                            case ParameterType.TYPE_OOJ:
                                ret = method.invoke(activity, param, new JSCallback(webView, method, port));
                                break;
                            case ParameterType.TYPE_OJ:
                                ret = method.invoke(param, new JSCallback(webView, method, port));
                                break;
                            case ParameterType.TYPE_WOJ:
                                ret = method.invoke(webView, param, new JSCallback(webView, method, port));
                                break;
                            case ParameterType.TYPE_AWOJ:
                                ret = method.invoke(activity, webView, param, new JSCallback(webView, method, port));
                                break;
                            default:
                                break;
                        }
                        if (ret != null) {
                            return ret.toString();
                        }
                    } catch (Exception e) {
                        Log.e(TAG,e.getMessage());
                    }
                }
            }
        }
        return null;
    }


    /**
     * 执行js回调
     *
     * @param webView
     * @param uriString
     * @return
     */
    public static String callJsPrompt(Activity activity, WebView webView, String uriString) {
        return callJsPromptO(activity, webView, uriString);
    }
}
