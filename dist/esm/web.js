import { WebPlugin } from '@capacitor/core';
export class EsimReaderWeb extends WebPlugin {
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
//# sourceMappingURL=web.js.map