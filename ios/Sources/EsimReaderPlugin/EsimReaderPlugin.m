#import <Capacitor/Capacitor.h>

// Регистрация плагина для iOS. Имя должно совпадать с именем в registerPlugin('EsimReader').
// Экспортируем методы, которые возвращают промисы на стороне JS.
CAP_PLUGIN(EsimReaderPlugin, "EsimReader",
           CAP_PLUGIN_METHOD(hasEsim, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getEsimInfo, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(hasEsimLikely, CAPPluginReturnPromise);
)


