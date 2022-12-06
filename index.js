import {NativeModules, DeviceEventEmitter} from 'react-native';

const {RNReactNativeMsa} = NativeModules;

const listeners = {};

const TAG = 'RNReactNativeMsaModule';

class MsaModule {
    static initSDK = async(params) => {
        return await RNReactNativeMsa.initSDK(params);
    }

    static isSupport = async() => {
        return await RNReactNativeMsa.isSupport();
    }

    static getOAID = async() => {
        return await RNReactNativeMsa.getOAID();
    }

    static getVAID = async() => {
        return await RNReactNativeMsa.getVAID();
    }

    static getAAID = async() => {
        return await RNReactNativeMsa.getAAID();
    }

    static isLimit = async() => {
        return await RNReactNativeMsa.isLimit();
    }

    static addReceiveMsaIdsListener = (cb) => {
        listeners[cb] = DeviceEventEmitter.addListener(TAG + 'addReceiveMsaIdsListener', cb);
    }

    static removeListener = (callback) => {
        if (!listeners[callback]) {
            return;
        }
        listeners[callback].remove();
        listeners[callback] = null;
    }
}

export default MsaModule;
