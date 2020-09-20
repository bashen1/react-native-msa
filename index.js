import {NativeModules} from 'react-native';

const {RNReactNativeMsa} = NativeModules;

export async function initSDK() {
	return await RNReactNativeMsa.initSDK();
}

export async function isSupport() {
    return await RNReactNativeMsa.isSupport();
}

export async function getOAID() {
    return await RNReactNativeMsa.getOAID();
}

export async function getVAID() {
    return await RNReactNativeMsa.getVAID();
}

export async function getAAID() {
    return await RNReactNativeMsa.getAAID();
}