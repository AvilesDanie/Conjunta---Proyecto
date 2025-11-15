/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ec.edu.monster;


/**
 *
 * @author danie
 */

import ec.edu.monster.controller.ClienteController;
import ec.edu.monster.controller.CreditoController;
import ec.edu.monster.controller.CuentaController;
import ec.edu.monster.controller.CuotaAmortizacionController;
import ec.edu.monster.controller.MovimientoController;
import ec.edu.monster.controller.UsuarioController;



import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Swagger-Core (integración JAX-RS)
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;

// Swagger-Core (modelo OpenAPI)
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@ApplicationPath("api")
public class ApplicationConfig extends Application {

    public ApplicationConfig() {
        // 1) Definimos el server base: /WS_JAVA_REST_BanQuito
        OpenAPI openAPI = new OpenAPI();
        openAPI.setServers(List.of(
                new Server()
                        .url("/WS_JAVA_REST_BanQuito")   // <<-- CONTEXT ROOT DE TU APP
                        .description("Servidor local BanQuito")
        ));

        // 2) Configuramos Swagger para usar ese OpenAPI y escanear tus controllers
        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(openAPI)
                .prettyPrint(true)
                .resourcePackages(Set.of("ec.edu.monster.controller"));

        try {
            new JaxrsOpenApiContextBuilder()
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        // === Tus controllers REST ===
        resources.add(ClienteController.class);
        resources.add(CreditoController.class);
        resources.add(CuentaController.class);
        resources.add(CuotaAmortizacionController.class);
        resources.add(MovimientoController.class);
        resources.add(UsuarioController.class);
        // agrega aquí cualquier otro controller REST que tengas

        // === Recurso de Swagger-Core que expone /api/openapi.json ===
        resources.add(OpenApiResource.class);

        return resources;
    }
}
