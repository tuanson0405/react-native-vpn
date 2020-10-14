import { NativeModules, NativeEventEmitter } from 'react-native';

const { Vpn } = NativeModules;

const VpnEmitter = new NativeEventEmitter(Vpn);

export default {
  /**
   * Listen VPN events - VPN status change
   */
  statusListener (onStatusChange) {
    VpnEmitter.addEventListener('VPN_EVENT_STATUS', onStatusChange);
  },
  infoListener (onInfoChange) {
    VpnEmitter.addEventListener('VPN_EVENT_INFO', onInfoChange);
  },
  /**
   * Remove events listener
   */
  removeListener () {
    VpnEmitter.removeListener();
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
