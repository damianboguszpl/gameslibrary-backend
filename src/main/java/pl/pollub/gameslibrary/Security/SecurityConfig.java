package pl.pollub.gameslibrary.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.pollub.gameslibrary.Filters.CustomAuthenticationFilter;
import pl.pollub.gameslibrary.Filters.CustomAuthorizationFilter;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
//@CrossOrigin
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
//                registry
//                        .addMapping("/**").allowedOrigins("http://localhost:3000")
//                        .allowedMethods(CorsConfiguration.ALL)
//                        .allowedHeaders(CorsConfiguration.ALL)
//                        .allowedOriginPatterns(CorsConfiguration.ALL);
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("*")
                        .allowedHeaders(CorsConfiguration.ALL);

            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(null));
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");

//        http.csrf( csrf -> csrf.disable());
//        http.csrf().disable();
        http.cors().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // **permit OPTIONS call to all**

                .antMatchers("/auth/login/**", "/token/refresh/**").permitAll()
                .antMatchers(POST,"/user").permitAll()
//                .antMatchers(GET,"/user/email/**").permitAll()
                .antMatchers(GET, "/user/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(GET, "/role/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(GET, "/app/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(GET, "/review/**").permitAll()
//                .antMatchers(GET, "/review/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(GET, "/favapp/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(POST, "/user/**").hasAnyAuthority("ADMIN_ROLE")
                .antMatchers(POST, "/role/**").hasAnyAuthority("ADMIN_ROLE")
                .antMatchers(POST, "/app/**").hasAnyAuthority("ADMIN_ROLE")
                .antMatchers(POST, "/review/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(POST, "/favapp/**").hasAnyAuthority("USER_ROLE")
                .anyRequest().authenticated();
//        .and().cors().configurationSource(corsConfigurationSource());
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
//             .httpBasic(Customizer.withDefaults());
                .httpBasic().disable()
                .csrf().disable();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
