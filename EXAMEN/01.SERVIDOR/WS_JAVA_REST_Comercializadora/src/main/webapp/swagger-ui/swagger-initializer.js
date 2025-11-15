window.onload = function() {
  window.ui = SwaggerUIBundle({
    url: "/WS_JAVA_REST_Comercializadora/api/openapi.json", // generado dinámicamente por swagger-core
    dom_id: '#swagger-ui',
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    layout: "StandaloneLayout"
  });
};
