package com.reactlibrary;

import com.facebook.react.bridge.Callback;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.eddieowens.receivers.BoundaryEventBroadcastReceiver;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeofenceModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private final ReactApplicationContext reactContext;

    public static final String TAG = "RNBoundary";
    public static final String ON_ENTER = "onEnter";
    public static final String ON_EXIT = "onExit";
    public static final String GEOFENCE_DATA_TO_EMIT = "com.eddieowens.GEOFENCE_DATA_TO_EMIT";

    private GeofencingClient mGeofencingClient;
    private PendingIntent mBoundaryPendingIntent;

    public GeofenceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.mGeofencingClient = LocationServices.getGeofencingClient(getReactApplicationContext());
        getReactApplicationContext().addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return "Geofence";
    }

    @ReactMethod
    public void add (final ReadableMap readableMap, final Promise promise) {
        // TODO: Implement some actually useful functionality

        final GeofencingRequest geofencingRequest = createGeofenceRequest(createGeofence(readableMap));
        addGeofence(promise, geofencingRequest, geofencingRequest.getGeofences().get(0).getRequestId());

    }

    private GeofencingRequest createGeofenceRequest(List<Geofence> geofences) {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofences(geofences)
                .build();
    }

    private PendingIntent getBoundaryPendingIntent() {
        if (mBoundaryPendingIntent != null) {
            return mBoundaryPendingIntent;
        }
        Intent intent = new Intent(getReactApplicationContext(), BoundaryEventBroadcastReceiver.class);
        mBoundaryPendingIntent = PendingIntent.getBroadcast(getReactApplicationContext(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mBoundaryPendingIntent;
    }

    private void addGeofence(final Promise promise, final GeofencingRequest geofencingRequest, final String requestId) {

        mGeofencingClient.addGeofences(
                geofencingRequest,
                getBoundaryPendingIntent()
        )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully added geofence.");
                        promise.resolve(requestId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Failed to add geofence.");
                        promise.reject(e);
                    }
                });

    }
}
