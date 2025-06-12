export interface EsimReaderPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
