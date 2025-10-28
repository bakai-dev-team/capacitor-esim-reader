import { WebPlugin } from '@capacitor/core';
import type { EsimReaderPlugin } from './definitions';

export class EsimReaderWeb extends WebPlugin implements EsimReaderPlugin {
  async hasEsim(): Promise<{ supported: boolean }> {
    // eSIM не поддерживается в браузере
    return { supported: false };
  }

  async getEsimInfo(): Promise<{
    esimSupported: boolean;
    profilesCount: number;
    profiles: any[];
  }> {
    return {
      esimSupported: false,
      profilesCount: 0,
      profiles: [],
    };
  }

  async hasEsimLikely(): Promise<{ likely: boolean }> {
    return { likely: false };
  }
}
