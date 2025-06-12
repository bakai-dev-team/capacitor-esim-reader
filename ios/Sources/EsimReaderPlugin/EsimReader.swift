import Foundation

@objc public class EsimReader: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
