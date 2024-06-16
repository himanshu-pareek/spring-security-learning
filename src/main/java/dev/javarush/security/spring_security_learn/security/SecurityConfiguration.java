package dev.javarush.security.spring_security_learn.security;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http.authorizeHttpRequests(
                authz -> {
                    authz.requestMatchers(
                            "/", "/favicon.svg", "/css/*", "/error"
                    ).permitAll();
                    authz.anyRequest().authenticated();
                }
        );
        http.formLogin(form -> form.defaultSuccessUrl("/private"));
        http.logout(logout -> logout.logoutSuccessUrl("/"));

        // START - Filters
        http.addFilterBefore(new ProhibidoFilter(), AuthorizationFilter.class);
        http.addFilterBefore(new RobotAuthenticationFilter(), AuthorizationFilter.class);
        // END - Filters

        // START - Providers
        http.authenticationProvider(new DanielAuthenticationProvider());
        // END - Providers

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.withUsername("himanshu")
                .password("{noop}password")
                .roles("user")
                .build();
        UserDetails user2 = User.withUsername("admin")
                .password("{noop}admin")
                .roles("user", "admin")
                .build();

        UserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(user1);
        userDetailsManager.createUser(user2);
        return userDetailsManager;
    }

    @Bean
    ApplicationListener<AuthenticationSuccessEvent> onAuthenticationSuccess() {
        return event -> {
            System.out.printf(
                    "\uD83C\uDF89 [%s] %s\n",
                    event.getAuthentication().getClass().getSimpleName(),
                    event.getAuthentication().getName()
            );
        };
    }

}
