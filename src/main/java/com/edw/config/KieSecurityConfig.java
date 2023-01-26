package com.edw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * <pre>
 *     com.edw.config.KieSecurityConfig
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 25 Jan 2023 21:02
 */
@Configuration("kieServerSecurity")
@EnableWebSecurity
public class KieSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/rest/server*").authenticated()
                .and()
                .httpBasic();
    }

    /**
     * set your username and password here, we can also connect it into ldap, AD, or existing user-management table
     * by using Keycloak
     *
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        auth.inMemoryAuthentication()
                .withUser("kieserver").password(encoder.encode("password")).roles("kie-server");

        auth.inMemoryAuthentication()
                .withUser("wbadmin").password(encoder.encode("wbadmin")).roles("kie-server");

        auth.inMemoryAuthentication()
                .withUser("manager").password(encoder.encode("password")).roles("manager");

        auth.inMemoryAuthentication()
                .withUser("analyst").password(encoder.encode("password")).roles("kie-server");

        auth.inMemoryAuthentication()
                .withUser("user").password(encoder.encode("password")).roles("kie-server");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.HEAD.name(),
                HttpMethod.POST.name(), HttpMethod.DELETE.name(), HttpMethod.PUT.name()));
        corsConfiguration.applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}