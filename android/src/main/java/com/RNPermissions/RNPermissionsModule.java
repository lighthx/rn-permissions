
package com.RNPermissions;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
public class RNPermissionsModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNPermissionsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNPermissions";
  }
  @ReactMethod
  public void getStatus(Promise promise){
    WritableMap map = Arguments.createMap();
    boolean shouldShut= FindHook.isHook(reactContext);
    boolean  shouldWarn=CheckSystemRoot.checkRoot(reactContext);
    if(shouldShut){
      map.putString("status","SHUT");
        promise.resolve(map);
    }else if(shouldWarn){
      map.putString("status","WARN");
      promise.resolve(map);
    }else{
      map.putString("status","SUCCESS");
      promise.resolve(map);
    }
  }
}