package com.mycompany.hrms.api.config.security;

import com.mycompany.hrms.api.filters.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomCorsConfiguration customCorsConfiguration;
    private final CustomAuthEntryPoint customAuthEntryPoint;

    public SecurityConfig(JwtFilter jwtFilter,
                          CustomCorsConfiguration customCorsConfiguration,
                          CustomAuthEntryPoint customAuthEntryPoint){
        this.jwtFilter = jwtFilter;
        this.customCorsConfiguration = customCorsConfiguration;
        this.customAuthEntryPoint = customAuthEntryPoint;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http){
        http.csrf(csrf -> csrf.disable()
                        .authorizeHttpRequests( auth ->
                                        auth.requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/error").permitAll()
//                                .requestMatchers("/api/users/**").hasAnyAuthority("Employee", "HR", "Manager")
//                                .requestMatchers("/api/travel/gallery/upload-multiple").hasAnyAuthority("HR", "Employee") //Remove Employee form here just for testing
                                                .anyRequest()
                                                .authenticated()
                        )
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .cors(c -> c.configurationSource(customCorsConfiguration))
                        .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthEntryPoint))
        );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config){
        return config.getAuthenticationManager();
    }
}