package com.arma.inz.compcal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class BasicAuthConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    public BasicAuthConfiguration(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.dataSource = dataSource;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/jasper/**" ,"/css/**", "/js/**", "/images/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "WWW-Authenticate"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors()
                .and()
                .authorizeRequests()
                .antMatchers("/user/registration").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/login/hash").permitAll()
                .antMatchers("/user/authorize").permitAll()
                .antMatchers("/activate/*").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/0.js").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/main.js").permitAll()
                .antMatchers("/polyfills.js").permitAll()
                .antMatchers("/runtime.js").permitAll()
                .antMatchers("/styles.js").permitAll()
                .antMatchers("/vendor.js").permitAll()
                .antMatchers("/index.html").permitAll()
                .anyRequest()
                .authenticated()
                .antMatchers(HttpMethod.GET,"/kpir/income").authenticated()
                .and()
                .formLogin().loginPage("/index.html")
                .and()
                .httpBasic().disable()
        ;

    }


}