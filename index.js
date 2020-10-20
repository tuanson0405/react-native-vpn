import { NativeModules, NativeEventEmitter } from 'react-native'

const { Vpn } = NativeModules

const VpnEmitter = new NativeEventEmitter(Vpn)

export default {
  /**
   * Listen VPN events - VPN status change
   */
  statusListener (onStatusChange) {
    return VpnEmitter.addListener('VPN_EVENT_STATUS', onStatusChange, null)
  },
  infoListener (onInfoChange) {
    return VpnEmitter.addListener('VPN_EVENT_INFO', onInfoChange, null)
  },
  getVpnStatus () {
    return Vpn.getVpnStatus()
  },
  connectVpn ({ config, country, username, password }) {
    return Vpn.connectVpn(config, country, username, password)
  },
  disconnectVpn () {
    return Vpn.disconnectVpn()
  },
}
