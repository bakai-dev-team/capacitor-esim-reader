import { WebPlugin } from '@capacitor/core';

import type { EsimReaderPlugin } from './definitions';

export class EsimReaderWeb extends WebPlugin implements EsimReaderPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
