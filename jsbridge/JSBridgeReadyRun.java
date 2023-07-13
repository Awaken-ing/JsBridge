package com.example.poch5.jsbridge;

/**
 * Created by givon on 16/5/13.
 */
class JSBridgeReadyRun implements JsMethodRun {

    JsBridgeConfigImpl config = JsBridgeConfigImpl.getInstance();

    @Override
    public String execJs() {
        StringBuilder builder = new StringBuilder("try{");
        builder.append("var ready = window." + config.getReadyFuncName() + ";");
        builder.append("if(ready && typeof(ready) === 'function'){setTimeout(ready(), 100)}");
        builder.append("}catch(e){};");
        return builder.toString();
    }
}
