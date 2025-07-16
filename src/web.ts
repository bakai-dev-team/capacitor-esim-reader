import { WebPlugin } from '@capacitor/core';

import type { EsimReaderPlugin } from './definitions';

export class EsimReaderWeb extends WebPlugin implements EsimReaderPlugin {
  async hasEsim(): Promise<{ supported: boolean }> {
    return { supported: false };
  }

  async getEsimInfo(): Promise<{ info: string | null }> {
    return { info: null };
  }
}
