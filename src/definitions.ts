export interface EsimReaderPlugin {
  hasEsim(): Promise<{ supported: boolean }>;
  getEsimInfo(): Promise<{ info: string | null }>;
}
