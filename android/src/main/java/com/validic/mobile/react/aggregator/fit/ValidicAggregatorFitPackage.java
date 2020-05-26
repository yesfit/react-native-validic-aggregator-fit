package com.validic.mobile.react.aggregator.fit;

import android.content.Intent;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ValidicAggregatorFitPackage implements ReactPackage {

    private ValidicAggregatorFitModule fitModule;

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        fitModule = new ValidicAggregatorFitModule(reactContext);
        return Arrays.<NativeModule>asList(fitModule);
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
    
}
