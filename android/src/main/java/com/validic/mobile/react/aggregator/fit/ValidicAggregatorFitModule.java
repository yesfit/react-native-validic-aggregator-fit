package com.validic.mobile.react.aggregator.fit;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.fitness.data.DataType;
import com.validic.mobile.aggregator.AggregatorException;
import com.validic.mobile.aggregator.AggregatorManager;
import com.validic.mobile.aggregator.fit.ValidicFitManager;
import com.validic.mobile.record.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidicAggregatorFitModule extends ReactContextBaseJavaModule implements AggregatorManager.AggregatorListener, AggregatorManager.PermissionsHandler<DataType> {

    private Promise permissionCallback;

    public ValidicAggregatorFitModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(new ActivityEventListener() {
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                ValidicFitManager.INSTANCE.handlePermissionResult(activity, requestCode, resultCode, data);
            }

            @Override
            public void onNewIntent(Intent intent) {

            }
        });
        ValidicFitManager.INSTANCE.setListener(this);
    }

    @Override
    public String getName() {
        return "ValidicAggregatorFit";
    }

    @ReactMethod
    public void requestPermissions(ReadableArray permissions, Promise promise) {
        if (permissionCallback != null)
            return;
        permissionCallback = promise;
        ValidicFitManager.INSTANCE.requestPermissions(getCurrentActivity(), getDataTypes(permissions), this);
    }

    @ReactMethod
    public void hasDataAccess(ReadableArray dataTypes, Promise promise) {

        Map<DataType, Boolean> m = ValidicFitManager.INSTANCE.hasDataAccess(getDataTypes(dataTypes));
        WritableMap map = Arguments.createMap();
        for (Map.Entry<DataType, Boolean> entry : m.entrySet()) {
            map.putBoolean(entry.getKey().getName(), entry.getValue());
        }
        promise.resolve(map);
    }

    @ReactMethod
    public void subscribe(ReadableArray dataTypes) {
        Set<String> types = new HashSet<>();
        for(int i=0; i<dataTypes.size();i++){
            types.add(dataTypes.getString(i));
        }
        ValidicFitManager.INSTANCE.subscribe(getDataTypes(dataTypes));
    }

    @ReactMethod
    public void unsubscribe(String dataType) {
        DataType d = lookupDataType(dataType);
        if (d != null) {
            ValidicFitManager.INSTANCE.unsubscribe(d);
        }
    }

    @ReactMethod
    public void currentSubscriptions(Promise promise) {
        WritableArray types = Arguments.createArray();
        for (DataType d : ValidicFitManager.INSTANCE.currentSubscriptions()) {
            types.pushString(d.getName());
        }
        promise.resolve(types);
    }

    @ReactMethod
    public void fetchHistory() {
        // only summary is available
        ValidicFitManager.INSTANCE.fetchHistory(AggregatorManager.HistoryType.SUMMARY);
    }

    @ReactMethod
    public void disconnect(){
        ValidicFitManager.INSTANCE.disconnect();
    }

    @Override
    public void onException(AggregatorException e) {
        WritableMap map = Arguments.createMap();
        map.putString("error", e.getMessage());
        if (permissionCallback != null) {
            permissionCallback.reject(e);
            permissionCallback = null;
        }
        sendEvent(getReactApplicationContext(), "validic:fit:onerror", map);

    }

    @Override
    public void onRecords(Map<Record.RecordType, Integer> recordSummary) {
        WritableMap map = Arguments.createMap();
        for(Record.RecordType t: recordSummary.keySet()){
            map.putInt(t.getTypeName(), recordSummary.get(t));
        }

        sendEvent(getReactApplicationContext(), "validic:fit:onrecords", map);

    }

    @Override
    public Map<String, Object> getConstants(){
        Map<String, Object> constants = new HashMap<>();
        constants.put("DataTypeStepCount", "com.google.step_count.delta");
        constants.put("DataTypeWeight", "com.google.weight");
        constants.put("DataTypeHeartRate", "com.google.heart_rate.bpm");
        constants.put("DataTypeCaloriesExpended", "com.google.calories.expended");
        constants.put("DataTypeActiveMinutes", "com.google.active_minutes");
        constants.put("DataTypeStepDistance", "com.google.distance.delta");
        constants.put("DataTypeNutrition", "com.google.nutrition");
        constants.put("EventOnRecords", "validic:fit:onrecords");
        constants.put("EventOnError", "validic:fit:onerror");
        return constants;
    }


    private static DataType lookupDataType(String type) {
        //double for loop boo...
        for (DataType t : ValidicFitManager.INSTANCE.getSupportedDataTypes()) {
            if (t.getName().equals(type)) {
                return t;
            }
        }
        return null;
    }

    private static Set<DataType> getDataTypes(ReadableArray strings) {
        Set<DataType> ret = new HashSet<>();
        for (int i=0;i<strings.size();i++) {
            DataType t = lookupDataType(strings.getString(i));
            if (t != null) {
                ret.add(t);
            }
        }
        return ret;
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public void onResults(Map<DataType, Boolean> map) {
        WritableMap ret = Arguments.createMap();
        for (DataType t : map.keySet()) {
            ret.putBoolean(t.getName(), map.get(t));
        }

        if (permissionCallback != null) {
            permissionCallback.resolve(ret);
            permissionCallback = null;
        }
    }
}
