/* Configuración central de conexión con el backend */
const CONFIG = {
  // Cambia a true para probar en local y a false para Render
  USAR_LOCAL: false,

  URL_LOCAL: "http://localhost:8080/mavenproject3/resources",
  URL_PRODUCCION: "https://empresatransporte.onrender.com/mavenproject3/resources",

  get API_BASE() {
    return this.USAR_LOCAL ? this.URL_LOCAL : this.URL_PRODUCCION;
  }
};