import Foundation
import Capacitor
import CoreTelephony
import UIKit

@objc(EsimReaderPlugin)
public class EsimReaderPlugin: CAPPlugin {

    // MARK: - Public API

    @objc func hasEsim(_ call: CAPPluginCall) {
        var byAPI = false
        if #available(iOS 12.0, *) {
            byAPI = CTCellularPlanProvisioning().supportsCellularPlan()
        }
        let supported = byAPI || isEsimCapableByModel(getDeviceModel())
        call.resolve(["supported": supported])
    }

    @objc func getEsimInfo(_ call: CAPPluginCall) {
        if #available(iOS 12.0, *) {
            let info = CTTelephonyNetworkInfo()
            let carriers = info.serviceSubscriberCellularProviders ?? [:]
            let ratByService = info.serviceCurrentRadioAccessTechnology ?? [:]

            var profiles: [[String: Any?]] = []
            for (sid, carrier) in carriers {
                let rat = ratByService[sid]
                profiles.append([
                    "carrierName": carrier.carrierName,
                    "displayName": nil,                // недоступно на iOS
                    "isoCountryCode": carrier.isoCountryCode,
                    "mobileCountryCode": carrier.mobileCountryCode,
                    "mobileNetworkCode": carrier.mobileNetworkCode,
                    "number": nil,                     // недоступно на iOS
                    "simSlotIndex": nil,               // недоступно на iOS
                    "subscriptionId": nil,             // недоступно на iOS
                    "isEmbedded": nil,                 // публично не различимо
                    "allowsVOIP": carrier.allowsVOIP,
                    "radioAccessTechnology": rat
                ])
            }

            let esimSupported = CTCellularPlanProvisioning().supportsCellularPlan()
            call.resolve([
                "esimSupported": esimSupported,
                "profilesCount": profiles.count,
                "profiles": profiles
            ])
        } else {
            call.resolve([
                "esimSupported": false,
                "profilesCount": 0,
                "profiles": []
            ])
        }
    }

    @objc func hasEsimLikely(_ call: CAPPluginCall) {
        var profilesCount = 0
        var esimSupported = false
        if #available(iOS 12.0, *) {
            let info = CTTelephonyNetworkInfo()
            profilesCount = info.serviceSubscriberCellularProviders?.count ?? 0
            esimSupported = CTCellularPlanProvisioning().supportsCellularPlan()
        }
        let byModel = isEsimCapableByModel(getDeviceModel())
        let likely = (esimSupported || byModel) && profilesCount > 0
        call.resolve(["likely": likely])
    }

    // MARK: - Helpers

    private func getDeviceModel() -> String {
        var systemInfo = utsname()
        uname(&systemInfo)
        return Mirror(reflecting: systemInfo.machine).children.reduce("") {
            guard let v = $1.value as? Int8, v != 0 else { return $0 }
            return $0 + String(UnicodeScalar(UInt8(v)))
        }
    }

    /// Динамика: XR/XS и новее (iPhone<major> >= 11) + SE2 (iPhone12,8) и SE3 (iPhone14,6)
    private func isEsimCapableByModel(_ modelId: String) -> Bool {
        if modelId == "x86_64" || modelId == "arm64" { return false } // симулятор
        guard modelId.hasPrefix("iPhone") else { return false }
        if modelId == "iPhone12,8" { return true } // SE 2
        if modelId == "iPhone14,6" { return true } // SE 3
        let suffix = modelId.dropFirst("iPhone".count)
        guard let majorStr = suffix.split(separator: ",").first,
        let major = Int(majorStr) else { return false }
        return major >= 11
    }
}
