'use strict';

var core = require('@capacitor/core');

const EsimReader = core.registerPlugin('EsimReader', {
    web: () => Promise.resolve().then(function () { return web; }).then((m) => new m.EsimReaderWeb()),
});

class EsimReaderWeb extends core.WebPlugin {
    async hasEsim() {
        // eSIM не поддерживается в браузере
        return { supported: false };
    }
    async getEsimInfo() {
        return {
            esimSupported: false,
            profilesCount: 0,
            profiles: [],
        };
    }
    async hasEsimLikely() {
        return { likely: false };
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    EsimReaderWeb: EsimReaderWeb
});

exports.EsimReader = EsimReader;
//# sourceMappingURL=plugin.cjs.js.map
