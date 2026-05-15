package com.nebrimatch.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// Esta clase controla quién puede acceder a qué y cómo se comporta la API ante peticiones externas.
@Configuration
@EnableWebSecurity
// Esta clase configura la seguridad de la aplicación, incluyendo CORS y reglas
// de acceso a los endpoints.
public class SecurityConfig {

    @Bean
    // Configura la cadena de filtros de seguridad, definiendo qué endpoints son
    // públicos y cuáles requieren autenticación.
    // Es equivalente al new object de Java, pero para configurar la seguridad de
    // Spring.
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactivar CSRF porque es una API REST (sin sesiones de navegador)
                .csrf(csrf -> csrf.disable())

                // Configurar CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                /*
                 * CORS
                 * Protege peticiones HTTP entre dominios distintos. Es una política del
                 * navegador que bloquea peticiones de http://localhost:5173 hacia
                 * http://localhost:8080 a menos que el servidor las permita explícitamente.
                 */
                // Sin sesiones: cada petición es independiente
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Todos los endpoints públicos hasta implementar autenticación
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Orígenes permitidos (frontend local en desarrollo)
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
