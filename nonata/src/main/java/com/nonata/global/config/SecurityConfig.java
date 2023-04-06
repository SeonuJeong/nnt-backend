package com.nonata.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.nonata.domain.repository.UserRepository;
import com.nonata.global.jwt.filter.JwtAuthenticationProcessingFilter;
import com.nonata.global.jwt.service.JwtService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserRepository userRepository;
  
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.formLogin().disable() 
        	.httpBasic().disable() 
        	.csrf().disable() 
        	.headers().frameOptions().disable()
        .and()
        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        	.authorizeHttpRequests()
        	.requestMatchers("/","","/assets/**","/index.html","/favicon.ico").permitAll()
        	.requestMatchers("/auth/**").permitAll()
        	.anyRequest().authenticated();           
  
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), LogoutFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
        return jwtAuthenticationFilter;
    }
}
