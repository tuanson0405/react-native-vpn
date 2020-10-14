import { NativeModules, NativeEventEmitter } from 'react-native';

const { Vpn } = NativeModules;

const VpnEmitter = new NativeEventEmitter(Vpn);

export default {
  /**
   * Listen VPN events - VPN status change
   */
  statusListener (onStatusChange) {
    VpnEmitter.addListener('VPN_EVENT_STATUS', onStatusChange, null);
  },
  infoListener (onInfoChange) {
    VpnEmitter.addListener('VPN_EVENT_INFO', onInfoChange, null);
  },
  /**
   * Remove events listener
   */
  removeListener () {
    VpnEmitter.removeCurrentListener();
  },
  getVpnStatus () {
    return Vpn.getVpnStatus()
  },
  connectVpn () {
    return Vpn.connectVpn();
  },
  disconnectVpn () {
    return Vpn.disconnectVpn();
  }
};
