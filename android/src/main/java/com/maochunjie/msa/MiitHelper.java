package com.maochunjie.msa;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

public class MiitHelper implements IIdentifierListener {

    private AppIdsUpdater _listener;

    public MiitHelper(AppIdsUpdater callback) {
        _listener = callback;
    }

    public int initSDK(Context cxt) {
        return CallFromReflect(cxt);
    }

    /*
     * 通过反射调用，解决android 9以后的类加载升级，导至找不到so中的方法
     *
     * */
    private int CallFromReflect(Context cxt) {
        return MdidSdkHelper.InitSdk(cxt, true, this);
    }


    @Override
    public void OnSupport(boolean isSupport, IdSupplier _supplier) {
        if (_supplier == null) {
            return;
        }
        if (_listener != null) {
            WritableMap map = Arguments.createMap();
            map.putString("OAID", _supplier.getOAID());
            map.putString("VAID", _supplier.getVAID());
            map.putString("AAID", _supplier.getAAID());
            map.putBoolean("isSupport", _supplier.isSupported());
            _listener.OnIdsAvalid(map);
        }
    }

    public interface AppIdsUpdater {
        void OnIdsAvalid(@NonNull ReadableMap data);
    }
}
