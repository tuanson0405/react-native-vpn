import { NativeModules, NativeEventEmitter } from 'react-native';

const { Vpn } = NativeModules;

const VpnEmitter = new NativeEventEmitter(Vpn);

export default {
  emitter: VpnEmitter,
  connectVpn () {
    return Vpn.connectVpn();
  },
  disconnectVpn () {
    return Vpn.disconnectVpn();
  },
  setupVpn () {
    return Vpn.setupServer();
  },
};
