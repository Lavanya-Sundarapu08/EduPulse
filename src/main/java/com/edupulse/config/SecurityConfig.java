package com.edupulse.config;

import com.edupulse.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authProvider())
            .authorizeHttpRequests(auth -> auth
                // Public: student feedback pages, static assets
                .requestMatchers(
                    "/", "/feedback/**", "/api/faculty/**",
                    "/css/**", "/js/**", "/images/**", "/favicon.ico"
                ).permitAll()
                // Login page only
                .requestMatchers("/login").permitAll()
                // Password change - only for authenticated users
                .requestMatchers("/password/change").authenticated()
                // Staff only
                .requestMatchers("/staff/**").hasRole("STAFF")
                // Principal only
                .requestMatchers("/principal/**").hasRole("PRINCIPAL")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((req, res, auth) -> {
                    boolean isPrincipal = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_PRINCIPAL"));
                    res.sendRedirect(isPrincipal ? "/principal/dashboard" : "/staff/dashboard");
                })
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            );
        return http.build();
    }
}