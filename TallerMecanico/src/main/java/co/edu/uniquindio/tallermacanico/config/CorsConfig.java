package co.edu.uniquindio.tallermacanico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing) para permitir
 * peticiones desde el frontend hacia el backend.
 *
 * Esta configuración permite que el frontend (ejecutándose en puertos como 5500, 5501, etc.)
 * pueda hacer peticiones al backend (ejecutándose en puerto 8080).
 */
@Configuration
public class CorsConfig {

    /**
     * Configura el filtro CORS para toda la aplicación.
     *
     * IMPORTANTE: Esta configuración es para DESARROLLO.
     * En PRODUCCIÓN debes especificar los dominios exactos permitidos.
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permitir credenciales (cookies, authorization headers)
        config.setAllowCredentials(true);

        // Usar allowedOriginPatterns en lugar de allowedOrigins cuando allowCredentials es true
        // Esto permite cualquier origen en desarrollo
        config.setAllowedOriginPatterns(Arrays.asList("*"));

        // Para PRODUCCIÓN, reemplaza la línea anterior con dominios específicos:
        // config.setAllowedOrigins(Arrays.asList(
        //     "http://localhost:5500",
        //     "http://127.0.0.1:5500",
        //     "https://tu-dominio-frontend.com"
        // ));

        // Permitir todos los headers
        config.setAllowedHeaders(Arrays.asList("*"));

        // Permitir todos los métodos HTTP
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Exponer headers en la respuesta
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Aplicar configuración a todos los endpoints
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    /**
     * Alternativa: Bean de configuración CORS sin credenciales
     * (descomenta este método y comenta el anterior si no necesitas credenciales)
     */
    /*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Sin credenciales, podemos usar allowedOrigins con "*"
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    */
}