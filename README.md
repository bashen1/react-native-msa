# react-native-msa

[![npm version](https://badge.fury.io/js/react-native-msa.svg)](https://badge.fury.io/js/react-native-msa)

## 开始

`$ npm install react-native-msa --save`

如果react native 版本小于0.6X，还需要执行 `react-native link react-native-msa`

## 注意

将`supplierconfig.json`拷贝到项目`android/app/src/main/assets`目录下。（暂时不用修改，只需原样放到assets目录下即可，这个是给未来做准备的。如果想要使用VAID，可修改里边对应内容，特别是需要设置appid的部分，要去对应厂商的应用商店里注册自己的app，来获取对应appid。）


## 如果与最新的TalkingData一起使用
打开项目中的 `MainApplication.java`

```java
import com.maochunjie.msa.RNReactNativeMsaModule;

public class MainApplication extends MultiDexApplication implements ReactApplication {

	....
	....
	....

	@Override
    public void onCreate() {
        super.onCreate();

        RNReactNativeMsaModule.initMSA(getApplicationContext()); //添加这句，在TD init之前
        RNReactNativeMtalkingdataModule.register(getApplicationContext(), null, null, true); //talkingdata
    }
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

详见examples目录
