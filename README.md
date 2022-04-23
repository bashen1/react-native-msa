# react-native-msa

[![npm version](https://badge.fury.io/js/react-native-msa.svg)](https://badge.fury.io/js/react-native-msa)

SDK Version 1.1.0

## 开始

`$ npm install react-native-msa --save`

如果react native 版本小于0.6X，还需要执行 `react-native link react-native-msa`

## 注意

将`supplierconfig.json`拷贝到项目`android/app/src/main/assets`目录下。（暂时不用修改，只需原样放到assets目录下即可，这个是给未来做准备的。如果想要使用VAID，可修改里边对应内容，特别是需要设置appid的部分，要去对应厂商的应用商店里注册自己的app，来获取对应appid。）

## 集成

打开项目中的 `MainApplication.java`

```java
public class MainApplication extends MultiDexApplication implements ReactApplication {

	....
	....
	....

	@Override
    public void onCreate() {
        super.onCreate();
        System.loadLibrary("msaoaidsec"); //加固版本在调用前必须载入SDK安全库,因为加载有延迟，推荐在application中调用loadLibrary方法
}
```

## 使用方法

|         方法        |   说明  |
| :-----------------: | :---: |
| initSDK | 初始化SDK |
| isSupport | 是否支持MSA |
| getOAID | 获取OAID |
| getVAID | 获取VAID |
| getAAID | 获取AAID |
| isLimit | 补充设备标识符开关 |

`注意：`initSDK 成功后不要立马取值，可以延迟50～100毫秒再进行取值

### initSDK 返回状态码（字符串）

- 1 初始化成功
- 0 cert为空
- -1 cert初始化失败，一般是证书字符串错误
- -2 未知错误
- 1008610 调用成功，获取接口是同步的
- 1008614 调用成功，获取接口是异步的
- 1008616 证书未初始化或证书无效
- 1008611 不支持的厂商
- 1008612 不支持的设备
- 1008613 加载配置文件出错
- 1008615 sdk 调用出错

详见examples目录
