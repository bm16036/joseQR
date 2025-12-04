export const environment = {
  production: false,
  /**
   * URL base del backend. Actualiza este valor cuando el API esté disponible.
   */
  apiBaseUrl: 'http://localhost:8080/api',
  /**
   * URL base del frontend para construir enlaces públicos.
   */
  frontendBaseUrl: 'http://localhost:4200',
  /**
   * Permite trabajar con datos simulados mientras el backend se integra.
   * Cambia a `false` cuando los endpoints estén listos.
   */
  useMockData: false
};
