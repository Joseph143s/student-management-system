package Mangement.StudentManagement.config;


import Mangement.StudentManagement.ServiceImpl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,UserDetailsServiceImpl userDetailService){

        this.jwtAuthFilter=jwtAuthFilter;
        this.userDetailService=userDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ───────── COURSE ─────────
                        .requestMatchers(HttpMethod.GET, "/courses/**")
                        .hasAnyRole("STUDENT","FACULTY","ADMIN")

                        .requestMatchers(HttpMethod.POST, "/courses/**")
                        .hasAnyRole("FACULTY","ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/courses/**")
                        .hasAnyRole("FACULTY","ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/courses/**")
                        .hasRole("ADMIN")

                        // ───────── STUDENT ─────────
                        .requestMatchers(HttpMethod.GET, "/students/**")
                        .hasAnyRole("STUDENT","FACULTY","ADMIN")

                        .requestMatchers(HttpMethod.POST, "/students/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/students/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/students/**")
                        .hasRole("ADMIN")

                        // ───────── FACULTY ─────────
                        .requestMatchers("/faculties/**")
                        .hasRole("ADMIN")

                        // ───────── DEPARTMENT ─────────
                        .requestMatchers(HttpMethod.GET, "/departments/**")
                        .hasAnyRole("STUDENT","FACULTY","ADMIN")

                        .requestMatchers(HttpMethod.POST, "/departments/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/departments/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/departments/**")
                        .hasRole("ADMIN")

                        // ───────── ADDRESS ─────────
                        .requestMatchers("/addresses/**")
                        .hasAnyRole("STUDENT","ADMIN")

                        // ───────── ENROLLMENT ─────────
                        .requestMatchers(HttpMethod.POST, "/enrollments/**")
                        .hasRole("STUDENT")

                        .requestMatchers(HttpMethod.GET, "/enrollments/**")
                        .hasAnyRole("STUDENT","FACULTY","ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/enrollments/**")
                        .hasAnyRole("STUDENT","FACULTY","ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/enrollments/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean

    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
    throws Exception{
        return config.getAuthenticationManager();
    }
}