package com.maochunjie.msa;

import androidx.annotation.NonNull;

import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

public class MiitHelper implements IIdentifierListener {
    private boolean isSDKLogOn = true; //开启日志

    private AppIdsUpdater appIdsUpdater;

    public MiitHelper(AppIdsUpdater callback) {
        // 加固版本在调用前必须载入SDK安全库,因为加载有延迟，推荐在application中调用loadLibrary方法
        // System.loadLibrary("msaoaidsec");
        appIdsUpdater = callback;
    }

    public int initSDK(ReactApplicationContext cxt) {
        return MdidSdkHelper.InitSdk(cxt, isSDKLogOn, this);
    }

    @Override
    public void onSupport(IdSupplier idSupplier) {
        if (idSupplier == null) {
            return;
        }
        if (appIdsUpdater != null) {
            WritableMap map = Arguments.createMap();
            map.putString("OAID", idSupplier.getOAID());
            map.putString("VAID", idSupplier.getVAID());
            map.putString("AAID", idSupplier.getAAID());
            map.putBoolean("isSupport", idSupplier.isSupported());
            map.putBoolean("isLimit", idSupplier.isLimited());
            appIdsUpdater.OnIdsAvalid(map);
        }
    }

    public interface AppIdsUpdater {
        void OnIdsAvalid(@NonNull ReadableMap data);
    }
}
