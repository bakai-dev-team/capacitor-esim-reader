import { registerPlugin } from '@capacitor/core';
const EsimReader = registerPlugin('EsimReader', {
    web: () => import('./web').then((m) => new m.EsimReaderWeb()),
});
export * from './definitions';
export { EsimReader };
//# sourceMappingURL=index.js.map