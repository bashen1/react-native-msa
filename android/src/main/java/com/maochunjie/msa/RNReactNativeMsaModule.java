package com.maochunjie.msa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bun.miitmdid.core.InfoCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.facebook.react.bridge.*;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RNReactNativeMsaModule extends ReactContextBaseJavaModule {
    private static String TAG = "RNReactNativeMsaModule";
    private static ReactApplicationContext reactContext = null;

    private Boolean isInitSDK = false; // 是否初始化SDK
    private Boolean isCertInit = false; // 是否初始化证书

    private static String oaid = "";
    private static String vaid = "";
    private static String aaid = "";
    private static Boolean isSupport = false;
    private static Boolean isLimit = false;

    private static MiitHelper.AppIdsUpdater appIdsUpdater = new MiitHelper.AppIdsUpdater() {
        @Override
        public void OnIdsAvalid(@NonNull ReadableMap data) {
            oaid = data.getString("OAID");
            vaid = data.getString("VAID");
            aaid = data.getString("AAID");
            isSupport = data.getBoolean("isSupport");
            isLimit = data.getBoolean("isLimit");

            // 监听返回的数据
            WritableMap map = Arguments.createMap();
            map.putString("oaid", oaid);
            map.putString("vaid", vaid);
            map.putString("aaid", aaid);
            map.putBoolean("isSupport", isSupport);
            map.putBoolean("isLimit", isLimit);

            if (reactContext != null) {
                sendEvent(reactContext, "addReceiveMsaIdsListener", map);
            }
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

    /**
     * 初始化SDK
     * 1 初始化成功
     * 0 cert为空
     * -1 cert初始化失败
     * -2 未知错误
     *
     * @param data
     * @param p
     */
    @ReactMethod
    public void initSDK(final ReadableMap data, final Promise p) {
        WritableMap map = Arguments.createMap();
        if (this.isInitSDK) {
            map.putString("message", "成功");
            map.putString("code", Integer.toString(1));
            p.resolve(map);
            return;
        }

        String certStr = data.getString("cert");
        if (certStr == null || certStr.equals("")) {
            map.putString("message", "cert为空");
            map.putString("code", Integer.toString(0));
            p.resolve(map);
            return;
        }

        if (!isCertInit) { // 证书只需初始化一次
            try {
                isCertInit = MdidSdkHelper.InitCert(this.reactContext, certStr);
            } catch (Error e) {
                e.printStackTrace();
            }
            if (!isCertInit) {
                map.putString("message", "cert初始化失败");
                map.putString("code", Integer.toString(-1));
                p.resolve(map);
                return;
            }
        }

        try {
            MiitHelper miitHelper = new MiitHelper(appIdsUpdater);
            int code = miitHelper.initSDK(this.reactContext);
            String message;

            if (code == InfoCode.INIT_ERROR_CERT_ERROR) {
                message = "证书未初始化或证书无效";
            } else if (code == InfoCode.INIT_ERROR_DEVICE_NOSUPPORT) {
                message = "不支持的设备";
            } else if (code == InfoCode.INIT_ERROR_LOAD_CONFIGFILE) {
                message = "加载配置文件出错";
            } else if (code == InfoCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {
                message = "不支持的设备厂商";
            } else if (code == InfoCode.INIT_ERROR_SDK_CALL_ERROR) {
                message = "sdk调用出错";
            } else if (code == InfoCode.INIT_INFO_RESULT_DELAY) {
                this.isInitSDK = true;
                message = "获取接口是异步的";
            } else if (code == InfoCode.INIT_INFO_RESULT_OK) {
                this.isInitSDK = true;
                message = "获取接口是同步的";
            } else {
                this.isInitSDK = true;
                message = "未知code";
            }
            map.putString("message", message);
            map.putString("code", Integer.toString(code));
            p.resolve(map);
        } catch (Throwable throwable) {
            map.putString("message", "未知错误");
            map.putString("code", Integer.toString(-2));
            p.resolve(map);
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

    @ReactMethod
    public void isLimit(final Promise p) {
        p.resolve(isLimit);
    }

    private static void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TAG + eventName, params);
    }
}
