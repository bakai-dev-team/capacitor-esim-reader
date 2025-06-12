// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorEsimReader",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorEsimReader",
            targets: ["EsimReaderPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "EsimReaderPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/EsimReaderPlugin"),
        .testTarget(
            name: "EsimReaderPluginTests",
            dependencies: ["EsimReaderPlugin"],
            path: "ios/Tests/EsimReaderPluginTests")
    ]
)