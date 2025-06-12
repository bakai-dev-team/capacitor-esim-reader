import { registerPlugin } from '@capacitor/core';

import type { EsimReaderPlugin } from './definitions';

const EsimReader = registerPlugin<EsimReaderPlugin>('EsimReader', {
  web: () => import('./web').then((m) => new m.EsimReaderWeb()),
});

export * from './definitions';
export { EsimReader };
