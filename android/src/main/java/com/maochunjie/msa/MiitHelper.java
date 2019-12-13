package com.maochunjie.msa;

import android.content.Context;
import androidx.annotation.NonNull;
import com.bun.miitmdid.core.IIdentifierListener;
import com.bun.miitmdid.core.MdidSdk;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.supplier.IdSupplier;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

public class MiitHelper implements IIdentifierListener {

    private AppIdsUpdater _listener;
    private String _initType;

    public MiitHelper(AppIdsUpdater callback, String initType) {
        _listener = callback;
        _initType = initType;
    }

    public int initSDK(Context cxt) {
        if (_initType.equals("direct")) {
            return DirectCall(cxt);
        } else {
            return ReflectCall(cxt);
        }
    }

    /*
     * 通过反射调用，解决android 9以后的类加载升级，导至找不到so中的方法
     *
     * */
    private int ReflectCall(Context cxt) {
        return MdidSdkHelper.InitSdk(cxt, true, this);
    }

    /*
     * 直接java调用，如果这样调用，在android 9以前没有题，在android 9以后会抛找不到so方法的异常
     * 解决办法是和JLibrary.InitEntry(cxt)，分开调用，比如在A类中调用JLibrary.InitEntry(cxt)，在B类中调用MdidSdk的方法
     * A和B不能存在直接和间接依赖关系，否则也会报错
     *
     * */
    private int DirectCall(Context cxt) {
        MdidSdk sdk = new MdidSdk();
        return sdk.InitSdk(cxt, this);
    }

    @Override
    public void OnSupport(boolean isSupport, IdSupplier _supplier) {
        if (_supplier == null) {
            return;
        }
        _supplier.shutDown();           //关闭接口
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
