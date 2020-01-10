package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;

public class GeofenceModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public GeofenceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "Geofence";
    }

    @ReactMethod
    public void add (String stringArgument, final Promise promise) {
        // TODO: Implement some actually useful functionality

        promise.resolve("Hello");
    }
}
