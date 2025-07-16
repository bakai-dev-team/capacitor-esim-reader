import Capacitor
import CoreTelephony

@objc(EsimReaderPlugin)
public class EsimReaderPlugin: CAPPlugin {
    @objc func hasEsim(_ call: CAPPluginCall) {
        if #available(iOS 12.0, *) {
            let networkInfo = CTTelephonyNetworkInfo()
            if let carriers = networkInfo.serviceSubscriberCellularProviders, carriers.count > 1 {
                call.resolve(["supported": true])
                return
            }
        }
        call.resolve(["supported": false])
    }

    @objc func getEsimInfo(_ call: CAPPluginCall) {
        if #available(iOS 12.0, *) {
            let networkInfo = CTTelephonyNetworkInfo()
            if let carriers = networkInfo.serviceSubscriberCellularProviders {
                var carrierDescriptions: [String] = []
                for (key, carrier) in carriers {
                    if let name = carrier.carrierName {
                        carrierDescriptions.append("\(key): \(name)")
                    }
                }
                call.resolve(["info": carrierDescriptions.joined(separator: ", ")])
                return
            }
        }
        call.resolve(["info": nil])
    }
}
