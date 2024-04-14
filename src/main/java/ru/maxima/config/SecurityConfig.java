package ru.maxima.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.maxima.services.PersonDetailsService;


@EnableWebSecurity
@Configuration
//@EnableMethodSecurity
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((s) ->
//                        s.requestMatchers("/books", "/auth/login", "/auth/registration", "/error").permitAll()
//                                .anyRequest().authenticated()
//                                .requestMatchers("/auth/admin").hasRole("ADMIN"))
//                .formLogin((s) -> s.loginPage("/auth/login")
//                        .loginProcessingUrl("/process_login")
//                        .defaultSuccessUrl("/books", true)
//                        .failureUrl("/auth/login?error"))
//                .logout((logout) -> logout.logoutSuccessUrl("/auth/login"));
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((s) ->
                        s.requestMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                                .anyRequest().authenticated())
                .formLogin((s) -> s.loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/people", true)
                        .failureUrl("/auth/login?error"))
                .logout((s) -> s.logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"));

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
