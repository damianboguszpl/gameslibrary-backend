package pl.pollub.gameslibrary.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.pollub.gameslibrary.Filters.CustomAuthenticationFilter;
import pl.pollub.gameslibrary.Filters.CustomAuthorizationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @SuppressWarnings("unused")
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @SuppressWarnings("unused")
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
//                        .allowedMethods(CorsConfiguration.ALL)
                        .allowedMethods("*")
//                        .allowedOriginPatterns(CorsConfiguration.ALL);
                        .allowedHeaders(CorsConfiguration.ALL);
            }
        };
    }

    @Bean
    @SuppressWarnings("unused")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(null));
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");

        http.cors().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // **permit OPTIONS call to all**

                .antMatchers("/auth/login/**", "/user/register/**", "/auth/token/refresh/**").permitAll()

                .antMatchers(POST, "/role/**").hasAnyAuthority("ADMIN_ROLE")
                .antMatchers(GET, "/role/**").hasAnyAuthority("USER_ROLE")

                .antMatchers(POST,"/user").permitAll()
                .antMatchers(POST, "/user/**").hasAnyAuthority("ADMIN_ROLE")    // to check
                .antMatchers(PUT, "/user/**").hasAnyAuthority("USER_ROLE")
//                .antMatchers(GET,"/user/email/**").permitAll()
                .antMatchers(GET, "/user/**").hasAnyAuthority("USER_ROLE")

                .antMatchers(POST, "/app/**").hasAnyAuthority("ADMIN_ROLE")
                .antMatchers(GET, "/app/**").permitAll()
//                .antMatchers(GET, "/app/**").hasAnyAuthority("USER_ROLE")

                .antMatchers(POST, "/review/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(GET, "/review/**").permitAll()
//                .antMatchers(GET, "/review/**").hasAnyAuthority("USER_ROLE")

                .antMatchers(POST, "/favapp/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(GET, "/favapp/**").hasAnyAuthority("USER_ROLE")

                .anyRequest().authenticated();

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
//             .httpBasic(Customizer.withDefaults());
        .httpBasic().disable()
        .csrf().disable();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(@Nullable  AuthenticationConfiguration authenticationConfiguration) throws Exception {
        assert authenticationConfiguration != null;
        return authenticationConfiguration.getAuthenticationManager();
    }

}
