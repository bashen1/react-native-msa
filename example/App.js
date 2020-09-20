/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React from 'react';
import {
  SafeAreaView,
  ScrollView,
  View,
  Text,
  StatusBar,
  TouchableOpacity,
} from 'react-native';

import * as MSA from 'react-native-msa';

const App: () => React$Node = () => {
  return (
    <>
      <StatusBar barStyle="dark-content" />
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic">
          <Text style={{
            fontSize:30,
            fontWeight: 'bold',
            textAlign: 'center',
            marginTop: 40,
            marginBottom: 20,
          }}>MSA</Text>
          <Button
            text={'initSDK_Direct'}
            onPress={async () => {
              let res= await MSA.initSDK();
              alert(JSON.stringify(res));
            }}
          />
          <Button
            text={'initSDK_Reflect'}
            onPress={async () => {
              let res= await MSA.initSDK({
                initType: 'reflect',
              });
              alert(JSON.stringify(res));
            }}
          />
          <Button
            text={'isSupport'}
            onPress={async () => {
              alert(await MSA.isSupport());
            }}
          />
          <Button
            text={'getOAID'}
            onPress={async () => {
              alert(await MSA.getOAID());
            }}
          />
          <Button
            text={'getVAID'}
            onPress={async () => {
              alert(await MSA.getVAID());
            }}
          />
          <Button
            text={'getAAID'}
            onPress={async () => {
              alert(await MSA.getAAID());
            }}
          />
        </ScrollView>
      </SafeAreaView>
    </>
  );
};

const Button = (props) => (
  <TouchableOpacity
    style={{
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor: 'rgba(187,239,253,.7)',
      height: 44,
      margin: 10,
      borderRadius: 5
    }}
    onPress={() => {
      props.onPress && props.onPress();
      MSA.getAAID();
    }}
  >
    <Text style={{fontSize: 16, fontWeight: '500'}}>{props.text || '未设置'}</Text>
  </TouchableOpacity>
);

export default App;
