package com.maochunjie.msa;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bun.miitmdid.core.ErrorCode;
import com.facebook.react.bridge.*;

public class RNReactNativeMsaModule extends ReactContextBaseJavaModule {
    private static String TAG = "RNReactNativeMsaModule";
    private final ReactApplicationContext reactContext;

    private Boolean isInitSDK = false;
    private static String oaid = "";
    private static String vaid = "";
    private static String aaid = "";
    private static Boolean isSupport = false;

    private static MiitHelper.AppIdsUpdater appIdsUpdater = new MiitHelper.AppIdsUpdater() {
        @Override
        public void OnIdsAvalid(@NonNull ReadableMap data) {
            oaid = data.getString("OAID");
            vaid = data.getString("VAID");
            aaid = data.getString("AAID");
            isSupport = data.getBoolean("isSupport");
        }
    };

    public RNReactNativeMsaModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNReactNativeMsa";
    }

    @ReactMethod
    public void initSDK(final Promise p) {
        WritableMap map = Arguments.createMap();
        if (this.isInitSDK) {
            map.putString("message", "success");
            map.putString("code", Integer.toString(1));
            p.resolve(map);
        } else {
            try {
                int nres = 1;
                String message = "success";
                MiitHelper miitHelper = new MiitHelper(appIdsUpdater);
                nres = miitHelper.initSDK(this.reactContext);

                if (nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {//不支持的设备
                    message = "不支持的设备";
                } else if (nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {//加载配置文件出错
                    message = "加载配置文件出错";
                } else if (nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {//不支持的设备厂商
                    message = "不支持的设备厂商";
                } else if (nres == ErrorCode.INIT_ERROR_RESULT_DELAY) {//获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程
                    //这里暂时返回初始化成功了
                    this.isInitSDK = true;
                    message = "获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程";
                } else if (nres == ErrorCode.INIT_HELPER_CALL_ERROR) {//反射调用出错
                    message = "反射调用出错";
                } else if (nres == ErrorCode.INIT_ERROR_BEGIN) {
                    message = "初始化失败";
                } else {
                    this.isInitSDK = true;
                }
                map.putString("message", message);
                map.putString("code", Integer.toString(nres));
                p.resolve(map);
            } catch (Throwable throwable) {
                map.putString("message", "未知错误");
                map.putString("code", Integer.toString(-1));
                p.resolve(map);
            }
        }
    }

    @ReactMethod
    public void isSupport(final Promise p) {
        p.resolve(isSupport);
    }

    @ReactMethod
    public void getOAID(final Promise p) {
        p.resolve(oaid);
    }

    @ReactMethod
    public void getVAID(final Promise p) {
        p.resolve(vaid);
    }

    @ReactMethod
    public void getAAID(final Promise p) {
        p.resolve(aaid);
    }

    public static void initMSA(Context context) {
        try {
            MiitHelper miitHelper = new MiitHelper(appIdsUpdater);
            miitHelper.initSDK(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
