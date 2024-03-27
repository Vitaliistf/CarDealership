package org.vitaliistf.cardealership.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for security setup.
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private static final String[] PUBLIC_MATCHERS = {
            "/",
            "/index",
            "/register",
            "/login",
            "/webjars/**",
            "/process_login",
            "/img/**"};

    /**
     * Configures security for HTTP requests.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(PUBLIC_MATCHERS).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/process_login")
                        .permitAll().defaultSuccessUrl("/car", true)
                        .failureUrl("/login?error")
                )
                .logout((logout) -> logout.permitAll()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));

        return http.build();
    }

    /**
     * Provides a PasswordEncoder bean for encoding passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
