import { WebPlugin } from '@capacitor/core';
import type { EsimReaderPlugin } from './definitions';
export declare class EsimReaderWeb extends WebPlugin implements EsimReaderPlugin {
    hasEsim(): Promise<{
        supported: boolean;
    }>;
    getEsimInfo(): Promise<{
        esimSupported: boolean;
        profilesCount: number;
        profiles: any[];
    }>;
    hasEsimLikely(): Promise<{
        likely: boolean;
    }>;
}
