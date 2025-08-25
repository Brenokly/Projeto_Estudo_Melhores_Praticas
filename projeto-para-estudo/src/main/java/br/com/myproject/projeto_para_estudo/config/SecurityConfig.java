package br.com.myproject.projeto_para_estudo.config;

import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.myproject.projeto_para_estudo.infrastructure.security.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public static CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:3000"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Configuration
  @Order(1)
  public static class StaticResourcesConfig {
    @Bean
    public SecurityFilterChain staticResourceFilterChain(HttpSecurity http) throws Exception {
      return http
          .securityMatcher(PathRequest.toStaticResources().atCommonLocations())
          .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .csrf(AbstractHttpConfigurer::disable)
          .build();
    }
  }

  @Configuration
  @Order(2)
  public static class ApiSecurityConfig {
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter,
        AuthenticationProvider authenticationProvider) throws Exception {
      http
          .securityMatcher("/api/**")
          .csrf(AbstractHttpConfigurer::disable)
          .cors(cors -> cors.configurationSource(corsConfigurationSource()))
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/api/v1/auth/**").permitAll()
              .requestMatchers(HttpMethod.POST, "/api/v1/clientes", "/api/v1/fornecedores").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/v1/fornecedores/**", "/api/v1/produtos/**")
              .permitAll()
              .anyRequest().authenticated())
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authenticationProvider(authenticationProvider)
          .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
      return http.build();
    }
  }

  @Configuration
  @Order(3)
  public static class WebSecurityConfig {
    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
      http
          .authorizeHttpRequests(auth -> auth
              .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
              .requestMatchers("/", "/login", "/perform_login", "/cadastro-cliente",
                  "/cadastro-fornecedor",
                  // ROTAS DO SWAGGER NO LUGAR CORRETO
                  "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
              .permitAll()
              .anyRequest().authenticated())
          .formLogin(form -> form
              .loginPage("/login")
              .loginProcessingUrl("/perform_login")
              .defaultSuccessUrl("/dashboard", true)
              .failureUrl("/login?error=true")
              .permitAll())
          .logout(logout -> logout
              .logoutUrl("/logout")
              .logoutSuccessUrl("/login?logout=true")
              .permitAll());
      return http.build();
    }
  }
}