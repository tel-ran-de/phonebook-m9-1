package com.telran.phonebookapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.phonebookapi.security.errorhandling.Http401UnauthenticatedEntryPoint;
import com.telran.phonebookapi.security.filter.JwtAuthenticationFilter;
import com.telran.phonebookapi.security.filter.UserLoginAuthenticationFilter;
import com.telran.phonebookapi.security.service.DbUserDetailService;
import com.telran.phonebookapi.security.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserAuthenticationSecurity extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_PATH = "/api/user/login";

    private final DbUserDetailService userDetailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ObjectMapper om;
    private final JwtService jwtService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/user/**",
                        "/v2/api-docs",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**"
                )
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new UserLoginAuthenticationFilter(LOGIN_PATH, authenticationManager(), om, jwtService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(userDetailService, jwtService, "Access-Token"),
                        AbstractPreAuthenticatedProcessingFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new Http401UnauthenticatedEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .httpBasic().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Collections.singletonList("Access-Token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder);
    }

}
