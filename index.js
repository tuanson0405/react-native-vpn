import { NativeModules, NativeEventEmitter } from 'react-native'

const { Vpn } = NativeModules

const VpnEmitter = new NativeEventEmitter(Vpn)

export default {
  emitter: VpnEmitter,
  sampleMethod (data, callback) {
    return Vpn.sampleMethod(data, callback);
  },
};
