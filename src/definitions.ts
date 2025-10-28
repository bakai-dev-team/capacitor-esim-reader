export interface EsimReaderPlugin {
  /**
   * Проверяет, поддерживает ли устройство технологию eSIM.
   * На Android — TelephonyManager.isEuiccSupported()
   * На iOS — CTCellularPlanProvisioning.supportsCellularPlan()
   */
  hasEsim(): Promise<{ supported: boolean }>;

  /**
   * Возвращает полную информацию о доступных SIM/eSIM профилях.
   * Android — SubscriptionInfoList
   * iOS — serviceSubscriberCellularProviders
   */
  getEsimInfo(): Promise<{
    esimSupported: boolean;
    profilesCount: number;
    profiles: Array<{
      carrierName?: string | null;
      displayName?: string | null;
      isoCountryCode?: string | null;
      mobileCountryCode?: string | null;
      mobileNetworkCode?: string | null;
      number?: string | null;
      simSlotIndex?: number;
      subscriptionId?: number;
      isEmbedded?: boolean;
      allowsVOIP?: boolean;
      radioAccessTechnology?: string | null;
    }>;
  }>;

  /**
   * (Необязательно) Быстрая эвристика, есть ли eSIM в наличии.
   */
  hasEsimLikely?(): Promise<{ likely: boolean }>;
}
